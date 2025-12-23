package uk.ac.aber.dcs.cs31620.faa.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import uk.ac.aber.dcs.cs31620.faa.model.AdopterViewModel

/**
 * Creates the page scaffold to contain top app bar, navigation drawer,
 * bottom navigation button and of course the page content.
 * @param navController To pass through the NavHostController since navigation is required
 * @param pageContent So that callers can plug in their own page content.
 * By default an empty lambda.
 * @author Chris Loftus
 */
@Composable
fun TopLevelScaffold(
    navController: NavController,
    adopterViewModel: AdopterViewModel = viewModel(),
    floatingActionButton: @Composable () -> Unit = { },
    snackbarContent: @Composable (SnackbarData) -> Unit = {},
    coroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState? = null,
    pageContent:
    @Composable (innerPadding: PaddingValues) -> Unit = {}
) {
    val drawerState =
        rememberDrawerState(initialValue = DrawerValue.Closed)
    //val coroutineScope = rememberCoroutineScope()

    MainPageNavigationDrawer(
        navController = navController,
        drawerState = drawerState,
        closeDrawer = {
            coroutineScope.launch {
                drawerState.close()
            }
        }
    ) {

        Scaffold(
            topBar = {
                MainPageTopAppBar(onClick = {
                    coroutineScope.launch {
                        if (drawerState.isOpen) {
                            drawerState.close()
                        } else {
                            drawerState.open()
                        }
                    }
                },
                    adopterViewModel = adopterViewModel)
            },
            floatingActionButton = floatingActionButton,
            snackbarHost = {
                snackbarHostState?.let {
                    SnackbarHost(hostState = snackbarHostState) { data ->
                        snackbarContent(data)
                    }
                }
            },
            bottomBar = {
                MainPageNavigationBar(navController)
            },
            content = { innerPadding -> pageContent(innerPadding) }
        )
    }
}