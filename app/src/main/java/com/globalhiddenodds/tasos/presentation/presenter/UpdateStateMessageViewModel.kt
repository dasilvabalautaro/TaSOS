package com.globalhiddenodds.tasos.presentation.presenter

import android.arch.lifecycle.MutableLiveData
import com.globalhiddenodds.tasos.domain.interactor.UpdateStateMessageUseCase
import com.globalhiddenodds.tasos.presentation.plataform.BaseViewModel
import javax.inject.Inject

class UpdateStateMessageViewModel @Inject constructor(private val updateStateMessageUseCase:
                                                      UpdateStateMessageUseCase):
        BaseViewModel() {
    val result: MutableLiveData<Boolean> = MutableLiveData()

    var source: String? = null

    fun updateStateMessages() = updateStateMessageUseCase(source!!){
        it.either(::handleFailure, ::handleResult)}

    private fun handleResult(value: Boolean){

        result.value = value

    }
}