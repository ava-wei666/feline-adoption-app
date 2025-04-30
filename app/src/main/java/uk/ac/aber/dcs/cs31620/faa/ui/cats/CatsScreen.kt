package uk.ac.aber.dcs.cs31620.faa.ui.cats

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import uk.ac.aber.dcs.cs31620.faa.ui.components.TopLevelScaffold
import uk.ac.aber.dcs.cs31620.faa.ui.theme.FAATheme

@Composable
fun CatsScreen(
    modifier: Modifier = Modifier
){
    TopLevelScaffold()
}

@Composable
@Preview
fun HomeScreenPreview(){
    FAATheme(dynamicColor = false) {
        CatsScreen()
    }
}