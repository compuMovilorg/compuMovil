package com.example.myapplication.data.injection

import com.example.myapplication.data.EventInfo
import com.example.myapplication.data.GastroBar
import com.example.myapplication.data.local.LocalEventsProvider
import com.example.myapplication.data.local.LocalGastroBarProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGastroBars(): List<GastroBar> {
        return LocalGastroBarProvider.gastroBars
    }
    @Provides
    fun provideEvents(): List<EventInfo> = LocalEventsProvider.events
}
