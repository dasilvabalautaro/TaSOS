package com.globalhiddenodds.tasos.models.persistent.network.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.globalhiddenodds.tasos.App
import com.globalhiddenodds.tasos.di.ApplicationComponent
import com.globalhiddenodds.tasos.domain.interactor.SaveMessageUseCase
import com.globalhiddenodds.tasos.models.exception.Failure
import com.globalhiddenodds.tasos.presentation.data.MessageView
import com.globalhiddenodds.tasos.tools.Constants
import com.google.firebase.database.*
import java.util.HashMap
import javax.inject.Inject

class HearMessageService : Service() {

    private  val constLastMessage = "lastMessage"
    private  val constSource = "source"
    private  val constType = "type"
    private  val constState = "state"
    private  val constTarget = "target"

    private val appComponent: ApplicationComponent by
    lazy(mode = LazyThreadSafetyMode.NONE) {
        (application as App).appComponent
    }

    @Inject
    lateinit var saveMessageUseCase:
            SaveMessageUseCase

    private var database: FirebaseDatabase? = null
    private var referenceRoot: DatabaseReference? = null
    private val rootUser = "users"

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        appComponent.inject(this)
        showLog("onCreate")
        if (this.database == null){
            this.database = FirebaseDatabase.getInstance()
            this.referenceRoot = this.database!!.reference

        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        showLog("onStartCommand")

        listenerMessageEvent()

        return START_STICKY

    }

    private fun listenerMessageEvent() {

        referenceRoot!!.child(rootUser).child(Constants.user.id)
                .addValueEventListener( object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val map: HashMap<*, *>? = dataSnapshot.value as HashMap<*, *>?
                        if (map != null && !Constants.user.id.isEmpty()){

                            saveMessage(map)

                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {

                        showLog("loadPost:onCancelled: " + databaseError.toException())
                    }
                })

    }

    override fun onDestroy() {
        super.onDestroy()
        showLog("onDestroy")
    }

    private fun saveMessage(map: HashMap<*, *>){
        var msg = ""
        var source = ""
        var type = 0
        var state = 1
        var target = ""

        for ((k, v) in map) {
            when (k) {
                constLastMessage -> if(v != null) msg = v as String
                constSource -> if(v != null) source = v as String
                constType -> type = if(v != null && v is Long) (v).toInt() else (v as String).toInt()
                constState -> state = if(v != null && v is Long) (v).toInt() else (v as String).toInt()
                constTarget -> if(v != null) target = v as String
            }

        }

        if (!msg.isEmpty()){
            val dateMessage = System.currentTimeMillis()
            val messageView = MessageView(source, target, msg,
                    dateMessage, type, state, false)
            saveMessageUseCase(messageView){
                it.either(::handleFailure, ::handleResult)}

        }
    }

    private fun clearFields(){
        referenceRoot!!.child(rootUser)
                .child(Constants.user.id)
                .child(constLastMessage).setValue("")
        referenceRoot!!.child(rootUser)
                .child(Constants.user.id)
                .child(constSource).setValue("")
        referenceRoot!!.child(rootUser)
                .child(Constants.user.id)
                .child(constType).setValue("0")
        referenceRoot!!.child(rootUser)
                .child(Constants.user.id)
                .child(constState).setValue("1")
        referenceRoot!!.child(rootUser)
                .child(Constants.user.id)
                .child(constTarget).setValue("")

    }

    fun handleFailure(failure: Failure) {
        if (failure is Failure.DatabaseError){
            showLog("Failure Database")
        }
    }

    private fun handleResult(value: Boolean){
        if (value){
            clearFields()
        }
        showLog(value.toString())

    }
    private fun showLog(msg: String){
        println(msg)
    }
}
