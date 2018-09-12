package com.globalhiddenodds.tasos.di

import android.content.Context
import com.globalhiddenodds.tasos.App
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(private val app: App) {
    @Provides
    @Singleton
    fun provideApplicationContext(): Context = app
}