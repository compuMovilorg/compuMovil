package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.data.dtos.UserFirestoreDto
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreTestActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Firestore", "== Iniciando pruebas de DTOs ==")

        // Dejar descomentado el metodo que quieran probar, todos a la vez pueden crashear lae appp
        testUserFirestoreDto()

        // testRegisterUserDto()
        // testCreateGastroBarDto()
        // testCreateReviewDto()
        // testReviewDto()
        // testUserProfileDto()
    }

    // === USER FIRESTORE DTO ===
    private fun testUserFirestoreDto() {
        val user = UserFirestoreDto(
            id = "1",
            username = "palmxl",
            email = "palmxl@example.com",
            password = "12345",
            name = "Juan Martín",
            birthdate = "2003-03-10",
            followersCount = 10,
            followingCount = 5,
            profileImage = null
        )

        db.collection("users").add(user)
            .addOnSuccessListener { Log.d("Firestore", "UserFirestoreDto agregado") }
            .addOnFailureListener { e -> Log.e("Firestore", " Error UserFirestoreDto", e) }
    }

    // === REGISTER USER DTO ===
    /*
    private fun testRegisterUserDto() {
        val newUser = RegisterUserDto(
            name = "Camila Torres",
            username = "cami_t",
            birthdate = "2001-12-05"
        )

        db.collection("registeredUsers").add(newUser)
            .addOnSuccessListener { Log.d("Firestore", " RegisterUserDto agregado") }
            .addOnFailureListener { e -> Log.e("Firestore", " Error RegisterUserDto", e) }
    }
    */

    // === CREATE GASTROBAR DTO ===
    /*
    private fun testCreateGastroBarDto() {
        val bar = CreateGastroBarDto(
            imagePlace = "https://example.com/bar.jpg",
            name = "Nocta Bar",
            rating = 4.7f,
            reviewCount = 12,
            address = "Calle 127 #12-45, Bogotá",
            hours = "6:00 PM - 2:00 AM",
            cuisine = "Cócteles y tapas",
            description = "Ambiente elegante con buena música",
            reviewId = null
        )

        db.collection("gastroBars").add(bar)
            .addOnSuccessListener { Log.d("Firestore", " CreateGastroBarDto agregado") }
            .addOnFailureListener { e -> Log.e("Firestore", " Error CreateGastroBarDto", e) }
    }
    */

    // === CREATE REVIEW DTO ===
    /*
    private fun testCreateReviewDto() {
        val review = CreateReviewDto(
            userId = 1,
            placeName = "Nocta Bar",
            reviewText = "Excelente lugar ",
            parentReviewId = null
        )

        db.collection("reviews").add(review)
            .addOnSuccessListener { Log.d("Firestore", " CreateReviewDto agregado") }
            .addOnFailureListener { e -> Log.e("Firestore", " Error CreateReviewDto", e) }
    }
    */

    // === REVIEW DTO ===
    /*
    private fun testReviewDto() {
        val userProfile = userProfileDto(
            id = "u1",
            username = "palmxl",
            name = "Juan Martín",
            profileImage = null
        )

        val gastroBar = GastroBarDto(
            id = "b1",
            imagePlace = null,
            name = "Nocta Bar",
            rating = 4.8f,
            reviewCount = 15,
            address = "Bogotá",
            hours = "6 PM - 2 AM",
            cuisine = "Cócteles",
            description = "Ambiente nocturno",
            reviewId = null
        )

        val review = ReviewDto(
            id = "r1",
            userId = "u1",
            placeName = "Nocta Bar",
            imagePlace = null,
            reviewText = "Muy buen sitio, volvería.",
            likes = 10,
            comments = 3,
            parentReviewId = null,
            createdAt = "2025-10-15",
            updatedAt = "2025-10-15",
            user = userProfile,
            gastroBar = gastroBar
        )

        db.collection("reviews").add(review)
            .addOnSuccessListener { Log.d("Firestore", " ReviewDto agregado") }
            .addOnFailureListener { e -> Log.e("Firestore", " Error ReviewDto", e) }
    }
    */

    // === USER PROFILE DTO ===
    /*
    private fun testUserProfileDto() {
        val profile = userProfileDto(
            id = "u1",
            username = "palmxl",
            name = "Juan Martín",
            profileImage = "https://example.com/palmxl.png"
        )

        db.collection("profiles").add(profile)
            .addOnSuccessListener { Log.d("Firestore", " UserProfileDto agregado") }
            .addOnFailureListener { e -> Log.e("Firestore", " Error UserProfileDto", e) }
    }
    */
}