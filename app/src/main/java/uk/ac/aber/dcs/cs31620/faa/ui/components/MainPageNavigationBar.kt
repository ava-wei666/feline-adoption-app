package uk.ac.aber.dcs.cs31620.faa.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Pets
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import uk.ac.aber.dcs.cs31620.faa.R
import uk.ac.aber.dcs.cs31620.faa.ui.navigation.Screen

/**
 * Updated Navigation Bar with 3 items: Home, Cats, and Fosterers.
 */
@Composable
fun MainPageNavigationBar(
    navController: NavController
) {
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        //home button
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = if (currentRoute == Screen.Home.route) Icons.Filled.Home else Icons.Outlined.Home,
                    contentDescription = stringResource(id = R.string.home)
                )
            },
            label = { Text(stringResource(id = R.string.home)) },
            selected = currentRoute == Screen.Home.route,
            onClick = { doNavigate(Screen.Home.route, navController) }
        )

        // fosterers button
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = if (currentRoute == Screen.Fosterers.route) Icons.Filled.Face else Icons.Outlined.Face,
                    contentDescription = stringResource(id = R.string.fosterers)
                )
            },
            label = { Text(stringResource(id = R.string.fosterers)) },
            selected = currentRoute == Screen.Fosterers.route,
            onClick = { doNavigate(Screen.Fosterers.route, navController) }
        )

        // Cats button
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = if (currentRoute == Screen.Cats.route) Icons.Filled.Pets else Icons.Outlined.Pets,
                    contentDescription = stringResource(id = R.string.cats)
                )
            },
            label = { Text(stringResource(id = R.string.cats)) },
            selected = currentRoute == Screen.Cats.route,
            onClick = { doNavigate(Screen.Cats.route, navController) }
        )
    }
}

private fun doNavigate(
    route: String,
    navController: NavController
){
    navController.navigate(route) {
        popUpTo(navController.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}