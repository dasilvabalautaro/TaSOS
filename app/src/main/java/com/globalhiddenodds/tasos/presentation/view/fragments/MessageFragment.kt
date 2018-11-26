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
import com.globalhiddenodds.tasos.presentation.plataform.BaseActivity
import com.globalhiddenodds.tasos.presentation.plataform.BaseFragment
import com.globalhiddenodds.tasos.presentation.presenter.LiveDataMessageContactViewModel
import com.globalhiddenodds.tasos.presentation.presenter.SendMessageViewModel
import com.globalhiddenodds.tasos.presentation.presenter.UpdateStateMessageViewModel
import com.globalhiddenodds.tasos.presentation.view.activities.MessageActivity
import com.globalhiddenodds.tasos.tools.Constants
import com.globalhiddenodds.tasos.tools.Variables
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
    private lateinit var liveDataMessageContactViewModel: LiveDataMessageContactViewModel
    private lateinit var sendMessageViewModel: SendMessageViewModel
    private lateinit var updateStateMessageViewModel: UpdateStateMessageViewModel

    override fun layoutId() = R.layout.view_message

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)

        Variables.source = (arguments?.get(paramGroup)
                as GroupMessageView).source

        liveDataMessageContactViewModel = viewModel(viewModelFactory) {
            observe(result, ::resultMessages)
            failure(failure, ::handleFailure)
        }

        sendMessageViewModel = viewModel(viewModelFactory) {
            observe(result, ::resultSendMessage)
            failure(failure, ::handleFailure)
        }

        updateStateMessageViewModel = viewModel(viewModelFactory) {
            observe(result, ::resultUpdateMessage)
            failure(failure, ::handleFailure)
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeView()
        if (firstTimeCreated(savedInstanceState)) {
        }

        ib_send.setOnClickListener { executeSend() }
    }

    override fun onResume() {
        super.onResume()
        (activity as MessageActivity).tv_name.text = (arguments!!.get(paramGroup)
                as GroupMessageView).source
        val quantity = (arguments!!.get(paramGroup)
                as GroupMessageView).quantity
        if (quantity > 0){
            updateStateMessageViewModel.source = (arguments!!.get(paramGroup)
                    as GroupMessageView).source
            updateStateMessageViewModel.updateStateMessages()
            (arguments!!.get(paramGroup)
                    as GroupMessageView).quantity = 0
        }
    }

    override fun onPause() {
        super.onPause()
        updateStateMessageViewModel.source = (arguments!!.get(paramGroup)
                as GroupMessageView).source
        updateStateMessageViewModel.updateStateMessages()
    }

    private fun initializeView(){
        rv_messages!!.setHasFixedSize(true)
        rv_messages!!.layoutManager = LinearLayoutManager(activity,
                LinearLayoutManager.VERTICAL, false)
        rv_messages.adapter = messageAdapter
        /*contactsAdapter.clickListener = { group, navigationExtras ->
            navigator.showMessages(activity!!, group, navigationExtras) }*/
    }

    private fun addRecyclerMessage(msg: String){
        val source = Constants.user.id
        val dateMessage = System.currentTimeMillis()
        val target = (arguments!!.get(paramGroup)
                as GroupMessageView).source
        val messageView = MessageView(source, target, msg,
                dateMessage, 0, 0, true )
        val listMessage = messageAdapter
                .collection.toMutableList()
        listMessage.add(messageView)
        messageAdapter.collection = listMessage
    }

    private fun sendMessage(msg: String){
        val target = (arguments!!.get(paramGroup)
                as GroupMessageView).source
        val map = mapOf("idTarget" to target,
                "message" to msg,
                "state" to 1, "source" to Constants.user.id,
                "type" to 0, "target" to target)
        sendMessageViewModel.map = map
        sendMessageViewModel.sendMessage()

    }

    private fun executeSend(){
        val msg =  et_send.text.toString()
        if (!msg.isEmpty()){
            addRecyclerMessage(msg)
            sendMessage(msg)
            et_send.text.clear()
            hideKeyboard(activity as BaseActivity)
        }
    }


    private fun resultSendMessage(value: Boolean?){
        if (value != null && value){

            context!!.toast(getString(R.string.action_message))
        }

    }

    private fun resultUpdateMessage(value: Boolean?){
        if (value != null && value){
            context!!.toast(getString(R.string.action_message))
        }

    }


    private fun resultMessages(list: List<MessageView>?){
        messageAdapter.collection = list.orEmpty()

    }

    override fun renderFailure(@StringRes message: Int) {
        hideProgress()
        notify(message)
    }

}