package uk.ac.aber.dcs.cs31620.faa

import androidx.navigation.NavType
import androidx.navigation.navArgument
import uk.ac.aber.dcs.cs31620.faa.ui.cats.CatDetailsScreenTopLevel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import uk.ac.aber.dcs.cs31620.faa.model.CatsViewModel
import uk.ac.aber.dcs.cs31620.faa.ui.authentication.LoginScreen
import uk.ac.aber.dcs.cs31620.faa.ui.cats.AddCatScreenTopLevel
import uk.ac.aber.dcs.cs31620.faa.ui.cats.CatsScreen
import uk.ac.aber.dcs.cs31620.faa.ui.cats.CatsScreenTopLevel
import uk.ac.aber.dcs.cs31620.faa.ui.theme.FAATheme
import uk.ac.aber.dcs.cs31620.faa.ui.home.HomeScreen
import uk.ac.aber.dcs.cs31620.faa.ui.home.HomeScreenTopLevel
import uk.ac.aber.dcs.cs31620.faa.ui.navigation.Screen
/**
 * Starting activity class. Entry point for the app.
 * @author Chris Loftus
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FAATheme(dynamicColor = false) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    BuildNavigationGraph(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
private fun BuildNavigationGraph(
    modifier: Modifier = Modifier,
    catsViewModel: CatsViewModel = viewModel()
) {
    val navController = rememberNavController()
    var startDestination = remember { Screen.Home.route }

    val ctx = LocalActivity.current
    val viewCatsAction = stringResource(R.string.action_view_cats)
    val catsUri = stringResource(R.string.cats_uri)

    ctx?.intent?.let {
        if (it.action != null && it.action == viewCatsAction) {
            if (it.data != null && it.data.toString() == catsUri) {
                startDestination = Screen.Cats.route
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Home.route) {
            HomeScreenTopLevel(navController, catsViewModel)
        }
        composable(Screen.Cats.route) {
            CatsScreenTopLevel(navController, catsViewModel)
        }
        composable(Screen.Login.route) {
            LoginScreen(navController, modifier)
        }
        composable(Screen.AddCat.route) {
            AddCatScreenTopLevel(navController)
        }
        composable(
            route = Screen.CatDetails.route,
            arguments = listOf(navArgument("catId") { type = NavType.IntType })
        ) { backStackEntry ->
            val catId = backStackEntry.arguments?.getInt("catId") ?: 0

            CatDetailsScreenTopLevel(navController = navController, catId = catId)
        }
    }
}