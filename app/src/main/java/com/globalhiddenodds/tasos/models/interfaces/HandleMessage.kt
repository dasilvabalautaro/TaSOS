package com.globalhiddenodds.tasos.models.interfaces

import com.globalhiddenodds.tasos.domain.functional.Either
import com.globalhiddenodds.tasos.models.data.User
import com.globalhiddenodds.tasos.models.data.UserCloud
import com.globalhiddenodds.tasos.models.exception.Failure
import com.globalhiddenodds.tasos.tools.Constants
import com.globalhiddenodds.tasos.tools.NetworkHandler
import com.google.firebase.database.*
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import javax.inject.Inject

interface HandleMessage {
    fun createUser(user: User): Either<Failure, Boolean>
    fun sendMessage(idTarget: String, msg: String): Either<Failure, Boolean>
    fun searchContact(id: String): Either<Failure, Boolean>

    class Message @Inject constructor(private val networkHandler:
                                      NetworkHandler): HandleMessage {

        private val rootUser = "users"
        private val fieldMessage = "lastMessage"
        private var database: FirebaseDatabase? = null
        private var referenceRoot: DatabaseReference? = null
        private var query: Query? = null
        private var response = ""
        private var messageError = ""
        var observableResponse: Subject<String> = PublishSubject.create()
        var observableError: Subject<String> = PublishSubject.create()

        init {
            observableResponse.subscribe{response}
            observableError.subscribe{messageError}
            if (this.database == null){
                this.database = FirebaseDatabase.getInstance()
                this.referenceRoot = this.database!!.reference
                this.query = this.referenceRoot!!.child(rootUser).orderByKey()
            }
            listenerMessageEvent()
            listenerSearchContact()
        }

        private fun buildUserCloud(): UserCloud{
            val userCloud = UserCloud()
            userCloud.key_public = ""
            userCloud.lastMessage = ""
            userCloud.contacts.add("")
            return  userCloud
        }

        override fun createUser(user: User): Either<Failure, Boolean> {

            return when (networkHandler.isConnected) {
                true -> {
                    referenceRoot!!.child(rootUser)
                            .child(user.id).setValue(buildUserCloud())
                    Either.Right(true)
                }
                false, null -> Either.Left(Failure.NetworkConnection())
            }
        }

        override fun sendMessage(idTarget: String, msg: String): Either<Failure, Boolean> {

            return when (networkHandler.isConnected) {
                true -> {
                    referenceRoot!!.child(rootUser).child(idTarget)
                            .child(fieldMessage).setValue(msg)
                    Either.Right(true)
                }
                false, null -> Either.Left(Failure.NetworkConnection())
            }

        }

        override fun searchContact(id: String): Either<Failure, Boolean> {
            return when (networkHandler.isConnected) {
                true -> {
                    this.query = this.referenceRoot!!.child(rootUser).orderByKey().equalTo(id)
                    Either.Right(true)
                }
                false, null -> Either.Left(Failure.NetworkConnection())
            }

        }

        private fun listenerSearchContact(){
            this.query!!.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    println(dataSnapshot.value.toString())

                }

                override fun onCancelled(databaseError: DatabaseError) {
                    println("loadPost:onCancelled: " + databaseError.toException())
                }
            })
        }

        private fun listenerMessageEvent() {

            referenceRoot!!.child(rootUser).child(Constants.user.id)
                    .addValueEventListener( object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            response = dataSnapshot.value.toString()
                            observableResponse.onNext(response)
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            messageError = databaseError.message
                            observableError.onNext(messageError)
                            println("loadPost:onCancelled: " + databaseError.toException())
                        }
                    })

        }


    }
}

