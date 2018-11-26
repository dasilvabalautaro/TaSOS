package com.globalhiddenodds.tasos.presentation.view.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.WindowManager
import com.globalhiddenodds.tasos.R
import com.globalhiddenodds.tasos.presentation.data.GroupMessageView
import com.globalhiddenodds.tasos.presentation.plataform.BaseActivity
import com.globalhiddenodds.tasos.presentation.view.fragments.MessageFragment
import kotlinx.android.synthetic.main.toolbar.*

class MessageActivity: BaseActivity() {

    companion object {
        private const val INTENT_EXTRA_PARAM_GROUP = "com.globalhiddenodds.INTENT_PARAM_GROUP"

        fun callingIntent(context: Context, group: GroupMessageView): Intent {
            val intent = Intent(context, MessageActivity::class.java)
            intent.putExtra(INTENT_EXTRA_PARAM_GROUP, group)
            return intent
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.tv_name.visibility = View.VISIBLE
        this.et_search.layoutParams.width = 0
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true

    }

    override fun fragment() = MessageFragment
            .forGroup(intent.getParcelableExtra(INTENT_EXTRA_PARAM_GROUP))
}