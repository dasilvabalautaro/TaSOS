package com.globalhiddenodds.tasos.models.persistent.network

import android.content.Context
import com.globalhiddenodds.tasos.domain.interactor.SaveMessageUseCase
import com.globalhiddenodds.tasos.models.exception.Failure
import com.globalhiddenodds.tasos.models.persistent.PreferenceRepository
import com.globalhiddenodds.tasos.presentation.component.Notify
import com.globalhiddenodds.tasos.presentation.data.MessageView
import com.globalhiddenodds.tasos.tools.Constants
import com.globalhiddenodds.tasos.tools.Variables
import com.google.firebase.database.*
import java.util.HashMap
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class FirebaseDbToRoom @Inject constructor(private val saveMessageUseCase:
                                           SaveMessageUseCase,
                                           private val notify: Notify,
                                           private val context: Context) {
    private  val constLastMessage = "lastMessage"
    private  val constSource = "source"
    private  val constType = "type"
    private  val constState = "state"
    private  val constTarget = "target"

    private var database: FirebaseDatabase? = null
    private var referenceRoot: DatabaseReference? = null
    private val rootUser = "users"


    fun startFirebase(){
        if (this.database == null){
            this.database = FirebaseDatabase.getInstance()
            this.referenceRoot = this.database!!.reference

        }

    }

    fun listenerMessageEvent() {

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
            showNotify(source)
        }
    }

    private fun handleFailure(failure: Failure) {
        if (failure is Failure.DatabaseError){
            showLog("Failure Database")
        }
    }

    private fun handleResult(value: Boolean){
        if (value){
            clearFieldsFirebase()
        }
        showLog(value.toString())

    }

    private fun clearFieldsFirebase(){
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

    fun getTokenUser(userId: String){
        referenceRoot!!.child(rootUser)
            .child(userId)
            .child("token")
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    Variables.tokenTarget = dataSnapshot.value.toString()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Variables.tokenTarget  = ""
                    showLog("loadPost:onCancelled: " + databaseError.toException())
                }
            })
    }

    fun clearValueFieldFirebase(field: String){
        referenceRoot!!.child(rootUser)
                .child(Constants.user.id)
                .child(field).setValue("")
    }

    private fun showNotify(source: String){
        val prefs = PreferenceRepository.customPrefs(context,
                Constants.preference_tasos)
        val runProcess = prefs.getString(Constants.run, "")
        if (runProcess.isNotEmpty() && runProcess == "0"){
            notify.sendNotification(source)
        }

    }

    private fun showLog(msg: String){
        println(msg)
    }
}