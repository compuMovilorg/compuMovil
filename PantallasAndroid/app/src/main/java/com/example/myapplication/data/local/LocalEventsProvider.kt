package com.example.myapplication.data.local

import com.example.myapplication.R
import com.example.myapplication.data.EventInfo

object LocalEventsProvider {

    val events = listOf(
        EventInfo(
            date = "25 Ago",
            time = "10:00 PM",
            title = "Reggaeton Night - DJ Balvin",
            eventImage = R.drawable.eventimg1
        ),
        EventInfo(
            date = "26 Ago",
            time = "9:30 PM",
            title = "Electro Vibes - Martin Garrix Tribute",
            eventImage = R.drawable.eventimg2
        ),
        EventInfo(
            date = "30 Ago",
            time = "11:00 PM",
            title = "Salsa y Perreo Mix",
            eventImage = R.drawable.eventimg3
        ),
        EventInfo(
            date = "1 Sep",
            time = "8:00 PM",
            title = "Retro 2000’s Party",
            eventImage = R.drawable.eventimg4
        ),
        EventInfo(
            date = "5 Sep",
            time = "9:00 PM",
            title = "Full Moon Party - Techno Edition",
            eventImage = R.drawable.eventimg5
        )
    )
}
