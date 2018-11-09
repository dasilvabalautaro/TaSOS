package com.globalhiddenodds.tasos.presentation.presenter

import android.arch.lifecycle.MutableLiveData
import com.globalhiddenodds.tasos.domain.interactor.SaveMessageUseCase
import com.globalhiddenodds.tasos.presentation.data.MessageView
import com.globalhiddenodds.tasos.presentation.plataform.BaseViewModel
import javax.inject.Inject

class SaveMessageViewModel @Inject constructor(private val saveMessageUseCase:
                                               SaveMessageUseCase):
        BaseViewModel() {

    val result: MutableLiveData<Boolean> = MutableLiveData()

    var messageView: MessageView? = null

    fun saveMessage() = saveMessageUseCase(messageView!!){
        it.either(::handleFailure, ::handleResult)}

    private fun handleResult(value: Boolean){

        result.value = value

    }


}