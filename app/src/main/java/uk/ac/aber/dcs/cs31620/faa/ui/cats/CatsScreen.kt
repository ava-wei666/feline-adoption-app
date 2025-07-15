package uk.ac.aber.dcs.cs31620.faa.ui.cats

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import uk.ac.aber.dcs.cs31620.faa.R
import uk.ac.aber.dcs.cs31620.faa.ui.components.CatCard
import uk.ac.aber.dcs.cs31620.faa.ui.components.DefaultSnackbar
import uk.ac.aber.dcs.cs31620.faa.ui.components.SearchArea
import uk.ac.aber.dcs.cs31620.faa.ui.components.TopLevelScaffold
import uk.ac.aber.dcs.cs31620.faa.ui.theme.FAATheme

/**
 * Represents the cats screen. For this version we only have a
 * top app bar, navigation bar, search area and a list of cats.
 * @author Chris Loftus
 */
@Composable
fun CatsScreen(
    navController: NavHostController
) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    TopLevelScaffold(
        navController = navController,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            message = "Add cat",
                            actionLabel = "Undo"
                        )
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription =
                        stringResource(R.string.add_cat)
                )
            }
        },
        snackbarContent = { data ->
            DefaultSnackbar(
                data = data,
                modifier = Modifier.padding(bottom = 4.dp),
                onDismiss = { data.dismiss() }
            )
        },
        coroutineScope = coroutineScope,
        snackbarHostState = snackbarHostState
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

            val context = LocalContext.current

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

           /* LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 4.dp, bottom = 4.dp)
            ) {

                items(cats) {
                    CatCard(
                        cat = it,
                        modifier = Modifier
                            .padding(end = 4.dp, top = 4.dp),
                        selectAction = {cat ->
                            Toast.makeText(context, "Selected ${cat.name}",
                                Toast.LENGTH_LONG).show()
                        },
                        deleteAction = {cat ->
                            Toast.makeText(context, "Delete ${cat.name}",
                                Toast.LENGTH_LONG).show()
                        }
                    )
                }
            }*/

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