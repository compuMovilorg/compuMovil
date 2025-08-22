package com.example.myapplication.data.local

import com.example.myapplication.R
import com.example.myapplication.data.ReviewInfo

object LocalReviewsProvider {

    val Reviews = listOf(
        ReviewInfo(
            userImage = R.drawable.usr1,
            placeImage = R.drawable.gastrobarimg1,
            name = "Ana Torres",
            placeName = "Gastrobar La Esquina",
            reviewText = "Excelente ambiente nocturno, cócteles creativos y buena música en vivo.",
            likes = 150,
            comments = 38
        ),
        ReviewInfo(
            userImage = R.drawable.usr1,
            placeImage = R.drawable.gastrobarimg1,
            name = "Carlos Perez",
            placeName = "Bistro & Bar Luna Roja",
            reviewText = "La decoración es increíble y las tapas son de otro nivel. Ideal para salir con amigos.",
            likes = 198,
            comments = 54
        ),
        ReviewInfo(
            userImage = R.drawable.usr1,
            placeImage = R.drawable.gastrobarimg1,
            name = "Laura Gómez",
            placeName = "El Rincón del Sabor",
            reviewText = "Amplia variedad de cervezas artesanales y un menú que combina sabores únicos.",
            likes = 175,
            comments = 42
        ),
        ReviewInfo(
            userImage = R.drawable.usr1,
            placeImage = R.drawable.gastrobarimg1,
            name = "Luis Hernández",
            placeName = "Discoteca Eclipse",
            reviewText = "La pista de baile es enorme y el DJ mantiene la energía toda la noche.",
            likes = 320,
            comments = 89
        ),
        ReviewInfo(
            userImage = R.drawable.usr1,
            placeImage = R.drawable.gastrobarimg1,
            name = "Sofía Vargas",
            placeName = "Bar Nocturno Black Moon",
            reviewText = "Ambiente elegante, cocteles premium y atención de primera.",
            likes = 210,
            comments = 60
        ),
        ReviewInfo(
            userImage = R.drawable.usr1,
            placeImage = R.drawable.gastrobarimg1,
            name = "Javier Moreno",
            placeName = "Club Neon",
            reviewText = "Luces espectaculares, sonido envolvente y un público que nunca para de bailar.",
            likes = 285,
            comments = 77
        )
    )

}