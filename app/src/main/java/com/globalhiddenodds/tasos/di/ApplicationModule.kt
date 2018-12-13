package com.globalhiddenodds.tasos.di

import android.arch.persistence.room.Room
import android.content.Context
import android.provider.SyncStateContract
import com.globalhiddenodds.tasos.App
import com.globalhiddenodds.tasos.BuildConfig
import com.globalhiddenodds.tasos.models.persistent.network.interfaces.ManagerUserCloud
import com.globalhiddenodds.tasos.models.persistent.network.interfaces.RepositoryNetwork
import com.globalhiddenodds.tasos.models.persistent.database.AppDatabase
import com.globalhiddenodds.tasos.models.persistent.network.interfaces.HandleSendMessage
import com.globalhiddenodds.tasos.tools.Constants
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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

    @Provides @Singleton fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
                .baseUrl(Constants.web_rtc)
                .client(createClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    private fun createClient(): OkHttpClient {
        val okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)
            okHttpClientBuilder.addInterceptor(loggingInterceptor)
        }
        return okHttpClientBuilder.build()
    }

}