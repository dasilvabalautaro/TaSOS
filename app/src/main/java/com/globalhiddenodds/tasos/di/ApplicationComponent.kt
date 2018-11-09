package com.globalhiddenodds.tasos.di

import android.content.Context
import com.globalhiddenodds.tasos.App
import com.globalhiddenodds.tasos.di.viewmodel.ViewModelModule
import com.globalhiddenodds.tasos.models.persistent.network.services.HearMessageService
import com.globalhiddenodds.tasos.presentation.view.activities.MainActivity
import com.globalhiddenodds.tasos.presentation.view.fragments.ContactFragment
import com.globalhiddenodds.tasos.presentation.view.fragments.LoginFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, ViewModelModule::class])
interface ApplicationComponent {
    fun inject(app: App)
    fun context(): Context
    fun inject(mainActivity: MainActivity)
    fun inject(loginFragment: LoginFragment)
    fun inject(contactFragment: ContactFragment)
    fun inject(hearMessageService: HearMessageService)
}