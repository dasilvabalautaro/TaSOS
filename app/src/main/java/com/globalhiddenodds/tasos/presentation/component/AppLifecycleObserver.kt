package com.globalhiddenodds.tasos.presentation.component

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import android.widget.Toast
import com.globalhiddenodds.tasos.R
import com.globalhiddenodds.tasos.models.persistent.PreferenceRepository
import com.globalhiddenodds.tasos.models.persistent.PreferenceRepository.set
import com.globalhiddenodds.tasos.models.persistent.network.ListenFCM
import com.globalhiddenodds.tasos.models.persistent.network.services.ManagerServices
import com.globalhiddenodds.tasos.tools.Constants
import javax.inject.Inject

class AppLifecycleObserver @Inject constructor(private val context: Context,
                                               private val managerServices: ManagerServices) :
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

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onEnterDestroy(){
        managerServices.stop()

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onEnterCreate(){
        managerServices.start()
    }

    private fun Toast.showAfterCanceling(toastToCancel: Toast) {
        toastToCancel.cancel()
        this.show()
    }
}