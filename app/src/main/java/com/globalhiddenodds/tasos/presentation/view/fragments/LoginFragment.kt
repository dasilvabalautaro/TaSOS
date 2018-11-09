package com.globalhiddenodds.tasos.presentation.view.fragments

import android.os.Bundle
import android.support.annotation.StringRes
import android.view.View
import com.globalhiddenodds.tasos.R
import com.globalhiddenodds.tasos.extension.observe
import com.globalhiddenodds.tasos.extension.failure
import com.globalhiddenodds.tasos.extension.viewModel
import com.globalhiddenodds.tasos.models.persistent.PreferenceRepository
import com.globalhiddenodds.tasos.models.persistent.PreferenceRepository.set
import com.globalhiddenodds.tasos.presentation.navigation.Navigator
import com.globalhiddenodds.tasos.presentation.plataform.BaseFragment
import com.globalhiddenodds.tasos.presentation.presenter.CreateUserDBViewModel
import com.globalhiddenodds.tasos.presentation.presenter.CreateUserViewModel
import com.globalhiddenodds.tasos.tools.Constants
import com.globalhiddenodds.tasos.tools.Validate
import kotlinx.android.synthetic.main.view_login.*
import javax.inject.Inject

class LoginFragment: BaseFragment() {

    @Inject
    lateinit var navigator: Navigator
    private lateinit var createUserViewModel: CreateUserViewModel
    private lateinit var createUserDBViewModel: CreateUserDBViewModel

    override fun layoutId() = R.layout.view_login
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        createUserViewModel = viewModel(viewModelFactory) {
            observe(result, ::renderResult)
            failure(failure, ::handleFailure)
        }

        createUserDBViewModel = viewModel(viewModelFactory) {
            observe(result, ::resultCreateUserDb)
            failure(failure, ::handleFailure)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bt_send_sign_up.setOnClickListener {createUser()}

    }
    private fun createUser(){
        if (validatedInput(et_email.text.toString(), et_password.text.toString())){
            Constants.user.email = et_email.text.toString()
            Constants.user.password = et_password.text.toString()
            createUserViewModel.createUser()
        }else{
            notify(R.string.failure_input                                                                                                                                       )
        }
    }

    private fun renderResult(value: Boolean?){
        if (value != null && value){
            val splitEmail = Constants.user.email.split("@")
            val prefix = (1000..9000).shuffled().last()
            val id = splitEmail[0] + "-" + prefix.toString()
            Constants.user.id = id
            createUserDBViewModel.createUser()
        }
    }

    private fun resultCreateUserDb(value: Boolean?){
        if (value != null && value){
            val prefs = PreferenceRepository.customPrefs(activity!!,
                    Constants.preference_tasos)
            prefs[Constants.userId] = Constants.user.id
            navigator.showContact(activity!!)
        }

    }


    private fun validatedInput(email: String, password: String): Boolean{
        return Validate.isValidEmail(email) && password.length > 5
    }

    override fun renderFailure(@StringRes message: Int) {
        hideProgress()
        notifyWithAction(message, R.string.action_refresh)
    }
}