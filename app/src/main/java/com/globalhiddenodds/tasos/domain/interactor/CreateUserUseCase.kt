package com.globalhiddenodds.tasos.domain.interactor

import com.globalhiddenodds.tasos.domain.functional.Either
import com.globalhiddenodds.tasos.models.data.User
import com.globalhiddenodds.tasos.models.exception.Failure
import com.globalhiddenodds.tasos.models.persistent.network.interfaces.RepositoryNetwork
import javax.inject.Inject


class CreateUserUseCase @Inject constructor(private val repositoryNetwork:
                                            RepositoryNetwork):
        UseCase<Boolean, User>() {

    override suspend fun run(params: User): Either<Failure, Boolean> {
        return repositoryNetwork.createUser(params)
    }

}