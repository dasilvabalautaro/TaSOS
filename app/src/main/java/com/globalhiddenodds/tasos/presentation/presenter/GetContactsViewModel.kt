package com.globalhiddenodds.tasos.presentation.presenter

import android.arch.lifecycle.*
import com.globalhiddenodds.tasos.domain.interactor.GetContactsUseCase
import com.globalhiddenodds.tasos.domain.interactor.UseCase
import com.globalhiddenodds.tasos.models.persistent.database.data.SSetContacts
import com.globalhiddenodds.tasos.presentation.data.GroupMessageView
import com.globalhiddenodds.tasos.presentation.plataform.BaseViewModel
import javax.inject.Inject

class GetContactsViewModel @Inject constructor(private val getContactsUseCase:
                                               GetContactsUseCase):
        BaseViewModel() {


    var result: MutableLiveData<List<GroupMessageView>> = MutableLiveData()


    fun loadContacts() = getContactsUseCase(UseCase.None()){
        it.either(::handleFailure, ::handleListContacts)
    }

    private fun handleListContacts(contacts: List<SSetContacts>){
        this.result.value = contacts.map { GroupMessageView(it.source,
                0, 0) }

       /* contacts.observe(this, Observer { it ->
            this.result.value = it!!.map { GroupMessageView(it.source,
                    0, 0) }
        })*/
    }

}