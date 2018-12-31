package com.globalhiddenodds.tasos.presentation.view.fragments

import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.support.annotation.StringRes
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.globalhiddenodds.tasos.R
import com.globalhiddenodds.tasos.extension.observe
import com.globalhiddenodds.tasos.extension.failure
import com.globalhiddenodds.tasos.extension.viewModel
import com.globalhiddenodds.tasos.models.persistent.files.ManageFiles
import com.globalhiddenodds.tasos.models.persistent.network.FirebaseDbToRoom
import com.globalhiddenodds.tasos.presentation.component.MessageAdapter
import com.globalhiddenodds.tasos.presentation.data.GroupMessageView
import com.globalhiddenodds.tasos.presentation.data.MessageView
import com.globalhiddenodds.tasos.presentation.plataform.BaseFragment
import com.globalhiddenodds.tasos.presentation.presenter.LiveDataMessageContactViewModel
import com.globalhiddenodds.tasos.presentation.presenter.SendMessageViewModel
import com.globalhiddenodds.tasos.presentation.presenter.UpdateStateMessageViewModel
import com.globalhiddenodds.tasos.presentation.view.activities.MessageActivity
import com.globalhiddenodds.tasos.tools.Chronometer
import com.globalhiddenodds.tasos.tools.Constants
import com.globalhiddenodds.tasos.tools.EnablePermissions
import com.globalhiddenodds.tasos.tools.Variables
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.view_message.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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
    @Inject
    lateinit var firebaseDbToRoom: FirebaseDbToRoom
    @Inject
    lateinit var chronometer: Chronometer
    @Inject
    lateinit var enablePermissions: EnablePermissions
    @Inject
    lateinit var manageFiles: ManageFiles

    private lateinit var liveDataMessageContactViewModel: LiveDataMessageContactViewModel
    private lateinit var sendMessageViewModel: SendMessageViewModel
    private lateinit var updateStateMessageViewModel: UpdateStateMessageViewModel
    private var flagChronometer = false
    private val typeImage = 2

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

        val image = (activity as MessageActivity).observableImage.map { i -> i }
        disposable.add(image.observeOn(Schedulers.newThread())
                .map { i ->
                    kotlin.run {
                        return@map Bitmap.createScaledBitmap(i,
                                (i.width*0.9).toInt(),
                                (i.height*0.9).toInt(), true)
                    }
                }  //.observeOn(AndroidSchedulers.mainThread())
                .subscribe { resize ->
                    kotlin.run {
                        val msg = manageFiles.base641EncodedImage(resize)
                        sendMessage(msg, typeImage)
                    }
                })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeView()
        if (firstTimeCreated(savedInstanceState)) {
        }

        ib_send.setOnClickListener { executeSend() }
        ib_image.setOnClickListener { (activity as MessageActivity).gallery() }
        ib_camera.setOnClickListener { (activity as MessageActivity).camera() }
        /*val imeManager = appComponent.context()
                .getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imeManager.showInputMethodPicker()*/
    }

    override fun onResume() {
        super.onResume()
        (activity as MessageActivity).tv_name.text = (arguments!!.get(paramGroup)
                as GroupMessageView).source
        (arguments!!.get(paramGroup)
                as GroupMessageView).quantity = 0
        GlobalScope.launch {
            firebaseDbToRoom.getTokenUser((arguments!!.get(paramGroup)
                    as GroupMessageView).source)
        }
    }

    override fun onPause() {
        super.onPause()
        GlobalScope.launch {
            updateStateMessageViewModel.source = (arguments!!.get(paramGroup)
                    as GroupMessageView).source
            updateStateMessageViewModel.updateStateMessages()

        }
        if (flagChronometer){
            flagChronometer = false

            GlobalScope.launch {
                chronometer.resetDataML(1)
            }
        }
    }

    private fun initializeView(){
        rv_messages!!.setHasFixedSize(true)
        rv_messages!!.layoutManager = LinearLayoutManager(activity,
                LinearLayoutManager.VERTICAL, false)
        rv_messages.adapter = messageAdapter
        /*contactsAdapter.clickListener = { group, navigationExtras ->
            navigator.showMessages(activity!!, group, navigationExtras) }*/
    }


    private fun sendMessage(msg: String, type: Int = 0){
        val target = (arguments!!.get(paramGroup)
                as GroupMessageView).source
        val map = mapOf("idTarget" to target,
                "message" to msg,
                "state" to 1, "source" to Constants.user.id,
                "type" to type, "target" to target)
        sendMessageViewModel.map = map
        sendMessageViewModel.sendMessage()

    }

    private fun executeSend(){
        val msg =  et_send.text.toString()
        if (!msg.isEmpty()){
            sendMessage(msg)
            et_send.text.clear()
            GlobalScope.launch{
                if (!flagChronometer && enablePermissions
                                .permissionWriteExternalStorage(activity!!)){
                    flagChronometer = true

                    chronometer.resetDataML(0)

                }
            }

            //hideKeyboard(activity as BaseActivity)
        }
    }

    private fun resultSendMessage(value: Boolean?){
        if (value != null && value){

            //context!!.toast(getString(R.string.action_message))
        }

    }

    private fun resultUpdateMessage(value: Boolean?){
        if (value != null && value){
            //context!!.toast(getString(R.string.action_message))
        }

    }


    private fun resultMessages(list: List<MessageView>?){

        if (!list.isNullOrEmpty()){

            GlobalScope.launch {
                messageAdapter.setObjectList(list as ArrayList<MessageView>)

                try {
                    activity!!.runOnUiThread {
                        //rv_messages!!.refreshDrawableState()
                        messageAdapter.notifyDataSetChanged()
                        Handler().postDelayed({
                            try {
                                rv_messages!!.scrollToPosition(messageAdapter.collection.count() - 1)


                            }catch (ex: KotlinNullPointerException){
                                println(ex.message)
                            }

                        }, 500)

                    }

                }catch (ex: KotlinNullPointerException){
                    println(ex.message)
                }
            }

        }else{
            println("Size list recycler EMPTY")
        }

    }

    override fun renderFailure(@StringRes message: Int) {
        hideProgress()
        notify(message)
    }

}