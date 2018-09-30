package com.globalhiddenodds.tasos.di.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.globalhiddenodds.tasos.presentation.presenter.CreateUserDBViewModel
import com.globalhiddenodds.tasos.presentation.presenter.CreateUserViewModel
import com.globalhiddenodds.tasos.presentation.presenter.SearchContactViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    internal abstract fun bindViewModelFactory(factory:
                                                   ViewModelFactory):
            ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(CreateUserViewModel::class)
    abstract fun bindsCreateUserViewModel(createUserViewModel:
                                              CreateUserViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CreateUserDBViewModel::class)
    abstract fun bindsCreateUserDBViewModel(createUserDBViewModel:
                                              CreateUserDBViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SearchContactViewModel::class)
    abstract fun bindsSearchContactViewModel(searchContactViewModel:
                                                SearchContactViewModel): ViewModel
}