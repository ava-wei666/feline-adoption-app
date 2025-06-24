package uk.ac.aber.dcs.cs31620.faa.ui.cats

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import uk.ac.aber.dcs.cs31620.faa.R
import uk.ac.aber.dcs.cs31620.faa.ui.components.ButtonSpinner
import uk.ac.aber.dcs.cs31620.faa.ui.components.SearchArea
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
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {

            val breedList = stringArrayResource(id = R.array.breed_array).toList()
            val genderList = stringArrayResource(id = R.array.gender_array).toList()
            val ageList = stringArrayResource(id = R.array.age_range_array).toList()
            var selectedBreed by remember { mutableStateOf(breedList[0]) }
            var selectedGender by remember { mutableStateOf(genderList[0]) }
            var selectedAge by remember { mutableStateOf(ageList[0]) }
            var proximity by remember { mutableIntStateOf(10) }

            SearchArea(
                breedList = breedList,
                updateBreed = { selectedBreed = it },
                genderList = genderList,
                updateGender = { selectedGender = it },
                ageList = ageList,
                updateAge = { selectedAge = it },
                proximity = proximity,
                updateProximity = { proximity = it }
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