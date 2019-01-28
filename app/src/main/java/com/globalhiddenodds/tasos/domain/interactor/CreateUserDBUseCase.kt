package com.globalhiddenodds.tasos.domain.interactor

import com.globalhiddenodds.tasos.domain.functional.Either
import com.globalhiddenodds.tasos.models.data.User
import com.globalhiddenodds.tasos.models.exception.Failure
import com.globalhiddenodds.tasos.models.persistent.network.interfaces.ManagerUserCloud
import javax.inject.Inject

class CreateUserDBUseCase @Inject constructor(private val managerUserCloud:
                                              ManagerUserCloud):
        UseCase<Boolean, User>() {
    override suspend fun run(params: User): Either<Failure, Boolean> {
        return managerUserCloud.createUser(params)
    }

}