package com.globalhiddenodds.tasos.models.persistent.network.interfaces


import com.globalhiddenodds.tasos.domain.functional.Either
import com.globalhiddenodds.tasos.models.data.User
import com.globalhiddenodds.tasos.models.data.UserCloud
import com.globalhiddenodds.tasos.models.exception.Failure
import com.globalhiddenodds.tasos.tools.NetworkHandler
import com.google.firebase.database.*
import java.util.HashMap
import javax.inject.Inject

interface ManagerUserCloud {

    fun createUser(user: User): Either<Failure, Boolean>

    fun searchContact(id: String): Either<Failure, Boolean>

    class ManagerUser @Inject constructor(private val networkHandler:
                                      NetworkHandler): ManagerUserCloud {

        private val rootUser = "users"
        private var database: FirebaseDatabase? = null
        private var referenceRoot: DatabaseReference? = null
        private var query: Query? = null

        init {

            if (this.database == null){
                this.database = FirebaseDatabase.getInstance()
                this.referenceRoot = this.database!!.reference

            }

        }

        private fun buildUserCloud(): UserCloud{
            val userCloud = UserCloud()
            userCloud.keyPublic = ""
            userCloud.lastMessage = ""
            userCloud.source = ""
            userCloud.target = ""
            userCloud.state = 0
            userCloud.type = 0
            userCloud.token = ""
            userCloud.data = ""
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

        override fun searchContact(id: String): Either<Failure, Boolean> {
            return when (networkHandler.isConnected) {
                true -> listenerSearchContact(id)
                false, null -> Either.Left(Failure.NetworkConnection())
            }

        }

        private fun listenerSearchContact(id: String): Either<Failure, Boolean>{
            var result = false
            this.query = this.referenceRoot!!.child(rootUser).child(id)

            this.query!!.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val map: HashMap<*, *>? = dataSnapshot.value as HashMap<*, *>?
                    if (map != null){

                        result = true
                    }

                }

                override fun onCancelled(databaseError: DatabaseError) {
                    println("loadPost:onCancelled: " + databaseError.toException())
                }
            })
            Thread.sleep(1000)
            return when(result){
                true -> Either.Right(true)
                false -> Either.Left(Failure.NetworkConnection())
            }

        }

    }
}




