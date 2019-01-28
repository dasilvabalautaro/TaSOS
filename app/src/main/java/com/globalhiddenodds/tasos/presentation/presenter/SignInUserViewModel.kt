package com.globalhiddenodds.tasos.presentation.presenter

import android.arch.lifecycle.MutableLiveData
import com.globalhiddenodds.tasos.domain.interactor.SignInUserUseCase
import com.globalhiddenodds.tasos.presentation.plataform.BaseViewModel
import com.globalhiddenodds.tasos.tools.Constants
import javax.inject.Inject

class SignInUserViewModel @Inject constructor(private val signInUserUseCase:
                                              SignInUserUseCase): BaseViewModel() {
    var result: MutableLiveData<Boolean> = MutableLiveData()

    fun signInUser() = signInUserUseCase(Constants.user){
        it.either(::handleFailure, ::handleResult)}


    private fun handleResult(value: Boolean){
        result.value = value

    }

}