package com.globalhiddenodds.tasos.models.persistent.network.interfaces

import com.globalhiddenodds.tasos.domain.functional.Either
import com.globalhiddenodds.tasos.models.data.User
import com.globalhiddenodds.tasos.models.exception.Failure
import com.globalhiddenodds.tasos.tools.NetworkHandler
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject
import com.google.firebase.auth.UserProfileChangeRequest


interface RepositoryNetwork {
    fun start()
    fun getCurrentUser(user: User): Boolean
    fun createUser(user: User): Either<Failure, Boolean>
    fun signInUser(user: User): Either<Failure, Boolean>

    class Network @Inject constructor(private val networkHandler:
                                             NetworkHandler) : RepositoryNetwork {

        private var auth: FirebaseAuth? = null

        override fun start() {
            if (this.auth == null){
                this.auth = FirebaseAuth.getInstance()

            }

        }

        override fun getCurrentUser(user: User): Boolean {
            start()

            val currentUser = this.auth!!.currentUser

            return if(currentUser != null){
                if (!currentUser.displayName.isNullOrEmpty()){
                    user.id  = currentUser.displayName!!
                }else{
                    update(currentUser, user.id)
                }

                currentUser.getIdToken(true).addOnSuccessListener {
                    if (it.token != null){
                        user.token = it.token!!

                    }
                }
                true
            }else{
                false
            }

        }

        override fun createUser(user: User):
                Either<Failure, Boolean> {
            start()

            return when (networkHandler.isConnected) {
                true -> create(user)
                false, null -> Either.Left(Failure.NetworkConnection())
            }

        }

        private fun update(currentUser: FirebaseUser, id: String){

            val profileUpdates = UserProfileChangeRequest
                    .Builder()
                    .setDisplayName(id)
                    .build()

            currentUser.updateProfile(profileUpdates)
                .addOnCompleteListener { task: Task<Void> ->
                    if (!task.isSuccessful) {
                        println("Update Profile OK")
                    }else{
                        println("Update Profile Fail")
                    }
                }

        }

        private fun create(user: User): Either<Failure, Boolean>{

            this.auth!!.createUserWithEmailAndPassword(user.email,
                    user.password).addOnCompleteListener { task: Task<AuthResult> ->
                if (!task.isSuccessful) {
                    Either.Left(Failure.ServerError())
                }
            }
            return Either.Right(true)

        }

        override fun signInUser(user: User): Either<Failure, Boolean> {
            start()

            return when (networkHandler.isConnected) {
                true -> signIn(user)
                false, null -> Either.Left(Failure.NetworkConnection())
            }

        }

        private fun signIn(user: User): Either<Failure, Boolean>{

            this.auth!!.signInWithEmailAndPassword(user.email,
                    user.password).addOnCompleteListener { task: Task<AuthResult> ->
                if (!task.isSuccessful) {
                    Either.Left(Failure.ServerError())
                }
            }
            return Either.Right(true)

        }

    }
}

