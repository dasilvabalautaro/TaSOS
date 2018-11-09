package com.globalhiddenodds.tasos.domain.interactor

import android.arch.lifecycle.LiveData
import com.globalhiddenodds.tasos.domain.functional.Either
import com.globalhiddenodds.tasos.models.exception.Failure
import com.globalhiddenodds.tasos.models.persistent.database.data.SSetContacts
import com.globalhiddenodds.tasos.models.persistent.database.interfaces.MessageDataDao
import javax.inject.Inject

class GetContactsUseCase @Inject constructor(private val messageDataDao:
                                            MessageDataDao):
        UseCase<LiveData<List<SSetContacts>>, UseCase.None>() {

    override suspend fun run(params: None): Either<Failure,
            LiveData<List<SSetContacts>>> {
        return try {
            val listData = messageDataDao.getContacts()

            when(!listData.value!!.isEmpty()){
                true -> Either.Right(listData)
                false -> Either.Left(Failure.DatabaseError())
            }


        }catch (exception: Throwable){
            Either.Left(Failure.DatabaseError())
        }

    }


}