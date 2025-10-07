package com.example.myapplication.data.local

import com.example.myapplication.data.ReviewInfo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalReviewsProvider @Inject constructor() {
    val reviews: List<ReviewInfo> = listOf(
        ReviewInfo(
            id = 1,
            userId = 1, // 👈 agregado
            userImage = "https://firebasestorage.googleapis.com/v0/b/nocta-95b8c.firebasestorage.app/o/usr1.png?alt=media&token=a28c0778-494b-4e4c-8378-64d33450fe9d",
            placeImage = LocalGastroBarProvider.gastroBars.first { it.id == 1 }.imagePlace,
            name = "Ana Torres",
            placeName = LocalGastroBarProvider.gastroBars.first { it.id == 1 }.name,
            reviewText = "Excelente ambiente nocturno, cócteles creativos y buena música en vivo.",
            likes = 150,
            comments = 38,
        ),
        ReviewInfo(
            id = 2,
            userId = 2,
            userImage = "https://firebasestorage.googleapis.com/v0/b/nocta-95b8c.firebasestorage.app/o/usr1.png?alt=media&token=a28c0778-494b-4e4c-8378-64d33450fe9d",
            placeImage = LocalGastroBarProvider.gastroBars.first { it.id == 2 }.imagePlace,
            name = "Carlos Perez",
            placeName = LocalGastroBarProvider.gastroBars.first { it.id == 2 }.name,
            reviewText = "La decoración es increíble y las tapas son de otro nivel. Ideal para salir con amigos.",
            likes = 198,
            comments = 54,
        ),
        ReviewInfo(
            id = 3,
            userId = 3,
            userImage = "https://firebasestorage.googleapis.com/v0/b/nocta-95b8c.firebasestorage.app/o/usr1.png?alt=media&token=a28c0778-494b-4e4c-8378-64d33450fe9d",
            placeImage = LocalGastroBarProvider.gastroBars.first { it.id == 3 }.imagePlace,
            name = "Laura Gómez",
            placeName = LocalGastroBarProvider.gastroBars.first { it.id == 3 }.name,
            reviewText = "Amplia variedad de cervezas artesanales y un menú que combina sabores únicos.",
            likes = 175,
            comments = 42,
        ),
        ReviewInfo(
            id = 4,
            userId = 4,
            userImage = "https://firebasestorage.googleapis.com/v0/b/nocta-95b8c.firebasestorage.app/o/usr1.png?alt=media&token=a28c0778-494b-4e4c-8378-64d33450fe9d",
            placeImage = LocalGastroBarProvider.gastroBars.first { it.id == 4 }.imagePlace,
            name = "Luis Hernández",
            placeName = LocalGastroBarProvider.gastroBars.first { it.id == 4 }.name,
            reviewText = "La pista de baile es enorme y el DJ mantiene la energía toda la noche.",
            likes = 320,
            comments = 89,
        ),
        ReviewInfo(
            id = 5,
            userId = 4,
            userImage = "https://firebasestorage.googleapis.com/v0/b/nocta-95b8c.firebasestorage.app/o/usr1.png?alt=media&token=a28c0778-494b-4e4c-8378-64d33450fe9d",
            placeImage = LocalGastroBarProvider.gastroBars.first { it.id == 5 }.imagePlace,
            name = "Sofía Vargas",
            placeName = LocalGastroBarProvider.gastroBars.first { it.id == 5 }.name,
            reviewText = "Ambiente elegante, cocteles premium y atención de primera.",
            likes = 210,
            comments = 60,
        ),
        ReviewInfo(
            id = 6,
            userId = 2,
            userImage = "https://firebasestorage.googleapis.com/v0/b/nocta-95b8c.firebasestorage.app/o/usr1.png?alt=media&token=a28c0778-494b-4e4c-8378-64d33450fe9d",
            placeImage = LocalGastroBarProvider.gastroBars.first { it.id == 6 }.imagePlace,
            name = "Javier Moreno",
            placeName = LocalGastroBarProvider.gastroBars.first { it.id == 6 }.name,
            reviewText = "Luces espectaculares, sonido envolvente y un público que nunca para de bailar.",
            likes = 285,
            comments = 77,
        )
    )
}
