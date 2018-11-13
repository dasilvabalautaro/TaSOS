package com.globalhiddenodds.tasos.presentation.view.activities

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.globalhiddenodds.tasos.R
import com.globalhiddenodds.tasos.models.persistent.PreferenceRepository
import com.globalhiddenodds.tasos.models.persistent.network.services.HearMessageService
import com.globalhiddenodds.tasos.presentation.plataform.BaseActivity
import com.globalhiddenodds.tasos.presentation.view.fragments.ContactFragment
import com.globalhiddenodds.tasos.tools.Constants
import kotlinx.android.synthetic.main.toolbar.*

@Suppress("DEPRECATION")
class ContactActivity: BaseActivity() {
    companion object {
        fun callingIntent(context: Context) = Intent(context,
                ContactActivity::class.java)
    }

    override fun fragment() = ContactFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.et_search.visibility = View.VISIBLE
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)

        val prefs = PreferenceRepository.customPrefs(this,
                Constants.preference_tasos)
        Constants.user.id = prefs.getString(Constants.userId, "")

        if (!checkServiceRunning()){
            val intent = Intent(this, HearMessageService::class.java)
            startService(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item!!.itemId
        if (id == R.id.action_config){

        }
        if (id == R.id.action_search){
            if (!this.et_search.text.toString().isEmpty()){

                supportFragmentManager.fragments.iterator().forEach { fr ->
                    (fr as? ContactFragment)?.searchContact(this
                            .et_search.text.toString())
                }

            }

        }
        return super.onOptionsItemSelected(item)
    }

    private fun checkServiceRunning(): Boolean {
        val manager = getSystemService(Context
                .ACTIVITY_SERVICE) as ActivityManager
        manager.getRunningServices(Integer.MAX_VALUE).forEach { service ->
            if ("com.globalhiddenodds.tasos.models.persistent.network.services.HearMessageService" == service.service.className) {
                return true
            }
        }
        return false
    }
}