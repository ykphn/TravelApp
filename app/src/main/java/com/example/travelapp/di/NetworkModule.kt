package com.example.travelapp.di

import android.app.Application
import com.example.travelapp.R
import com.example.travelapp.data.location.api.LocationManager
import com.example.travelapp.data.location.repository.LocationManagerRepository
import com.example.travelapp.data.maps.MapsRepository
import com.example.travelapp.data.maps.MapsRepositoryImpl
import com.example.travelapp.data.remote.api.OverpassApi
import com.example.travelapp.data.remote.query.OverpassBuildQuery
import com.example.travelapp.data.remote.query.OverpassQueryProviderFactory
import com.example.travelapp.data.remote.repository.OverpassRepository
import com.example.travelapp.data.remote.repository.OverpassRepositoryImp
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
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

    @Provides
    @Singleton
    fun provideFusedLocationClient(context: Application): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context)
    }

    @Provides
    @Singleton
    fun provideLocationManagerRepository(
        context: Application,
        fusedLocationClient: FusedLocationProviderClient
    ): LocationManager {
        return LocationManagerRepository(context, fusedLocationClient)
    }

    @Provides
    @Singleton
    fun provideOverpassBuildQuery(): OverpassBuildQuery {
        return OverpassBuildQuery()
    }

    @Provides
    fun provideOverpassQueryProviderFactory(
        overpassBuildQuery: OverpassBuildQuery
    ): OverpassQueryProviderFactory {
        return OverpassQueryProviderFactory(overpassBuildQuery)
    }

    @Provides
    @Singleton
    fun providePlacesClient(context: Application): PlacesClient {
        Places.initializeWithNewPlacesApiEnabled(
            context,
            context.getString(R.string.google_maps_key)
        )
        return Places.createClient(context)
    }

    @Provides
    @Singleton
    fun provideMapsRepository(placesClient: PlacesClient): MapsRepository {
        return MapsRepositoryImpl(placesClient)
    }

}