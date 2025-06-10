package uk.ac.aber.dcs.cs31620.faa.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TopLevelScaffold(
    navController: NavController,
    pageContent:
    @Composable (innerPadding: PaddingValues) -> Unit = {}
){
    Scaffold(
        topBar = {
            MainPageTopAppBar()
        },
        bottomBar = {
            MainPageNavigationBar(navController)
        },
        content = { innerPadding -> pageContent(innerPadding) }
    )
}