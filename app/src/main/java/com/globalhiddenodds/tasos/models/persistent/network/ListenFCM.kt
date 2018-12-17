package com.globalhiddenodds.tasos.models.persistent.network

import android.content.Context
import android.widget.Toast
import com.globalhiddenodds.tasos.R
import com.google.firebase.functions.FirebaseFunctions
import javax.inject.Inject
import javax.inject.Singleton
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.functions.FirebaseFunctionsException
import com.google.firebase.functions.HttpsCallableResult

@Singleton
class ListenFCM @Inject constructor(private val context: Context) {

    private var firebaseFunctions: FirebaseFunctions? = null

    fun start() {
        if (firebaseFunctions == null){
            firebaseFunctions = FirebaseFunctions.getInstance()
        }
    }

    private fun addListen(user_id: String, token: String, nameFunction: String):
            Task<String> {
        val data = mutableMapOf<String, Any?>()
        data["user_id"] = user_id as Any
        data["token"] = token

        return firebaseFunctions!!
                .getHttpsCallable(nameFunction)
                .call(data)
                .continueWith(Continuation<HttpsCallableResult,
                        String> { task: Task<HttpsCallableResult> ->
                    return@Continuation (if (task.result != null)
                        task.result!!.data.toString()
                    else throw Exception("Expression 'task.result' must not be null"))
                })
    }

    fun listenFromFCM(user_id: String, token: String, nameFunction: String){
        addListen(user_id, token, nameFunction)
            .addOnCompleteListener { task: Task<String> ->
                if (task.isSuccessful){
                    task.result

                    println(task.result.toString())
                }else{
                    if (task.exception is FirebaseFunctionsException ){
                        (task.exception as FirebaseFunctionsException)
                                .details.toString()

                    }else{
                        task.exception.toString()
                        println(task.exception.toString())
                    }
                }
        }


    }
}