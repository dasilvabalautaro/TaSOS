package com.globalhiddenodds.tasos.presentation.presenter

import android.arch.lifecycle.MutableLiveData
import com.globalhiddenodds.tasos.domain.interactor.DeleteUserUseCase
import com.globalhiddenodds.tasos.presentation.plataform.BaseViewModel
import javax.inject.Inject

class DeleteUserViewModel @Inject constructor(private val deleteUserUseCase:
                                              DeleteUserUseCase):
        BaseViewModel() {
    val result: MutableLiveData<Boolean> = MutableLiveData()

    var source: String? = null

    fun deleteUser() = deleteUserUseCase(source!!){
        it.either(::handleFailure, ::handleResult)}

    private fun handleResult(value: Boolean){

        result.value = value

    }

}