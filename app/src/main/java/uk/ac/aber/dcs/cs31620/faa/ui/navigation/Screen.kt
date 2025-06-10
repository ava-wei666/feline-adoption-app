package uk.ac.aber.dcs.cs31620.faa.ui.navigation

sealed class Screen (
    val route: String
) {
    data object Home : Screen("home")
    data object Cats : Screen("cats")
}