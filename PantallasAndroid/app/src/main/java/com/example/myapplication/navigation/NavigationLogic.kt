package com.example.myapplication.navigation

object NavigationLogic {
    private val showTopBarScreens = listOf(
        Screen.Home.route,
       /// Screen.Search.route,
        Screen.Create.route,
        Screen.Profile.route,
        Screen.EditProfile.route,
        Screen.SettingsRoute.route,
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
        Screen.EditProfile.route,
        Screen.Notification.route,
        Screen.SettingsRoute.route,

    )

    fun shouldShowBottomBar(route: String?) =showBottomBarScreens.contains(route)
    fun shouldShowTopBar(route: String?) = showTopBarScreens.contains(route)



}