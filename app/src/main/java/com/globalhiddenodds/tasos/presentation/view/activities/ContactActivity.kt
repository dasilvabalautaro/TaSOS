package com.globalhiddenodds.tasos.presentation.view.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.globalhiddenodds.tasos.R
import com.globalhiddenodds.tasos.presentation.plataform.BaseActivity
import com.globalhiddenodds.tasos.presentation.view.fragments.ContactFragment
import kotlinx.android.synthetic.main.toolbar.*

class ContactActivity: BaseActivity() {
    companion object {
        fun callingIntent(context: Context) = Intent(context,
                ContactActivity::class.java)
    }

    override fun fragment() = ContactFragment()

/*
    var serviceMessageIntent: Intent? = null
    var hearMessageService: HearMessageService? = null
    var isBound = false
*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.et_search.visibility = View.VISIBLE
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)

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

    override fun onDestroy() {
      /*  if (isBound){
            unbindService(serviceConnection)
            isBound = false
        }*/
        println("OnDestroy Contact Activity")
        super.onDestroy()
    }

}