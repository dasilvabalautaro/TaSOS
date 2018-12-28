package com.globalhiddenodds.tasos.presentation.view.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.globalhiddenodds.tasos.App
import com.globalhiddenodds.tasos.di.ApplicationComponent
import com.globalhiddenodds.tasos.presentation.plataform.BaseActivity
import com.globalhiddenodds.tasos.presentation.view.fragments.LoginFragment
import com.globalhiddenodds.tasos.tools.EnablePermissions
import javax.inject.Inject


class LoginActivity: BaseActivity() {

    private val appComponent: ApplicationComponent by
    lazy(mode = LazyThreadSafetyMode.NONE) {
        (application as App).component
    }
    @Inject
    lateinit var enablePermissions: EnablePermissions

    companion object {
        fun callingIntent(context: Context) = Intent(context,
                LoginActivity::class.java)
    }


    override fun fragment() = LoginFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
    }

    override fun onResume() {
        super.onResume()
        if (!enablePermissions.permissionAllNeed(this))
            println("Solicitude Permission")

    }
}