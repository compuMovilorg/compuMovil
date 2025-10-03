package com.example.myapplication.data.injection

import com.example.myapplication.data.EventInfo
import com.example.myapplication.data.GastroBar
import com.example.myapplication.data.local.LocalEventsProvider
import com.example.myapplication.data.local.LocalGastroBarProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
    }
    @Provides
    fun provideEvents(): List<EventInfo> = LocalEventsProvider.events
}
