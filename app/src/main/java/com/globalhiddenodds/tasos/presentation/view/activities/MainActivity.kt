package com.globalhiddenodds.tasos.presentation.view.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.globalhiddenodds.tasos.App
import com.globalhiddenodds.tasos.di.ApplicationComponent
import com.globalhiddenodds.tasos.models.persistent.files.ManageFiles
import com.globalhiddenodds.tasos.presentation.navigation.Navigator
import com.globalhiddenodds.tasos.tools.Constants
import javax.inject.Inject


class MainActivity : AppCompatActivity() {

    private val appComponent: ApplicationComponent by
    lazy(mode = LazyThreadSafetyMode.NONE) {
        (application as App).component
    }

    @Inject
    internal lateinit var navigator: Navigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        navigator.showMain(this, Constants.user)

    }

}
