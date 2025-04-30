package uk.ac.aber.dcs.cs31620.faa.ui.components

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TopLevelScaffold(){
    Scaffold(
        topBar = {
            MainPageTopAppBar()
        },
        bottomBar = {
            MainPageNavigationBar()
        },
        content = {  }
    )
}