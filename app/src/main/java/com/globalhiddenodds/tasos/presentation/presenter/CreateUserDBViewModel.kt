package com.globalhiddenodds.tasos.presentation.presenter

import android.arch.lifecycle.MutableLiveData
import com.globalhiddenodds.tasos.domain.interactor.CreateUserDBUseCase
import com.globalhiddenodds.tasos.models.data.UserCloud
import com.globalhiddenodds.tasos.presentation.plataform.BaseViewModel
import com.globalhiddenodds.tasos.tools.Constants
import javax.inject.Inject


class CreateUserDBViewModel @Inject constructor(private val createUserDBUseCase:
                                                CreateUserDBUseCase):
        BaseViewModel() {

    var result: MutableLiveData<Boolean> = MutableLiveData()

    fun createUser() = createUserDBUseCase(Constants.user){
        it.either(::handleFailure, ::handleResult)
    }

    private fun handleResult(value: Boolean){
        result.value = value

    }

}