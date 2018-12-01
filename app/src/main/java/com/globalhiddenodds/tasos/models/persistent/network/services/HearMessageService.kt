package com.globalhiddenodds.tasos.models.persistent.network.services

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.globalhiddenodds.tasos.App
import com.globalhiddenodds.tasos.di.ApplicationComponent
import com.globalhiddenodds.tasos.models.persistent.network.FirebaseDbToRoom
import javax.inject.Inject

class HearMessageService : Service() {
    private var iBinder: IBinder = LocalBinder()

    private val appComponent: ApplicationComponent by
    lazy(mode = LazyThreadSafetyMode.NONE) {
        (application as App).appComponent
    }

    @Inject
    lateinit var firebaseDbToRoom: FirebaseDbToRoom


    inner class LocalBinder: Binder(){
        fun getService(): HearMessageService?{
            return this@HearMessageService
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return iBinder
    }

    override fun onCreate() {
        super.onCreate()
        appComponent.inject(this)
        firebaseDbToRoom.startFirebase()
        showLog("onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        showLog("onStartCommand")
        firebaseDbToRoom.listenerMessageEvent()
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        showLog("onDestroy Services")

    }

    private fun showLog(msg: String){
        println(msg)
    }
}
