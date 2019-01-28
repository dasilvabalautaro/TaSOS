package com.globalhiddenodds.tasos

import android.app.Application
import android.arch.lifecycle.ProcessLifecycleOwner
import android.content.res.Configuration
import com.globalhiddenodds.tasos.di.ApplicationComponent
import com.globalhiddenodds.tasos.di.ApplicationModule
import com.globalhiddenodds.tasos.di.DaggerApplicationComponent
import com.globalhiddenodds.tasos.presentation.component.AppLifecycleObserver
import com.globalhiddenodds.tasos.tools.LocaleUtils
import com.squareup.leakcanary.LeakCanary
import java.util.*
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
    @Inject
    lateinit var localeUtils: LocaleUtils

    override fun onCreate() {
        super.onCreate()
        this.injectMembers()
        this.initializeLeakDetection()
        ProcessLifecycleOwner.get().lifecycle.addObserver(appLifecycleObserver)
        localeUtils.setLocale(Locale("es"))
        localeUtils.updateConfiguration(this,
                baseContext.resources.configuration)
    }

    private fun injectMembers() = component.inject(this)

    private fun initializeLeakDetection() {
        if (BuildConfig.DEBUG) LeakCanary.install(this)
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        localeUtils.updateConfiguration(this, newConfig!!)
    }

    fun getAppComponent(): ApplicationComponent{
        appComponent = component
        return appComponent
    }
}