package com.globalhiddenodds.tasos.domain.interactor

import com.globalhiddenodds.tasos.domain.functional.Either
import com.globalhiddenodds.tasos.models.exception.Failure
import com.globalhiddenodds.tasos.models.persistent.network.interfaces.ManagerUserCloud
import javax.inject.Inject

class SearchContactUseCase @Inject constructor(private val handleMessage:
                                               ManagerUserCloud):
        UseCase<Boolean, String>() {

    override suspend fun run(params: String): Either<Failure, Boolean> {
        return handleMessage.searchContact(params)
    }
}