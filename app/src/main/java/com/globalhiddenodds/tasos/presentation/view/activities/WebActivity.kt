package com.globalhiddenodds.tasos.presentation.view.activities

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import com.globalhiddenodds.tasos.App
import com.globalhiddenodds.tasos.di.ApplicationComponent
import com.globalhiddenodds.tasos.models.persistent.network.FirebaseDbToRoom
import com.globalhiddenodds.tasos.models.persistent.network.ListenFCM
import com.globalhiddenodds.tasos.presentation.plataform.BaseActivity
import com.globalhiddenodds.tasos.presentation.view.fragments.WebFragment
import com.globalhiddenodds.tasos.tools.Constants
import javax.inject.Inject

class WebActivity: BaseActivity() {
    companion object {
        const val source = "source"
        const val target = "target"

        fun callingIntent(context: Context, source:String,
                          target: String): Intent {
            val intent = Intent(context, WebActivity::class.java)
            intent.putExtra(WebActivity.source, source)
            intent.putExtra(WebActivity.target, target)
            return intent
        }
    }
    private val functionFCM = "sendMessage"
    private val fieldData = "data"

    private val appComponent: ApplicationComponent by
    lazy(mode = LazyThreadSafetyMode.NONE) {
        (application as App).appComponent
    }

    @Inject
    lateinit var firebaseDbToRoom: FirebaseDbToRoom

    @Inject
    lateinit var listenFCM: ListenFCM

    override fun fragment() = WebFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        listenFCM.start()
        val src = intent.getStringExtra(source)
        val tgt = intent.getStringExtra(target)

        val address = Constants.web_rtc + "?" + "source=" + src +
                "&target=" + tgt

        listenFCM.listenFromFCM(Constants.user.id,
                Constants.user.token, functionFCM)
        openWebPage(address)

    }

    private fun openWebPage(url: String) {
        val webPage = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, webPage)
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        firebaseDbToRoom.startFirebase()
        firebaseDbToRoom.clearValueFieldFirebase(fieldData)
    }

   /* override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemUI()
    }*/

    private fun hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    // Shows the system bars by removing all the flags
    // except for the ones that make the content appear under the system bars.
    private fun showSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }


}