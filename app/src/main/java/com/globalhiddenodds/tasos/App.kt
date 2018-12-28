package com.globalhiddenodds.tasos

import android.app.Application
import android.arch.lifecycle.ProcessLifecycleOwner
import com.globalhiddenodds.tasos.di.ApplicationComponent
import com.globalhiddenodds.tasos.di.ApplicationModule
import com.globalhiddenodds.tasos.di.DaggerApplicationComponent
import com.globalhiddenodds.tasos.presentation.component.AppLifecycleObserver
import com.squareup.leakcanary.LeakCanary
import javax.inject.Inject

class App: Application() {

    companion object{
        lateinit var appComponent: ApplicationComponent
    }

    val component: ApplicationComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        DaggerApplicationComponent
                .builder()
                .applicationModule(ApplicationModule(this))
                .build()
    }

    @Inject
    lateinit var appLifecycleObserver: AppLifecycleObserver

    override fun onCreate() {
        super.onCreate()
        this.injectMembers()
        this.initializeLeakDetection()
        ProcessLifecycleOwner.get().lifecycle.addObserver(appLifecycleObserver)
    }

    private fun injectMembers() = component.inject(this)

    private fun initializeLeakDetection() {
        if (BuildConfig.DEBUG) LeakCanary.install(this)
    }

    fun getAppComponent(): ApplicationComponent{
        appComponent = component
        return appComponent
    }
}