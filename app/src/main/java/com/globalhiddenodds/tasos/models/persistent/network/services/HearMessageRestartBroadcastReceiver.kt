package com.globalhiddenodds.tasos.models.persistent.network.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class HearMessageRestartBroadcastReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        context!!.startService(Intent(context, HearMessageService::class.java))
        //bindService
    }

}