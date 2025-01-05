package com.example.vknewsclient.di

import android.app.Application
import androidx.room.Room
import com.example.vknewsclient.data.NewsFeedRepositoryImpl
import com.example.vknewsclient.data.database.VkDatabase
import com.example.vknewsclient.data.network.ApiFactory
import com.example.vknewsclient.data.network.ApiService
import com.example.vknewsclient.domain.repository.NewsFeedRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainModule {

    @Provides
    @Singleton
    fun provideVkDatabase(application: Application): VkDatabase {
        return Room.databaseBuilder(
            application,
            VkDatabase::class.java,
            "VkNewsDatabase"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideNewsFeedRepository(impl: NewsFeedRepositoryImpl) : NewsFeedRepository {
        return impl
    }

    @Provides
    @Singleton
    fun provideApiService() : ApiService {
        return ApiFactory.apiService
    }
}