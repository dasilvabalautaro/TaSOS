package com.globalhiddenodds.tasos.presentation.navigation

import android.content.Context
import android.view.View
import com.globalhiddenodds.tasos.models.data.User
import com.globalhiddenodds.tasos.models.persistent.network.interfaces.RepositoryNetwork
import com.globalhiddenodds.tasos.presentation.view.activities.LoginActivity
import com.globalhiddenodds.tasos.presentation.view.activities.ContactActivity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Navigator @Inject constructor(private val repositoryNetwork: RepositoryNetwork) {
    private fun showLogin(context: Context) = context
            .startActivity(LoginActivity.callingIntent(context))

    fun showContact(context: Context) = context
            .startActivity(ContactActivity.callingIntent(context))

    fun showMain(context: Context, user: User) {
        when (repositoryNetwork.getCurrentUser(user)) {
            true -> showContact(context)
            false -> showLogin(context)
        }
    }

    class Extras(val transitionSharedElement: View)
}