package com.globalhiddenodds.tasos.presentation.view.fragments

import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.globalhiddenodds.tasos.R
import com.globalhiddenodds.tasos.extension.addDecorationRecycler
import com.globalhiddenodds.tasos.extension.observe
import com.globalhiddenodds.tasos.extension.failure
import com.globalhiddenodds.tasos.extension.viewModel
import com.globalhiddenodds.tasos.presentation.component.MessageAdapter
import com.globalhiddenodds.tasos.presentation.data.GroupMessageView
import com.globalhiddenodds.tasos.presentation.data.MessageView
import com.globalhiddenodds.tasos.presentation.plataform.BaseFragment
import com.globalhiddenodds.tasos.presentation.presenter.GetMessageContactViewModel
import com.globalhiddenodds.tasos.presentation.view.activities.MessageActivity
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.view_message.*

import javax.inject.Inject

class MessageFragment: BaseFragment() {

    companion object {
        private const val paramGroup = "param_group"
        fun forGroup(group: GroupMessageView): MessageFragment{
            val messageFragment = MessageFragment()
            val arguments = Bundle()
            arguments.putParcelable(paramGroup, group)
            messageFragment.arguments = arguments

            return messageFragment
        }
    }

    @Inject
    lateinit var messageAdapter: MessageAdapter

    private lateinit var getMessageContactViewModel: GetMessageContactViewModel

    override fun layoutId() = R.layout.view_message

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        getMessageContactViewModel = viewModel(viewModelFactory) {
            observe(result, ::resultMessages)
            failure(failure, ::handleFailure)
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeView()
        if (firstTimeCreated(savedInstanceState)) {
            getMessageContactViewModel.source = (arguments?.get(paramGroup)
                    as GroupMessageView).source
            getMessageContactViewModel.loadMessage()

        }
    }

    override fun onResume() {
        super.onResume()
        (activity as MessageActivity).tv_name.text = (arguments!!.get(paramGroup)
                as GroupMessageView).source
    }
    private fun initializeView(){
        rv_messages!!.setHasFixedSize(true)
        rv_messages!!.layoutManager = LinearLayoutManager(activity,
                LinearLayoutManager.VERTICAL, false)
        addDecorationRecycler(rv_messages, context!!)
        rv_messages.adapter = messageAdapter
        /*contactsAdapter.clickListener = { group, navigationExtras ->
            navigator.showMessages(activity!!, group, navigationExtras) }*/
    }

    private fun resultMessages(list: List<MessageView>?){
        messageAdapter.collection = list.orEmpty()

    }

    override fun renderFailure(@StringRes message: Int) {
        hideProgress()
        notify(message)
    }

}