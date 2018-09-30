package com.globalhiddenodds.tasos.domain.interactor

import com.globalhiddenodds.tasos.domain.functional.Either
import com.globalhiddenodds.tasos.models.data.User
import com.globalhiddenodds.tasos.models.data.UserCloud
import com.globalhiddenodds.tasos.models.exception.Failure
import com.globalhiddenodds.tasos.models.interfaces.HandleMessage
import javax.inject.Inject

class CreateUserDBUseCase @Inject constructor(private val handleMessage:
                                              HandleMessage):
        UseCase<Boolean, User>() {
    override suspend fun run(params: User): Either<Failure, Boolean> {
        return handleMessage.createUser(params)
    }

}