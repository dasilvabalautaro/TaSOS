package com.globalhiddenodds.tasos.models.persistent.network.interfaces

import com.globalhiddenodds.tasos.domain.functional.Either
import com.globalhiddenodds.tasos.models.data.User
import com.globalhiddenodds.tasos.models.data.UserCloud
import com.globalhiddenodds.tasos.models.exception.Failure
import com.globalhiddenodds.tasos.models.persistent.PreferenceRepository
import com.globalhiddenodds.tasos.models.persistent.network.RegisterTokenFCM
import com.globalhiddenodds.tasos.tools.Constants
import com.globalhiddenodds.tasos.tools.NetworkHandler
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Singleton


interface RepositoryNetwork {
    fun start()
    fun getCurrentUser(user: User): Boolean
    fun createUser(user: User): Either<Failure, Boolean>
    fun signInUser(user: User): Either<Failure, Boolean>

    @Singleton
    class Network @Inject constructor(private val networkHandler:
                                             NetworkHandler,
                                      private val registerTokenFCM:
                                      RegisterTokenFCM) :
            RepositoryNetwork {

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
                    Constants.user.id  = currentUser.displayName!!
                }else if (user.id.isNotEmpty()){
                    Constants.user.id = user.id
                    update(currentUser, user.id)
                }

                if (Constants.user.id.isNotEmpty()){
                    GlobalScope.launch { registerTokenFCM.getTokenFCM() }
                }

               /* currentUser.getIdToken(true).addOnSuccessListener {
                    if (it.token != null){
                        user.token = it.token!!
                        println("TOKEN ID TASOS: ${user.token}")
                    }
                }*/
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
            var result = true
            this.auth!!.createUserWithEmailAndPassword(user.email,
                    user.password).addOnCompleteListener { task: Task<AuthResult> ->
                if (!task.isSuccessful) {
                    result = false
                }
            }

            Thread.sleep(1000)
            return if (result){
                Either.Right(true)
            }else{
                Either.Left(Failure.ServerError())
            }


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

                if (task.isSuccessful) {
                    Constants.user.id = this.auth!!.currentUser!!.displayName!!
                }
            }

            Thread.sleep(1000)
            if (Constants.user.id.isEmpty()){
                return Either.Left(Failure.AuthenticateError())
            }
            GlobalScope.launch { registerTokenFCM.getTokenFCM() }
            return Either.Right(true)

        }

    }
}

