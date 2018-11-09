package com.globalhiddenodds.tasos.domain.interactor

import com.globalhiddenodds.tasos.domain.functional.Either
import com.globalhiddenodds.tasos.models.exception.Failure
import com.globalhiddenodds.tasos.models.persistent.network.interfaces.HandleSendMessage
import com.globalhiddenodds.tasos.models.persistent.network.interfaces.ManagerUserCloud
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(private val handleSendMessage:
                                             HandleSendMessage):
        UseCase<Boolean, Map<String, Any>>() {

    override suspend fun run(params: Map<String, Any>): Either<Failure, Boolean> {
        return handleSendMessage.sendMessage(params)
    }

}