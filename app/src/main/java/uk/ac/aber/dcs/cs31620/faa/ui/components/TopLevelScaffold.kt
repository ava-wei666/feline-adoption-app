package uk.ac.aber.dcs.cs31620.faa.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import uk.ac.aber.dcs.cs31620.faa.ui.theme.FAATheme

@Composable
fun TopLevelScaffold(
    navController: NavController,
    pageContent:
    @Composable (innerPadding: PaddingValues) -> Unit = {}
) {
    val drawerState =
        rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

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
                })
            },
            bottomBar = {
                MainPageNavigationBar(navController)
            },
            content = { innerPadding -> pageContent(innerPadding) }
        )
    }
}