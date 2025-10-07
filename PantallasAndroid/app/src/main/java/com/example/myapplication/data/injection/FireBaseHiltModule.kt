package com.example.myapplication.data.injection

import com.example.myapplication.data.auth.CurrentUserProvider
import com.example.myapplication.data.auth.FirebaseCurrentUserProvider
import com.example.myapplication.data.repository.AuthRepository
import com.example.myapplication.data.repository.UserRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FireBaseHiltModule {

    @Provides fun auth(): FirebaseAuth = Firebase.auth
    @Provides fun storage(): FirebaseStorage = Firebase.storage

    @Provides
    @Singleton
    fun firestore(): FirebaseFirestore = Firebase.firestore


    // 🔹 Provee el CurrentUserProvider ya cableado
    @Provides
    @Singleton
    fun provideCurrentUserProvider(
        authRepository: AuthRepository,
        userRepository: UserRepository
    ): CurrentUserProvider = FirebaseCurrentUserProvider(authRepository, userRepository)
}
