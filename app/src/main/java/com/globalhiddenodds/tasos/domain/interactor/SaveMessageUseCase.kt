package com.globalhiddenodds.tasos.domain.interactor

import com.globalhiddenodds.tasos.domain.functional.Either
import com.globalhiddenodds.tasos.models.exception.Failure
import com.globalhiddenodds.tasos.models.persistent.database.data.MessageData
import com.globalhiddenodds.tasos.models.persistent.database.interfaces.MessageDataDao
import com.globalhiddenodds.tasos.presentation.data.MessageView
import javax.inject.Inject

class SaveMessageUseCase @Inject constructor(private val messageDataDao:
                                             MessageDataDao):
        UseCase<Boolean, MessageView>() {

    override suspend fun run(params: MessageView): Either<Failure, Boolean> {
        return try {
            val messageData = MessageData(0, params.source, params.target,
                    params.message, params.dateMessage, params.type, params.state)
            messageDataDao.insert(messageData)
            Either.Right(true)
        }catch (exception: Throwable){
            Either.Left(Failure.DatabaseError())
        }

    }

}