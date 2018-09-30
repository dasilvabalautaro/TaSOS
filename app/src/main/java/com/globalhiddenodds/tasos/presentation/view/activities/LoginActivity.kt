package com.globalhiddenodds.tasos.presentation.view.activities

import android.content.Context
import android.content.Intent
import com.globalhiddenodds.tasos.presentation.plataform.BaseActivity
import com.globalhiddenodds.tasos.presentation.view.fragments.LoginFragment


class LoginActivity: BaseActivity() {

    companion object {
        fun callingIntent(context: Context) = Intent(context,
                LoginActivity::class.java)
    }


    override fun fragment() = LoginFragment()
}