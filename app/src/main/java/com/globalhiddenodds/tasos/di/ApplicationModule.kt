package com.globalhiddenodds.tasos.di

import android.arch.persistence.room.Room
import android.content.Context
import com.globalhiddenodds.tasos.App
import com.globalhiddenodds.tasos.models.persistent.network.interfaces.ManagerUserCloud
import com.globalhiddenodds.tasos.models.persistent.network.interfaces.RepositoryNetwork
import com.globalhiddenodds.tasos.models.persistent.database.AppDatabase
import com.globalhiddenodds.tasos.models.persistent.network.interfaces.HandleSendMessage
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(private val app: App) {
    private val databaseName = "tasosdb"

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
    fun provideManagerUserCloud(managerUserCloud: ManagerUserCloud.ManagerUser):
            ManagerUserCloud = managerUserCloud

    @Provides
    @Singleton
    fun provideHandleSendMessage(handleSendMessage: HandleSendMessage.SendMessage):
            HandleSendMessage = handleSendMessage

    @Provides
    @Singleton
    fun provideAppDatabase(context: Context): AppDatabase =
            Room.databaseBuilder(context,
                    AppDatabase::class.java,
                    databaseName).allowMainThreadQueries().build()

    @Provides
    @Singleton
    fun provideMessageDao(database: AppDatabase) = database.messageDao()

}