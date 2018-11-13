package com.globalhiddenodds.tasos.domain.interactor

import com.globalhiddenodds.tasos.domain.functional.Either
import com.globalhiddenodds.tasos.models.exception.Failure
import com.globalhiddenodds.tasos.models.persistent.database.data.SSetNewMessageQuantity
import com.globalhiddenodds.tasos.models.persistent.database.interfaces.MessageDataDao
import javax.inject.Inject

class GetNewMessageUseCase @Inject constructor(private val messageDataDao:
                                               MessageDataDao):
        UseCase<List<SSetNewMessageQuantity>, UseCase.None>() {

    override suspend fun run(params: None): Either<Failure, List<SSetNewMessageQuantity>> {
        return try {

            val listData = messageDataDao.getNewMessage()

            when(!listData.isEmpty()){
                true -> Either.Right(listData)
                false -> Either.Left(Failure.DatabaseError())
            }


        }catch (exception: Throwable){
            Either.Left(Failure.DatabaseError())
        }

    }

}