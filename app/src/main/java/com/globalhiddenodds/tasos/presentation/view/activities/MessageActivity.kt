package com.globalhiddenodds.tasos.presentation.view.activities

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.content.FileProvider
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.github.chrisbanes.photoview.PhotoView
import com.globalhiddenodds.tasos.App
import com.globalhiddenodds.tasos.R
import com.globalhiddenodds.tasos.di.ApplicationComponent
import com.globalhiddenodds.tasos.models.persistent.files.ManageFiles
import com.globalhiddenodds.tasos.presentation.component.MenuSheetDialog
import com.globalhiddenodds.tasos.presentation.data.GroupMessageView
import com.globalhiddenodds.tasos.presentation.data.MessageView
import com.globalhiddenodds.tasos.presentation.interfaces.OptionsMenuSheet
import com.globalhiddenodds.tasos.presentation.navigation.Navigator
import com.globalhiddenodds.tasos.presentation.plataform.BaseActivity
import com.globalhiddenodds.tasos.presentation.view.fragments.MessageFragment
import com.globalhiddenodds.tasos.tools.Constants
import com.globalhiddenodds.tasos.tools.EnablePermissions
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.view_row_message.view.*
import javax.inject.Inject

class MessageActivity: BaseActivity(), OptionsMenuSheet {

    override fun callExecuteOptions(task: Int) {
        when(task){
            1 -> {
                Toast.makeText(this, "Hi", Toast.LENGTH_LONG).show()
            }
            2 -> {

            }
        }
    }

    companion object {
        private const val INTENT_EXTRA_PARAM_GROUP = "com.globalhiddenodds.INTENT_PARAM_GROUP"

        fun callingIntent(context: Context, group: GroupMessageView): Intent {
            val intent = Intent(context, MessageActivity::class.java)
            intent.putExtra(INTENT_EXTRA_PARAM_GROUP, group)
            return intent
        }

    }

    var image: Bitmap? = null
    var observableImage: Subject<Bitmap> = PublishSubject.create()

    private val appComponent: ApplicationComponent by
    lazy(mode = LazyThreadSafetyMode.NONE) {
        (application as App).component
    }

    @Inject
    lateinit var navigator: Navigator
    @Inject
    lateinit var enablePermissions: EnablePermissions
    @Inject
    lateinit var manageFiles: ManageFiles

    private val audio = "Audio"
    private val video = "Video"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        this.tv_name.visibility = View.VISIBLE
        this.et_search.layoutParams.width = 0
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        //window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        menu!!.findItem(R.id.action_search).isVisible = false
        menu.findItem(R.id.action_video).isVisible = true
        menu.findItem(R.id.action_call).isVisible = true
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item!!.itemId

        if (id == R.id.action_video){
            val address = Constants.web_rtc
            navigator.showWeb(this, Constants.user.id,
                    tv_name.text.toString(), address, video)

        }

        if (id == R.id.action_call){
            val address = Constants.audio_rtc
            navigator.showWeb(this, Constants.user.id,
                    tv_name.text.toString(), address, audio)

        }

        if (id == R.id.action_config){
            val menuSheetDialog = MenuSheetDialog.newInstance()
            menuSheetDialog.show(supportFragmentManager, "Bottom Sheet")

        }

        return super.onOptionsItemSelected(item)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == enablePermissions.galleryImageRequest &&
                resultCode == Activity.RESULT_OK && data != null) {
            this.image = manageFiles.getBitmap(data.data)
            if (this.image != null){
                this.observableImage.onNext(this.image!!)
            }

        } else if (requestCode == enablePermissions.cameraImageRequest &&
                resultCode == Activity.RESULT_OK) {

            val photoUri = FileProvider.getUriForFile(this,
                    applicationContext.packageName +
                            ".provider", enablePermissions
                    .getCameraFile())
            this.image = manageFiles.getBitmap(photoUri)
            if (this.image != null){
                this.observableImage.onNext(this.image!!)
            }

        }

    }


    override fun fragment() = MessageFragment
            .forGroup(intent.getParcelableExtra(INTENT_EXTRA_PARAM_GROUP))

    fun gallery(){
        enablePermissions.startGalleryChooser(this)
    }

    fun camera(){

        enablePermissions.startCamera(this)
    }




    /* private inline fun <reified T : Activity> Activity.navigate() {
         val intent = Intent(this@MessageActivity, T::class.java)
         startActivity(intent.apply {
             putExtra("source", Constants.user.id)
             putExtra("target", tv_name.text.toString())
         })
     }*/

}