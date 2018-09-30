package com.globalhiddenodds.tasos.di

import android.content.Context
import com.globalhiddenodds.tasos.App
import com.globalhiddenodds.tasos.models.interfaces.HandleMessage
import com.globalhiddenodds.tasos.models.interfaces.RepositoryNetwork
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(private val app: App) {
    @Provides
    @Singleton
    fun provideApplicationContext(): Context = app
    @Provides
    @Singleton
    fun provideRepositoryNetwork(dataSource:
                                 RepositoryNetwork.Network):
            RepositoryNetwork = dataSource
    @Provides
    @Singleton
    fun provideHandleMessage(messageSource: HandleMessage.Message):
            HandleMessage = messageSource

}