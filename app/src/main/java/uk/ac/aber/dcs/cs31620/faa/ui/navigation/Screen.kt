package uk.ac.aber.dcs.cs31620.faa.ui.navigation

sealed class Screen (
    val route: String
) {
    data object Home : Screen("home")
    data object Cats : Screen("cats")
    data object Login : Screen("login")
    data object SignUp : Screen("signup")
    data object AddCat : Screen("addCat")
    data object Fosterers : Screen("fosterers")
    data object AdopterProfile : Screen("adopter_profile")

    data object CatDetails : Screen("cats/{catId}") {
        fun createRoute(catId: Int) = "cats/$catId"
    }

    data object FostererCats : Screen("fosterer_cats/{fostererId}") {
        fun createRoute(fostererId: Long) = "fosterer_cats/$fostererId"
    }

    data object FostererProfile : Screen("fosterer/{fostererId}") {
        fun createRoute(fostererId: Long) = "fosterer/$fostererId"
    }
}