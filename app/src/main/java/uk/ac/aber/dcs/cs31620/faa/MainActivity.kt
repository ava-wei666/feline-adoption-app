package uk.ac.aber.dcs.cs31620.faa

import android.os.Bundle
import androidx.activity.ComponentActivity
import uk.ac.aber.dcs.cs31620.faa.ui.navigation.Screen
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import uk.ac.aber.dcs.cs31620.faa.model.CatsViewModel
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import uk.ac.aber.dcs.cs31620.faa.ui.theme.FAATheme
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.*
import androidx.navigation.compose.*
import uk.ac.aber.dcs.cs31620.faa.model.AdopterViewModel
import uk.ac.aber.dcs.cs31620.faa.ui.authentication.LoginScreen
import uk.ac.aber.dcs.cs31620.faa.ui.cats.*
import uk.ac.aber.dcs.cs31620.faa.ui.fosterers.*
import uk.ac.aber.dcs.cs31620.faa.ui.home.HomeScreenTopLevel
import uk.ac.aber.dcs.cs31620.faa.ui.profile.AdopterProfileScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FAATheme(dynamicColor = false) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BuildNavigationGraph()
                }
            }
        }
    }
}

@Composable
private fun BuildNavigationGraph(
    modifier: Modifier = Modifier,
    catsViewModel: CatsViewModel = viewModel(),
    adopterViewModel: AdopterViewModel = viewModel()
) {
    val navController = rememberNavController()
    var startDestination = remember { Screen.Home.route }

    val ctx = LocalActivity.current
    val viewCatsAction = stringResource(R.string.action_view_cats)
    val catsUri = stringResource(R.string.cats_uri)

    ctx?.intent?.let { intent ->
        if (intent.action == viewCatsAction && intent.data?.toString() == catsUri) {
            startDestination = Screen.Cats.route
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Screen.Home.route) { HomeScreenTopLevel(navController, catsViewModel, adopterViewModel) }

        composable(Screen.Cats.route) { CatsScreenTopLevel(navController, catsViewModel, adopterViewModel) }

        composable(Screen.Login.route) { LoginScreen(navController, adopterViewModel) }

        composable(Screen.SignUp.route) { LoginScreen(navController, adopterViewModel, isSignUpMode = true) }

        composable(Screen.AddCat.route) { AddCatScreenTopLevel(navController) }

        composable(route = Screen.CatDetails.route, arguments = listOf(navArgument("catId") { type = NavType.IntType })) { backStackEntry ->
            val catId = backStackEntry.arguments?.getInt("catId") ?: 0
            CatDetailsScreenTopLevel(navController, catId, catsViewModel, adopterViewModel)
        }

        composable(Screen.Fosterers.route) { FosterersScreenTopLevel(navController = navController, adopterViewModel = adopterViewModel) }

        composable(route = Screen.FostererProfile.route, arguments = listOf(navArgument("fostererId") { type = NavType.LongType })) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("fostererId") ?: 0L
            FostererProfileScreen(navController, id)
        }

        composable(route = Screen.FostererCats.route, arguments = listOf(navArgument("fostererId") { type = NavType.LongType })) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("fostererId") ?: 0L
            FostererCatsScreen(navController, id)
        }

        composable(Screen.AdopterProfile.route) { AdopterProfileScreen(navController, adopterViewModel) }
    }
}