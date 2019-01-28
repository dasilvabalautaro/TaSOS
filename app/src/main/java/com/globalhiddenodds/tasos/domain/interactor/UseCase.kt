package com.globalhiddenodds.tasos.domain.interactor

import com.globalhiddenodds.tasos.domain.functional.Either
import com.globalhiddenodds.tasos.models.exception.Failure
import kotlinx.coroutines.*
import kotlinx.coroutines.GlobalScope


abstract class UseCase<out Type, in Params> where Type : Any  {
    abstract suspend fun run(params: Params): Either<Failure, Type>

    operator fun invoke(params: Params,
                        onResult: (Either<Failure, Type>) -> Unit = {}) {
        val job = GlobalScope.async { run(params) }
        GlobalScope.launch(Dispatchers.Main) { onResult(job.await()) }

    }

    class None

}