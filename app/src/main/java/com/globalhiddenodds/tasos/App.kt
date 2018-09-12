package com.globalhiddenodds.tasos

import android.app.Application
import com.globalhiddenodds.tasos.di.ApplicationComponent
import com.globalhiddenodds.tasos.di.ApplicationModule
import com.globalhiddenodds.tasos.di.DaggerApplicationComponent
import com.squareup.leakcanary.LeakCanary

class App: Application() {
    val appComponent: ApplicationComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        DaggerApplicationComponent
                .builder()
                .applicationModule(ApplicationModule(this))
                .build()
    }

    override fun onCreate() {
        super.onCreate()
        this.injectMembers()
        this.initializeLeakDetection()
    }

    private fun injectMembers() = appComponent.inject(this)

    private fun initializeLeakDetection() {
        if (BuildConfig.DEBUG) LeakCanary.install(this)
    }

}