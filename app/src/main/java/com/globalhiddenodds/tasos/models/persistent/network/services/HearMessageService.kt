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
        //return super.onStartCommand(intent, flags, startId)
    }

    private fun listenerMessageEvent() {

        referenceRoot!!.child(rootUser).child(Constants.user.id)
                .addValueEventListener( object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val map: HashMap<*, *>? = dataSnapshot.value as HashMap<*, *>?
                        if (map != null && !Constants.user.id.isEmpty()){

                            getMessage(map)

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

    private fun getMessage(map: HashMap<*, *>){
        var msg = ""
        var source = ""
        var type = 0
        var state = 1
        var target = ""

        for ((k, v) in map) {
            if (v is HashMap<*, *>){
                when {
                    v["lastMessage"] != null -> msg = v["lastMessage"] as String
                }
                when {
                    v["source"] != null -> source = v["source"] as String
                }
                when {
                    v["type"] != null -> type = (v["type"] as Long).toInt()
                }
                when {
                    v["state"] != null -> state = (v["state"] as Long).toInt()
                }
                when {
                    v["target"] != null -> target = v["target"] as String
                }

            }
        }
        if (!msg.isEmpty()){
            val dateMessage = System.currentTimeMillis()
            val messageView = MessageView(source, target, msg,
                    dateMessage, type, state)
            saveMessageUseCase(messageView){
                it.either(::handleFailure, ::handleResult)}

        }
    }

    fun handleFailure(failure: Failure) {
        if (failure is Failure.DatabaseError){
            showLog("Failure Database")
        }
    }

    private fun handleResult(value: Boolean){
        showLog(value.toString())

    }
    private fun showLog(msg: String){
        println(msg)
    }
}
