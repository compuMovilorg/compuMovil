package com.example.myapplication.navigation

object NavigationLogic {
    private val showTopBarScreens = listOf(
        Screen.Home.route,
       /// Screen.Search.route,
        Screen.Create.route,
        Screen.Profile.route,
        Screen.Settings.route,
        Screen.Notification.route,
        Screen.Events.route
    )

    private val showBottomBarScreens = listOf(
        Screen.Home.route,
        Screen.Detail.route,
        Screen.Search.route,
        Screen.Create.route,
        Screen.Events.route,
        Screen.Profile.route,
        Screen.Notification.route,
        Screen.Settings.route,

    )

    fun shouldShowBottomBar(route: String?) =showBottomBarScreens.contains(route)
    fun shouldShowTopBar(route: String?) = showTopBarScreens.contains(route)



}