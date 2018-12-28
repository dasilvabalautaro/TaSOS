package com.globalhiddenodds.tasos.presentation.view.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.globalhiddenodds.tasos.App
import com.globalhiddenodds.tasos.R
import com.globalhiddenodds.tasos.di.ApplicationComponent
import com.globalhiddenodds.tasos.extension.viewModel
import com.globalhiddenodds.tasos.models.persistent.network.services.ManagerServices
import com.globalhiddenodds.tasos.presentation.plataform.BaseActivity
import com.globalhiddenodds.tasos.presentation.presenter.DeleteUserViewModel
import com.globalhiddenodds.tasos.presentation.view.fragments.ContactFragment
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

class ContactActivity: BaseActivity() {
    private val appComponent: ApplicationComponent by
    lazy(mode = LazyThreadSafetyMode.NONE) {
        (application as App).component
    }


    companion object {

        fun callingIntent(context: Context) = Intent(context,
                ContactActivity::class.java)

    }

    @Inject
    lateinit var managerServices: ManagerServices

    override fun fragment() = ContactFragment()

/*
    var serviceMessageIntent: Intent? = null
    var hearMessageService: HearMessageService? = null
    var isBound = false
*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        this.et_search.visibility = View.VISIBLE
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        managerServices.start()

    }

/*
    override fun onStart() {
        super.onStart()
        bindService(serviceMessageIntent, serviceConnection, Context.BIND_AUTO_CREATE)
    }
*/

  /*  private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName,
                                        service: IBinder) {
            val binder = service as
                    HearMessageService.LocalBinder
            hearMessageService = binder.getService()
            isBound = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            isBound = false
        }
    }*/

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        menu!!.findItem(R.id.action_video).isVisible = false
        menu.findItem(R.id.action_call).isVisible = false
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



}