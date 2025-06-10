package uk.ac.aber.dcs.cs31620.faa.ui.cats

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import uk.ac.aber.dcs.cs31620.faa.ui.components.TopLevelScaffold
import uk.ac.aber.dcs.cs31620.faa.ui.theme.FAATheme
/**
 * Represents the cats screen. For this version we only have a
 * top app bar, navigation bar and empty content.
 * @author Chris Loftus
 */
@Composable
fun CatsScreen(
    navController: NavHostController
) {
    TopLevelScaffold(
        navController = navController
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Text(
                text = "Cats Screen",
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

@Composable
@Preview
fun CatsScreenPreview(){
    FAATheme(dynamicColor = false) {
        val navController = rememberNavController()
        CatsScreen(navController = navController)
    }
}