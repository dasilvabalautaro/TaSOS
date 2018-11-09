package com.globalhiddenodds.tasos.models.persistent.network.interfaces

import com.globalhiddenodds.tasos.domain.functional.Either
import com.globalhiddenodds.tasos.models.exception.Failure
import com.globalhiddenodds.tasos.tools.NetworkHandler
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject

interface HandleSendMessage {
    fun sendMessage(map: Map<String, Any>):
            Either<Failure, Boolean>

    class SendMessage @Inject constructor(private val networkHandler:
                                          NetworkHandler): HandleSendMessage{

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

                    Either.Right(true)
                }
                false, null -> Either.Left(Failure.NetworkConnection())
            }

        }
    }
}