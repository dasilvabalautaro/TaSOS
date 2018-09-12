package com.globalhiddenodds.tasos.di

import com.globalhiddenodds.tasos.App
import com.globalhiddenodds.tasos.presentation.view.activities.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {
    fun inject(app: App)
    fun inject(mainActivity: MainActivity)
}