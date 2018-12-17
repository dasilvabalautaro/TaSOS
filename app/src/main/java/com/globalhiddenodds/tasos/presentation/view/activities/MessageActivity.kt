package com.globalhiddenodds.tasos.presentation.view.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.globalhiddenodds.tasos.App
import com.globalhiddenodds.tasos.R
import com.globalhiddenodds.tasos.di.ApplicationComponent
import com.globalhiddenodds.tasos.models.persistent.network.FirebaseDbToRoom
import com.globalhiddenodds.tasos.presentation.data.GroupMessageView
import com.globalhiddenodds.tasos.presentation.navigation.Navigator
import com.globalhiddenodds.tasos.presentation.plataform.BaseActivity
import com.globalhiddenodds.tasos.presentation.view.fragments.MessageFragment
import com.globalhiddenodds.tasos.tools.Constants
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

class MessageActivity: BaseActivity() {

    companion object {
        private const val INTENT_EXTRA_PARAM_GROUP = "com.globalhiddenodds.INTENT_PARAM_GROUP"

        fun callingIntent(context: Context, group: GroupMessageView): Intent {
            val intent = Intent(context, MessageActivity::class.java)
            intent.putExtra(INTENT_EXTRA_PARAM_GROUP, group)
            return intent
        }

    }
    private val appComponent: ApplicationComponent by
    lazy(mode = LazyThreadSafetyMode.NONE) {
        (application as App).appComponent
    }

    @Inject
    lateinit var navigator: Navigator


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
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item!!.itemId

        if (id == R.id.action_video){
            navigator.showWeb(this, Constants.user.id,
                    tv_name.text.toString())

        }

        return super.onOptionsItemSelected(item)

    }
    override fun fragment() = MessageFragment
            .forGroup(intent.getParcelableExtra(INTENT_EXTRA_PARAM_GROUP))

   /* private inline fun <reified T : Activity> Activity.navigate() {
        val intent = Intent(this@MessageActivity, T::class.java)
        startActivity(intent.apply {
            putExtra("source", Constants.user.id)
            putExtra("target", tv_name.text.toString())
        })
    }*/

}