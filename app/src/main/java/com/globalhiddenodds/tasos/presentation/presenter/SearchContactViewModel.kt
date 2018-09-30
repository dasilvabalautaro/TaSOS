package com.globalhiddenodds.tasos.presentation.presenter

import android.arch.lifecycle.MutableLiveData
import com.globalhiddenodds.tasos.domain.interactor.SearchContactUseCase
import com.globalhiddenodds.tasos.presentation.plataform.BaseViewModel
import javax.inject.Inject

class SearchContactViewModel @Inject constructor(private val searchContactUseCase:
                                                 SearchContactUseCase): BaseViewModel(){

    val result: MutableLiveData<Boolean> = MutableLiveData()
    var id: String? = null

    fun searchContact() = searchContactUseCase(id!!){
        it.either(::handleFailure, ::handleResult)}

    private fun handleResult(value: Boolean){

        result.value = value

    }

}