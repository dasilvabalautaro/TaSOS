package com.globalhiddenodds.tasos.presentation.navigation

import android.content.Context
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.FragmentActivity
import android.view.View
import android.widget.TextView
import com.globalhiddenodds.tasos.models.data.User
import com.globalhiddenodds.tasos.models.persistent.network.interfaces.RepositoryNetwork
import com.globalhiddenodds.tasos.presentation.data.GroupMessageView
import com.globalhiddenodds.tasos.presentation.view.activities.LoginActivity
import com.globalhiddenodds.tasos.presentation.view.activities.ContactActivity
import com.globalhiddenodds.tasos.presentation.view.activities.MessageActivity
import com.globalhiddenodds.tasos.presentation.view.activities.WebActivity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Navigator @Inject constructor(private val repositoryNetwork: RepositoryNetwork) {
    private fun showLogin(context: Context) = context
            .startActivity(LoginActivity.callingIntent(context))

    fun showContact(context: Context) = context
            .startActivity(ContactActivity.callingIntent(context))

    fun showWeb(context: Context, source: String, target: String,
                address: String, type: String) = context
            .startActivity(WebActivity.callingIntent(context, source,
                    target, address, type))

    fun showMain(context: Context, user: User) {
        when (repositoryNetwork.getCurrentUser(user)) {
            true -> showContact(context)
            false -> showLogin(context)
        }
    }

    fun showMessages(activity: FragmentActivity,
                     group: GroupMessageView, navigationExtras: Extras){
        val intent = MessageActivity.callingIntent(activity, group)
        val sharedView = navigationExtras
                .transitionSharedElement as TextView
        val activityOptions = ActivityOptionsCompat
                .makeSceneTransitionAnimation(activity,
                        sharedView, sharedView.transitionName)
        activity.startActivity(intent, activityOptions.toBundle())

    }

    class Extras(val transitionSharedElement: View)
}