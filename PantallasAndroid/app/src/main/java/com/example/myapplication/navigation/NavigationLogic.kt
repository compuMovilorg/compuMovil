package com.example.myapplication.navigation

object NavigationLogic {
    private val showTopBarScreens = listOf(
        Screen.Home.route,
       /// Screen.Search.route,
        Screen.BarReviews.route,
        Screen.Create.route,
        Screen.Profile.route,
        Screen.MainUser.route,
        Screen.User.route,
        Screen.EditProfile.route,
        Screen.SettingsRoute.route,
        Screen.Notification.route,
        Screen.Events.route
    )

    private val showBottomBarScreens = listOf(
        Screen.Home.route,
        Screen.Detail.route,
        Screen.BarReviews.route,
        Screen.Search.route,
        Screen.Create.route,
        Screen.Events.route,
        Screen.Profile.route,
        Screen.MainUser.route,
        Screen.EditProfile.route,
        Screen.Notification.route,
        Screen.SettingsRoute.route,
        Screen.User.route
    )

    fun shouldShowBottomBar(route: String?) =showBottomBarScreens.contains(route)
    fun shouldShowTopBar(route: String?) = showTopBarScreens.contains(route)



}