package com.globalhiddenodds.tasos.models.persistent.network

import android.content.Context
import android.util.Log
import com.globalhiddenodds.tasos.R
import com.globalhiddenodds.tasos.tools.Constants
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RegisterTokenFCM @Inject constructor(private val context: Context) {

    companion object {
        private const val tag = "RegisterTokenFCM"
    }

    private val rootUser = "users"
    private val tokenFCM = "token"

    fun getTokenFCM(){
        FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w(RegisterTokenFCM.tag,
                                "getInstanceId failed", task.exception)
                        return@OnCompleteListener
                    }
                    val token = task.result!!.token
                    Constants.user.token = token
                    sendRegistrationTokenFCM(token)
                    val msg = context.getString(R.string.msg_token_fmt, token)
                    Log.d(RegisterTokenFCM.tag, msg)
                })
    }

    fun sendRegistrationTokenFCM(token: String){
        val database = FirebaseDatabase.getInstance()
        val referenceRoot = database.reference
        referenceRoot.child(rootUser)
                .child(Constants.user.id)
                .child(tokenFCM).setValue(token)
    }
}