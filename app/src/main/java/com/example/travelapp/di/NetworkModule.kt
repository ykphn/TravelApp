package com.example.travelapp.di

import com.example.travelapp.data.remote.api.OverpassApi
import com.example.travelapp.data.remote.repository.OverpassRepository
import com.example.travelapp.data.remote.repository.OverpassRepositoryImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOverpass(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://overpass-api.de/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideOverpassService(retrofit: Retrofit): OverpassApi {
        return retrofit.create(OverpassApi::class.java)
    }

    @Provides
    @Singleton
    fun provideOverpassRepository(api: OverpassApi): OverpassRepository {
        return OverpassRepositoryImp(api)
    }
}