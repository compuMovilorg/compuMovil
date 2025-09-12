package com.example.myapplication.data.repository

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.datasource.AuthRemoteDataSource
import com.example.myapplication.data.datasource.StorageRemoteDataSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.StorageException
import javax.inject.Inject



class StorageRepository @Inject constructor(
    private val storage: StorageRemoteDataSource,
    private val auth: AuthRemoteDataSource
): ViewModel(){
    suspend fun uploadProfileImg(uri: Uri): Result<String> {
        return try {
            val userId = auth.currentUser?.uid
                ?: return Result.failure(Exception("Ningún usuario logeado"))

            val path = "profileImages/$userId.jpg"
            val url = storage.uploadImage(path, uri)

            auth.updateProfileImage(url)
            Result.success(url)
        } catch (e: Exception) {
            Log.d("Error_app", e.toString())
            val message = when (e) {
                is StorageException -> when (e.errorCode) {
                    StorageException.ERROR_OBJECT_NOT_FOUND -> "El archivo no existe en el servidor."
                    StorageException.ERROR_NOT_AUTHENTICATED -> "Debes iniciar sesión para subir imágenes."
                    StorageException.ERROR_NOT_AUTHORIZED -> "No tienes permisos para esta acción."
                    StorageException.ERROR_QUOTA_EXCEEDED -> "Se alcanzó el límite de almacenamiento."
                    else -> "Error de almacenamiento: ${e.message}"
                }
                else -> e.message ?: "Error desconocido al subir la imagen."
            }
            Result.failure(Exception(message))
        }
    }
}
