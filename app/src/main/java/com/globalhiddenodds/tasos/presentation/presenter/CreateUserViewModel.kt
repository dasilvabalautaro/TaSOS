package com.globalhiddenodds.tasos.presentation.presenter

import android.arch.lifecycle.MutableLiveData
import com.globalhiddenodds.tasos.domain.interactor.CreateUserUseCase
import com.globalhiddenodds.tasos.presentation.plataform.BaseViewModel
import com.globalhiddenodds.tasos.tools.Constants
import javax.inject.Inject


class CreateUserViewModel @Inject constructor(private val createUserUseCase:
                                              CreateUserUseCase): BaseViewModel(){

    var result: MutableLiveData<Boolean> = MutableLiveData()

    fun createUser() = createUserUseCase(Constants.user){
        it.either(::handleFailure, ::handleResult)}


    private fun handleResult(value: Boolean){
        result.value = value

    }
}