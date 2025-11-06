package com.example.myapplication

import android.app.Application
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.app
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)
        val opts = Firebase.app.options
        android.util.Log.d("FB", "projectId=${opts.projectId}, appId=${opts.applicationId}, bucket=${opts.storageBucket}")
        /*if(BuildConfig.DEBUG){
            Firebase.firestore.useEmulator("10.0.2.2", 8080)
            Firebase.auth.useEmulator("10.0.2.2", 9099)
        }*/

    }
}