package com.globalhiddenodds.tasos.presentation.view.fragments


import android.os.Bundle
import android.support.annotation.StringRes
import android.view.View
import com.globalhiddenodds.tasos.R
import com.globalhiddenodds.tasos.extension.observe
import com.globalhiddenodds.tasos.extension.failure
import com.globalhiddenodds.tasos.extension.viewModel
import kotlinx.android.synthetic.main.view_contacts.*
import com.globalhiddenodds.tasos.presentation.navigation.Navigator
import com.globalhiddenodds.tasos.presentation.plataform.BaseFragment
import com.globalhiddenodds.tasos.presentation.presenter.SearchContactViewModel
import javax.inject.Inject

class ContactFragment: BaseFragment() {
    @Inject
    lateinit var navigator: Navigator

    private lateinit var searchContactViewModel: SearchContactViewModel

    override fun layoutId() = R.layout.view_contacts
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        searchContactViewModel = viewModel(viewModelFactory) {
            observe(result, ::resultProcess)
            failure(failure, ::handleFailure)
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iv_logo.visibility = View.INVISIBLE
    }

    fun searchContact(search: String){

        searchContactViewModel.id = search
        searchContactViewModel.searchContact()

    }

    private fun resultProcess(value: Boolean?){
        if (value != null && value){
            context!!.toast(getString(R.string.msg_search_process))
        }
    }

    override fun renderFailure(@StringRes message: Int) {
        hideProgress()
        notifyWithAction(message, R.string.action_refresh)
    }

}