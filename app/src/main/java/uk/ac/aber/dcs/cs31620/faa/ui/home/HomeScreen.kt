package uk.ac.aber.dcs.cs31620.faa.ui.home

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import uk.ac.aber.dcs.cs31620.faa.ui.components.MainPageTopAppBar
import uk.ac.aber.dcs.cs31620.faa.ui.theme.FAATheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier
){
    Scaffold(
        topBar = {
            MainPageTopAppBar()
        },
        content = {  }
    )
}

@Composable
@Preview
fun HomeScreenPreview(){
    FAATheme(dynamicColor = false) {
        HomeScreen()
    }
}