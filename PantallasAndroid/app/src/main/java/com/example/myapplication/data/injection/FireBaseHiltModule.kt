package com.example.myapplication.data.injection

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.InstallIn


@Module
@InstallIn(SingletonComponent::class)
class FireBaseHiltModule {

    @Provides
    fun auth(): FirebaseAuth = Firebase.auth

    @Provides
    fun storage(): FirebaseStorage = Firebase.storage
}