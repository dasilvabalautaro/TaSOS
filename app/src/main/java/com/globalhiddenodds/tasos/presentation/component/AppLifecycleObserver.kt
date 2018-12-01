package com.globalhiddenodds.tasos.presentation.component

import android.app.ActivityManager
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.globalhiddenodds.tasos.R
import com.globalhiddenodds.tasos.models.persistent.PreferenceRepository
import com.globalhiddenodds.tasos.models.persistent.PreferenceRepository.set
import com.globalhiddenodds.tasos.models.persistent.network.services.HearMessageService
import com.globalhiddenodds.tasos.tools.Constants
import javax.inject.Inject

class AppLifecycleObserver @Inject constructor(private val context: Context) :
        LifecycleObserver {

    private val enterForegroundToast =
            Toast.makeText(context,
                    context.getString(R.string.foreground_message),
                    Toast.LENGTH_SHORT)

    private val enterBackgroundToast =
            Toast.makeText(context, context.getString(R.string.background_message),
                    Toast.LENGTH_SHORT)


    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onEnterForeground() {
        val prefs = PreferenceRepository.customPrefs(context,
                Constants.preference_tasos)
        prefs[Constants.run] = "1"
        enterForegroundToast.showAfterCanceling(enterBackgroundToast)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onEnterBackground() {
        val prefs = PreferenceRepository.customPrefs(context,
                Constants.preference_tasos)
        prefs[Constants.run] = "0"
        enterBackgroundToast.showAfterCanceling(enterForegroundToast)
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onEnterCreate(){
        val serviceMessageIntent = Intent(context, HearMessageService::class.java)
        val prefs = PreferenceRepository.customPrefs(context,
                Constants.preference_tasos)
        Constants.user.id = prefs.getString(Constants.userId, "")

        if (!checkServiceRunning()){
            println("Ingrese Start Services")
            context.startService(serviceMessageIntent)
        }
    }

    @Suppress("DEPRECATION")
    private fun checkServiceRunning(): Boolean {
        val manager = context.getSystemService(Context
                .ACTIVITY_SERVICE) as ActivityManager
        manager.getRunningServices(Integer.MAX_VALUE).forEach { service ->
            if ("com.globalhiddenodds.tasos.models.persistent.network.services.HearMessageService" == service.service.className) {
                return true
            }
        }
        return false
    }

    private fun Toast.showAfterCanceling(toastToCancel: Toast) {
        toastToCancel.cancel()
        this.show()
    }
}