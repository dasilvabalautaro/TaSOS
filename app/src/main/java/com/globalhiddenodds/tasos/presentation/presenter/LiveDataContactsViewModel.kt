package com.globalhiddenodds.tasos.presentation.presenter

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import com.globalhiddenodds.tasos.models.persistent.database.data.SSetNewMessageQuantity
import com.globalhiddenodds.tasos.models.persistent.database.interfaces.MessageDataDao
import com.globalhiddenodds.tasos.presentation.data.GroupMessageView
import com.globalhiddenodds.tasos.presentation.plataform.BaseViewModel
import javax.inject.Inject

class LiveDataContactsViewModel @Inject constructor(messageDataDao:
                                                      MessageDataDao):
        BaseViewModel() {

    val result: LiveData<List<GroupMessageView>> = Transformations
            .map(messageDataDao.getLiveDataContacts(), ::handleContacts)

    private fun handleContacts(list: List<SSetNewMessageQuantity>):
            List<GroupMessageView>{
        val listResult: MutableList<GroupMessageView> = ArrayList()
        list.forEach { listResult.add(GroupMessageView(it.source, 0, it.quantity)) }
        return listResult
    }
}