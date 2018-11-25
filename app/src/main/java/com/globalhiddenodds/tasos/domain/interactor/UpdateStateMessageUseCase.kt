package com.globalhiddenodds.tasos.domain.interactor

import com.globalhiddenodds.tasos.domain.functional.Either
import com.globalhiddenodds.tasos.models.exception.Failure
import com.globalhiddenodds.tasos.models.persistent.database.interfaces.MessageDataDao
import javax.inject.Inject

class UpdateStateMessageUseCase @Inject constructor(private val messageDataDao:
                                                    MessageDataDao):
        UseCase<Boolean, String>()  {
    override suspend fun run(params: String): Either<Failure, Boolean> {
        return try {
            messageDataDao.updateStateMessages(params)
            Either.Right(true)
        }catch (exception: Throwable){
            Either.Left(Failure.DatabaseError())
        }
    }

}