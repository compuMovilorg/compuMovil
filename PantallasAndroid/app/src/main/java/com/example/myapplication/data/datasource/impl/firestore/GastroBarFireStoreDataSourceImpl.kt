package com.example.myapplication.data.datasource.impl.firestore

import com.example.myapplication.data.datasource.GastroBarRemoteDataSource
import com.example.myapplication.data.dtos.CreateGastroBarDto
import com.example.myapplication.data.dtos.GastroBarDto
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GastroBarFireStoreDataSourceImpl @Inject constructor(
    private val db: FirebaseFirestore
) : GastroBarRemoteDataSource {

    private val collectionName = "gastroBars" // nombre de la colección en Firestore

    override suspend fun getAllGastroBares(): List<GastroBarDto> {
        return db.collection(collectionName)
            .get()
            .await()
            .toObjects(GastroBarDto::class.java)
    }

    override suspend fun getGastroBarById(id: String): GastroBarDto {
        val docSnapshot = db.collection(collectionName)
            .document(id)
            .get()
            .await()

        if (!docSnapshot.exists()) {
            throw NoSuchElementException("GastroBar con id $id no encontrado")
        }

        return docSnapshot.toObject(GastroBarDto::class.java)
            ?: throw IllegalStateException("No se pudo convertir el documento a GastroBarDto")
    }

    override suspend fun createGastroBar(gastrobar: CreateGastroBarDto) {
        // Si CreateGastroBarDto tiene un campo "id", lo usamos como id del documento
        val id = gastrobar.hashCode().toString() // o genera un UUID si lo prefieres
        db.collection(collectionName)
            .document(id)
            .set(gastrobar)
            .await()
    }

    override suspend fun updateGastroBar(id: String, gastrobar: CreateGastroBarDto) {
        val docRef = db.collection(collectionName).document(id)
        val docSnapshot = docRef.get().await()

        if (!docSnapshot.exists()) {
            throw NoSuchElementException("GastroBar con id $id no encontrado para update")
        }

        docRef.set(gastrobar).await()
    }

    override suspend fun deleteGastroBar(id: String) {
        val docRef = db.collection(collectionName).document(id)
        val docSnapshot = docRef.get().await()

        if (!docSnapshot.exists()) {
            throw NoSuchElementException("GastroBar con id $id no encontrado para delete")
        }

        docRef.delete().await()
    }
}
