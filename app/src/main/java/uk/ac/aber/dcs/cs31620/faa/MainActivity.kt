package uk.ac.aber.dcs.cs31620.faa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import uk.ac.aber.dcs.cs31620.faa.ui.authentication.LoginScreen
import uk.ac.aber.dcs.cs31620.faa.ui.cats.CatsScreen
import uk.ac.aber.dcs.cs31620.faa.ui.theme.FAATheme
import uk.ac.aber.dcs.cs31620.faa.ui.home.HomeScreen
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
private fun BuildNavigationGraph(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }
        composable(Screen.Cats.route) {
            CatsScreen(navController)
        }
        composable(Screen.Login.route) {
            LoginScreen(navController, modifier)
        }
    }
}