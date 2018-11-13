package com.globalhiddenodds.tasos.domain.interactor

import com.globalhiddenodds.tasos.domain.functional.Either
import com.globalhiddenodds.tasos.models.exception.Failure
import com.globalhiddenodds.tasos.models.persistent.database.data.MessageData
import com.globalhiddenodds.tasos.models.persistent.database.interfaces.MessageDataDao
import javax.inject.Inject

class GetMessageContactUseCase @Inject constructor(private val messageDataDao:
                                                   MessageDataDao):
        UseCase<List<MessageData>, String>() {

    override suspend fun run(params: String): Either<Failure, List<MessageData>> {
        return try {

            val listData = messageDataDao.getMessageOfContact(params)

            when(!listData.isEmpty()){
                true -> Either.Right(listData)
                false -> Either.Left(Failure.DatabaseError())
            }


        }catch (exception: Throwable){
            Either.Left(Failure.DatabaseError())
        }

    }

}