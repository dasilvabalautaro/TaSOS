package com.globalhiddenodds.tasos.presentation.presenter

import android.arch.lifecycle.MutableLiveData
import com.globalhiddenodds.tasos.domain.interactor.SendMessageUseCase
import com.globalhiddenodds.tasos.presentation.plataform.BaseViewModel
import javax.inject.Inject

class SendMessageViewModel @Inject constructor(private val sendMessageUseCase:
                                               SendMessageUseCase): BaseViewModel() {
    val result: MutableLiveData<Boolean> = MutableLiveData()

    var map: Map<String, Any>? = null

    fun sendMessage() = sendMessageUseCase(map!!){
        it.either(::handleFailure, ::handleResult)}

    private fun handleResult(value: Boolean){

        result.value = value

    }

}