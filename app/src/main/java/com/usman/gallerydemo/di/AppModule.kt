package com.usman.gallerydemo.di

import android.app.Application
import android.content.Context
import com.usman.gallerydemo.data.DataManager
import com.usman.gallerydemo.data.DataManagerImpl
import com.usman.gallerydemo.utils.ResourceProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application
    }

    @Provides
    fun provideResourceProvider(context: Context): ResourceProvider {
        return ResourceProvider(context.applicationContext)
    }

    @Singleton
    @Provides
    fun provideDataManager(dataManagerImpl: DataManagerImpl): DataManager {
        return dataManagerImpl
    }

}