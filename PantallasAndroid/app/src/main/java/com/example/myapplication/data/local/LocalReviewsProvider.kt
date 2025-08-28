package com.example.myapplication.data.local

import com.example.myapplication.R
import com.example.myapplication.data.ReviewInfo

object LocalReviewsProvider {
    val Reviews = listOf(
        ReviewInfo(
            id = 1,
            userImage = R.drawable.usr1,
            placeImage = LocalGastroBarProvider.gastroBars.first { it.id == 1 }.imagePlace,
            name = "Ana Torres",
            placeName = LocalGastroBarProvider.gastroBars.first { it.id == 1 }.name,
            reviewText = "Excelente ambiente nocturno, cócteles creativos y buena música en vivo.",
            likes = 150,
            comments = 38,
            gastroBarId = 1
        ),
        ReviewInfo(
            id = 2,
            userImage = R.drawable.usr1,
            placeImage = LocalGastroBarProvider.gastroBars.first { it.id == 2 }.imagePlace,
            name = "Carlos Perez",
            placeName = LocalGastroBarProvider.gastroBars.first { it.id == 2 }.name,
            reviewText = "La decoración es increíble y las tapas son de otro nivel. Ideal para salir con amigos.",
            likes = 198,
            comments = 54,
            gastroBarId = 2
        ),
        ReviewInfo(
            id = 3,
            userImage = R.drawable.usr1,
            placeImage = LocalGastroBarProvider.gastroBars.first { it.id == 3 }.imagePlace,
            name = "Laura Gómez",
            placeName = LocalGastroBarProvider.gastroBars.first { it.id == 3 }.name,
            reviewText = "Amplia variedad de cervezas artesanales y un menú que combina sabores únicos.",
            likes = 175,
            comments = 42,
            gastroBarId = 3
        ),
        ReviewInfo(
            id = 4,
            userImage = R.drawable.usr1,
            placeImage = LocalGastroBarProvider.gastroBars.first { it.id == 4 }.imagePlace,
            name = "Luis Hernández",
            placeName = LocalGastroBarProvider.gastroBars.first { it.id == 4 }.name,
            reviewText = "La pista de baile es enorme y el DJ mantiene la energía toda la noche.",
            likes = 320,
            comments = 89,
            gastroBarId = 4
        ),
        ReviewInfo(
            id = 5,
            userImage = R.drawable.usr1,
            placeImage = LocalGastroBarProvider.gastroBars.first { it.id == 5 }.imagePlace,
            name = "Sofía Vargas",
            placeName = LocalGastroBarProvider.gastroBars.first { it.id == 5 }.name,
            reviewText = "Ambiente elegante, cocteles premium y atención de primera.",
            likes = 210,
            comments = 60,
            gastroBarId = 5
        ),
        ReviewInfo(
            id = 6,
            userImage = R.drawable.usr1,
            placeImage = LocalGastroBarProvider.gastroBars.first { it.id == 6 }.imagePlace,
            name = "Javier Moreno",
            placeName = LocalGastroBarProvider.gastroBars.first { it.id == 6 }.name,
            reviewText = "Luces espectaculares, sonido envolvente y un público que nunca para de bailar.",
            likes = 285,
            comments = 77,
            gastroBarId = 6
        )
    )
}
