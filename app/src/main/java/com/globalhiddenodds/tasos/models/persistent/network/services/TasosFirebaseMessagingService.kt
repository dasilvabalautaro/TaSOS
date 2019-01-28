package com.globalhiddenodds.tasos.models.persistent.network.services

import android.media.MediaPlayer
import android.support.design.widget.Snackbar
import android.util.ArrayMap
import android.view.Gravity
import android.widget.Toast
import com.globalhiddenodds.tasos.App
import com.globalhiddenodds.tasos.R
import com.globalhiddenodds.tasos.di.ApplicationComponent
import com.globalhiddenodds.tasos.models.persistent.network.RegisterTokenFCM
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.jetbrains.anko.runOnUiThread
import java.lang.Exception
import javax.inject.Inject

class TasosFirebaseMessagingService: FirebaseMessagingService() {

    private val appComponent: ApplicationComponent by
    lazy(mode = LazyThreadSafetyMode.NONE) {
        (application as App).component
    }

    @Inject
    lateinit var registerTokenFCM: RegisterTokenFCM

    private var refreshToken: String? = null
    private val audio = "Audio"
    private val video = "Video"

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
        println("LLEGUE MESSAGE" )

        when {
            remoteMessage!!.data.isNotEmpty() -> {
                val map = remoteMessage.data

                if (map != null){
                    var type = ""
                    var message = ""
                    for ((k, v) in map) {
                        when (k) {
                            "type" -> if(v != null) type = v as String
                        }
                    }

                    if (type.isNotEmpty() && type == video){
                        message = getString(R.string.lbl_video_foreground)
                    }
                    if (type.isNotEmpty() && type == audio){
                        message = getString(R.string.lbl_audio_foreground)
                    }
                    runOnUiThread {

                        val toast = Toast.makeText(appComponent.context(), message,
                                Toast.LENGTH_LONG)
                        toast.setGravity(Gravity.CENTER, 0, 0 )
                        toast.show()


                    }


                }
            }
        }

        remoteMessage?.notification?.let {

            runOnUiThread {
                playSound()
            }

            println("Remote Notification: ${it.body}")
        }
    }

    override fun onDeletedMessages() {
        super.onDeletedMessages()
        println("Delete Message")

    }

    private fun playSound(){
        val mediaPlayer: MediaPlayer? = MediaPlayer.create(appComponent.context(),
                R.raw.google_video)
        mediaPlayer?.start()
    }
}