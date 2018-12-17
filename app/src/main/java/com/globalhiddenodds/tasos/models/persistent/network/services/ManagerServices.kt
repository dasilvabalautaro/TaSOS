package com.globalhiddenodds.tasos.models.persistent.network.services

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import com.globalhiddenodds.tasos.models.persistent.PreferenceRepository
import com.globalhiddenodds.tasos.tools.Constants
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ManagerServices @Inject constructor(private val context: Context) {

    private val serviceMessageIntent = Intent(context, HearMessageService::class.java)


    fun start() {
        val prefs = PreferenceRepository.customPrefs(context,
                Constants.preference_tasos)
        if (Constants.user.id.isEmpty()){
            Constants.user.id = prefs.getString(Constants.userId, "")
        }

        if (!checkServiceRunning() && Constants.user.id.isNotEmpty()){
            context.startService(serviceMessageIntent)
        }

    }

    fun stop(){
        context.stopService(serviceMessageIntent)
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

}