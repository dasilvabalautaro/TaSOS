package com.globalhiddenodds.tasos.models.persistent.network.interfaces

import com.globalhiddenodds.tasos.domain.functional.Either
import com.globalhiddenodds.tasos.domain.interactor.SaveMessageUseCase
import com.globalhiddenodds.tasos.models.exception.Failure
import com.globalhiddenodds.tasos.presentation.data.MessageView
import com.globalhiddenodds.tasos.tools.NetworkHandler
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject

interface HandleSendMessage {
    fun sendMessage(map: Map<String, Any>):
            Either<Failure, Boolean>

    class SendMessage @Inject constructor(private val networkHandler:
                                          NetworkHandler,
                                          private val saveMessageUseCase:
                                          SaveMessageUseCase): HandleSendMessage{

        private var database: FirebaseDatabase? = null
        private var referenceRoot: DatabaseReference? = null
        private val fieldMessage = "lastMessage"
        private val fieldState = "state"
        private val fieldSource = "source"
        private val fieldType = "type"
        private val fieldTarget = "target"

        init {

            if (this.database == null){
                this.database = FirebaseDatabase.getInstance()
                this.referenceRoot = this.database!!.reference

            }

        }

        override fun sendMessage(map: Map<String, Any>):
                Either<Failure, Boolean> {

            val idTarget = map["idTarget"]!!
            val message = map["message"]!!
            val state = map["state"]!!
            val source = map["source"]!!
            val type = map["type"]
            val target = map["target"]

            return when (networkHandler.isConnected) {
                true -> {
                    val values = mapOf("users/$idTarget/$fieldMessage" to message,
                            "users/$idTarget/$fieldState" to state,
                            "users/$idTarget/$fieldSource" to source,
                            "users/$idTarget/$fieldType" to type,
                            "users/$idTarget/$fieldTarget" to target)

                    this.referenceRoot!!.updateChildren(values)
                    if(type == 0){
                        //Change source for target in database
                        saveDbMessage(target as String, source as String,
                                message as String, type as Int, state as Int)

                    }
                    Either.Right(true)
                }
                false, null -> Either.Left(Failure.NetworkConnection())
            }

        }

        private fun saveDbMessage(source: String, target: String,
                                  msg: String, type: Int, state: Int){

            val dateMessage = System.currentTimeMillis()
            val messageView = MessageView(source, target, msg,
                    dateMessage, type, state, true)
            saveMessageUseCase(messageView) {
                it.either(::handleFailure, ::handleResult)
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
}