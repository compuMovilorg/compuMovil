package com.example.myapplication.data.datasource.impl.firestore

import android.util.Log
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
        val snap = db.collection(collectionName).get().await()

        Log.d("GastroBarFS", "getAll size=${snap.size()}")
        snap.documents.forEach { d ->
            Log.v("GastroBarFS", "docId=${d.id} data=${d.data}")
        }

        return snap.documents.mapNotNull { doc ->
            val dto = doc.toObject(GastroBarDto::class.java) ?: return@mapNotNull null
            // IMPORTANTE: inyectar el document.id en el DTO
            dto.copy(id = doc.id)
        }
    }

    override suspend fun getGastroBarById(id: String): GastroBarDto {
        val doc = db.collection(collectionName).document(id).get().await()
        if (!doc.exists()) throw NoSuchElementException("GastroBar con id $id no encontrado")

        val dto = doc.toObject(GastroBarDto::class.java)
            ?: throw IllegalStateException("No se pudo convertir el documento a GastroBarDto")

        // IMPORTANTE: retornar el DTO con id
        return dto.copy(id = doc.id)
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
