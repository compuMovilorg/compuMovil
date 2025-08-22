package com.example.myapplication.navigation

object NavigationLogic {
    private val noBottomBarScreens = listOf(
        Screen.Start.route,
        Screen.Login.route,
        Screen.Register.route,
    )

    private val showBottomBarScreens = listOf(
        Screen.Home.route,
        //Screen.Search.route,
        Screen.Profile.route,

    )

    fun shouldShowBottomBar(route: String?) =showBottomBarScreens.contains(route)


}