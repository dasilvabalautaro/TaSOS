package com.globalhiddenodds.tasos.models.persistent.network.services

import com.globalhiddenodds.tasos.App
import com.globalhiddenodds.tasos.di.ApplicationComponent
import com.globalhiddenodds.tasos.models.persistent.network.RegisterTokenFCM
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import javax.inject.Inject

class TasosFirebaseMessagingService: FirebaseMessagingService() {

    private val appComponent: ApplicationComponent by
    lazy(mode = LazyThreadSafetyMode.NONE) {
        (application as App).appComponent
    }

    @Inject
    lateinit var registerTokenFCM: RegisterTokenFCM

    private var refreshToken: String? = null

    override fun onCreate() {
        super.onCreate()
        appComponent.inject(this)
    }

    override fun onNewToken(token: String?) {
        super.onNewToken(token)
        refreshToken = token
        if (this.refreshToken != null){
            registerTokenFCM.sendRegistrationTokenFCM(this.refreshToken!!)
            println("TOKEN FCM: ${this.refreshToken}")
        }

    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)

        remoteMessage?.notification?.let {
            println("Remote Notification: ${it.body}")
        }
    }

}