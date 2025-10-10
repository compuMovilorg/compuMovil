package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.myapplication.ui.theme.MyApplicationTheme
import dagger.hilt.android.AndroidEntryPoint
import android.util.Log
import androidx.compose.runtime.LaunchedEffect
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore


@AndroidEntryPoint

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                NoctaApp()

                // 🔥 Verificación Firestore
                LaunchedEffect(Unit) {
                    val db = Firebase.firestore
                    val testData = mapOf(
                        "status" to "ok",
                        "timestamp" to System.currentTimeMillis()
                    )

                    db.collection("testConnection").add(testData)
                        .addOnSuccessListener { ref ->
                            Log.d("FirestoreCheck", "✅ WRITE OK: ${ref.id}")
                        }
                        .addOnFailureListener { e ->
                            Log.e("FirestoreCheck", "❌ WRITE ERROR", e)
                        }

                    db.collection("testConnection").get()
                        .addOnSuccessListener { snap ->
                            Log.d("FirestoreCheck", "📦 READ OK: ${snap.size()} documentos")
                        }
                        .addOnFailureListener { e ->
                            Log.e("FirestoreCheck", "❌ READ ERROR", e)
                        }
                }
            }
        }

    }
}
