package com.globalhiddenodds.tasos.presentation.view.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.globalhiddenodds.tasos.R
import com.globalhiddenodds.tasos.extension.failure
import com.globalhiddenodds.tasos.extension.observe
import com.globalhiddenodds.tasos.extension.viewModel
import com.globalhiddenodds.tasos.presentation.plataform.BaseActivity
import com.globalhiddenodds.tasos.presentation.presenter.SearchContactViewModel
import com.globalhiddenodds.tasos.presentation.view.fragments.ContactFragment
import kotlinx.android.synthetic.main.toolbar.*


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
                fragment().searchContact(this.et_search.text.toString())
            }

        }
        return super.onOptionsItemSelected(item)
    }
}