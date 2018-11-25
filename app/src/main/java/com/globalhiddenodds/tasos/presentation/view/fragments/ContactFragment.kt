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
import com.globalhiddenodds.tasos.presentation.component.ContactsAdapter
import com.globalhiddenodds.tasos.presentation.data.GroupMessageView
import kotlinx.android.synthetic.main.view_contacts.*
import com.globalhiddenodds.tasos.presentation.navigation.Navigator
import com.globalhiddenodds.tasos.presentation.plataform.BaseFragment
import com.globalhiddenodds.tasos.presentation.presenter.*
import com.globalhiddenodds.tasos.tools.Constants
import org.jetbrains.anko.noButton
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.yesButton
import javax.inject.Inject

class ContactFragment: BaseFragment() {
    @Inject
    lateinit var navigator: Navigator
    @Inject
    lateinit var contactsAdapter: ContactsAdapter

    private lateinit var searchContactViewModel: SearchContactViewModel
    private lateinit var sendMessageViewModel: SendMessageViewModel
    private lateinit var liveDataContactsViewModel: LiveDataContactsViewModel

    private var idTarget = ""

    override fun layoutId() = R.layout.view_contacts
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        searchContactViewModel = viewModel(viewModelFactory) {
            observe(result, ::resultSearch)
            failure(failure, ::handleFailure)
        }

        sendMessageViewModel = viewModel(viewModelFactory) {
            observe(result, ::resultMessage)
            failure(failure, ::handleFailure)
        }

        liveDataContactsViewModel = viewModel(viewModelFactory) {
            observe(result, ::resultLiveDataContact)
            failure(failure, ::handleFailure)
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeView()
    }

    override fun onResume() {
        super.onResume()
        context!!.toast(Constants.user.id)
    }
    private fun initializeView(){
        rv_contacts!!.setHasFixedSize(true)
        rv_contacts!!.layoutManager = LinearLayoutManager(activity,
                LinearLayoutManager.VERTICAL, false)
        addDecorationRecycler(rv_contacts, context!!)
        rv_contacts.adapter = contactsAdapter
        contactsAdapter.clickListener = { group, navigationExtras ->
             navigator.showMessages(activity!!, group, navigationExtras) }
    }

    fun searchContact(search: String){
        this.idTarget = search
        searchContactViewModel.id = search
        searchContactViewModel.searchContact()

    }

    private fun sendMessage(){
        val usr = Constants.user.id

        val map = mapOf("idTarget" to idTarget,
                "message" to
                        "$usr ${getString(R.string.action_invitation)}",
                "state" to 1, "source" to Constants.user.id,
                "type" to 1, "target" to idTarget)
        sendMessageViewModel.map = map
        sendMessageViewModel.sendMessage()

    }


    private fun resultLiveDataContact(list: List<GroupMessageView>?){
        contactsAdapter.collection = list.orEmpty()
    }


    private fun resultMessage(value: Boolean?){
        if (value != null && value){
            context!!.toast(getString(R.string.action_message))
        }

    }

    private fun resultSearch(value: Boolean?){
        if (value != null && value){
            invitationDialog()
        }
    }

    private fun invitationDialog(){
        alert("Send invitation") {
            title = "Invitation"
            yesButton { sendMessage() }
            noButton { }
        }.show()

    }

    override fun renderFailure(@StringRes message: Int) {
        hideProgress()
        notify(message)
    }

}