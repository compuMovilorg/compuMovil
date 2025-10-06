package com.example.myapplication.data.injection

import com.example.myapplication.data.EventInfo
import com.example.myapplication.data.GastroBar
import com.example.myapplication.data.datasource.services.GastroBarRetrofitService
import com.example.myapplication.data.datasource.services.ReviewRetrofitService
import com.example.myapplication.data.datasource.services.UserRetrofitService
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
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun providesReviewRetrofitService(retrofit: Retrofit): ReviewRetrofitService{
        return retrofit.create(ReviewRetrofitService::class.java)
    }

    @Provides
    @Singleton
    fun provideGastroBarRetrofitService(retrofit: Retrofit): GastroBarRetrofitService {
        return retrofit.create(GastroBarRetrofitService::class.java)
    }

    @Provides
    @Singleton
    fun provideUserRetrofitService(retrofit: Retrofit): UserRetrofitService {
        return retrofit.create(UserRetrofitService::class.java)
    }

    @Provides
    fun provideEvents(): List<EventInfo> = LocalEventsProvider.events
}
