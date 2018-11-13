package com.globalhiddenodds.tasos.presentation.presenter

import android.arch.lifecycle.MutableLiveData
import com.globalhiddenodds.tasos.domain.interactor.GetNewMessageUseCase
import com.globalhiddenodds.tasos.domain.interactor.UseCase
import com.globalhiddenodds.tasos.models.persistent.database.data.SSetNewMessageQuantity
import com.globalhiddenodds.tasos.presentation.data.GroupMessageView
import com.globalhiddenodds.tasos.presentation.plataform.BaseViewModel
import javax.inject.Inject

class GetNewMessageViewModel @Inject constructor(private val getNewMessageUseCase:
                                                 GetNewMessageUseCase): BaseViewModel() {
    var result: MutableLiveData<List<GroupMessageView>> = MutableLiveData()

    fun loadNewMessage() = getNewMessageUseCase(UseCase.None()){
        it.either(::handleFailure, ::handleNewMessages)
    }

    private fun handleNewMessages(list: List<SSetNewMessageQuantity>){
        this.result.value = list.map { GroupMessageView(it.source,
                0, it.quantity) }
    }
}