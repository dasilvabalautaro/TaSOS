package com.globalhiddenodds.tasos.presentation.presenter

import android.arch.lifecycle.MutableLiveData
import com.globalhiddenodds.tasos.domain.interactor.GetMessageContactUseCase
import com.globalhiddenodds.tasos.models.persistent.database.data.MessageData
import com.globalhiddenodds.tasos.presentation.data.MessageView
import com.globalhiddenodds.tasos.presentation.plataform.BaseViewModel
import javax.inject.Inject

class GetMessageContactViewModel @Inject constructor(private val getMessageContactUseCase:
                                                     GetMessageContactUseCase):
        BaseViewModel() {
    var result: MutableLiveData<List<MessageView>> = MutableLiveData()
    var source: String? = null

    fun loadMessage() = getMessageContactUseCase(source!!){
        it.either(::handleFailure, ::handleListMessage)
    }

    private fun handleListMessage(list: List<MessageData>){
        this.result.value = list.map { MessageView(it.source, it.target, it.message,
                it.dateMessage, it.type, it.state) }
    }


}