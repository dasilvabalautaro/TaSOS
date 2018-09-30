package com.globalhiddenodds.tasos.models.interfaces

import com.globalhiddenodds.tasos.domain.functional.Either
import com.globalhiddenodds.tasos.models.data.User
import com.globalhiddenodds.tasos.models.exception.Failure
import com.globalhiddenodds.tasos.tools.Constants
import com.globalhiddenodds.tasos.tools.NetworkHandler
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.experimental.delay
import javax.inject.Inject


interface RepositoryNetwork {
    fun start()
    fun getCurrentUser(user: User): Boolean
    fun createUser(user: User): Either<Failure, Boolean>

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
        private fun create(user: User): Either<Failure, Boolean>{

            this.auth!!.createUserWithEmailAndPassword(user.email,
                    user.password).addOnCompleteListener { task: Task<AuthResult> ->
                if (!task.isSuccessful) {
                    Either.Left(Failure.ServerError())
                }
            }
            return Either.Right(true)
           /* return try {
                val response = this.auth!!
                        .createUserWithEmailAndPassword(user.email, user.password)
                        .addOnCompleteListener{return@addOnCompleteListener}
                Thread.sleep(2000)

                if (response.isSuccessful) Either.Right(true)
                else Either.Left(Failure.ServerError())

            }catch (exception: Throwable){
                Either.Left(Failure.ServerError())
            }
*/
        }
    }
}


//{
/*this.auth!!.currentUser!!.getIdToken(true).addOnSuccessListener {
    if (it.token != null){
        user.token = it.token!!
    }
}*/
//Either.Right(true)}