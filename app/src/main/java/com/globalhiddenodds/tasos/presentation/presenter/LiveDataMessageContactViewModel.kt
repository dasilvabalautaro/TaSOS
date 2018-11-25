package com.globalhiddenodds.tasos.presentation.presenter

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import com.globalhiddenodds.tasos.models.persistent.database.data.MessageData
import com.globalhiddenodds.tasos.models.persistent.database.interfaces.MessageDataDao
import com.globalhiddenodds.tasos.presentation.data.MessageView
import com.globalhiddenodds.tasos.presentation.plataform.BaseViewModel
import com.globalhiddenodds.tasos.tools.Variables
import javax.inject.Inject

class LiveDataMessageContactViewModel @Inject constructor(messageDataDao:
                                                          MessageDataDao):
        BaseViewModel()  {
    var source = Variables.source
    val result: LiveData<List<MessageView>> = Transformations
            .map(messageDataDao.getMessageOfContact(source), ::handleMessages)

    private fun handleMessages(list: List<MessageData>):
            List<MessageView>{
        val listResult: MutableList<MessageView> = ArrayList()
        list.forEach { listResult.add(MessageView(it.source, it.target, it.message,
                it.dateMessage, it.type, it.state, it.who)) }
        return listResult
    }
}