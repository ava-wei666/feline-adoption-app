package uk.ac.aber.dcs.cs31620.faa.ui.cats

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import uk.ac.aber.dcs.cs31620.faa.R
import uk.ac.aber.dcs.cs31620.faa.model.AdopterViewModel
import uk.ac.aber.dcs.cs31620.faa.model.Cat
import uk.ac.aber.dcs.cs31620.faa.model.CatSearch
import uk.ac.aber.dcs.cs31620.faa.model.CatsViewModel
import uk.ac.aber.dcs.cs31620.faa.ui.components.CatCard
import uk.ac.aber.dcs.cs31620.faa.ui.components.DefaultSnackbar
import uk.ac.aber.dcs.cs31620.faa.ui.components.SearchArea
import uk.ac.aber.dcs.cs31620.faa.ui.components.TopLevelScaffold
import uk.ac.aber.dcs.cs31620.faa.ui.navigation.Screen

@Composable
fun CatsScreenTopLevel(
    navController: NavHostController,
    catsViewModel: CatsViewModel = viewModel(),
    adopterViewModel: AdopterViewModel
){
    val catList by catsViewModel.catList.observeAsState(listOf())
    val searchDistance by catsViewModel.searchDistance.observeAsState(50f)
    val currentUser by adopterViewModel.user.observeAsState()

    LaunchedEffect(currentUser) {
        catsViewModel.updateUserLocation(currentUser)
    }

    CatsScreen(
        catsList = catList,
        catSearch = catsViewModel.catSearch,
        isLoggedIn = currentUser != null,
        currentDistance = searchDistance,
        onSearchChanged = { catsViewModel.updateCatSearch(it) },
        onDistanceChange = { catsViewModel.updateDistance(it) },
        navController = navController
    )
}

@Composable
fun CatsScreen(
    catsList: List<Cat> = listOf(),
    catSearch: CatSearch = CatSearch(),
    isLoggedIn: Boolean = false,
    currentDistance: Float = 50f,
    onSearchChanged: (CatSearch) -> Unit = {},
    onDistanceChange: (Float) -> Unit = {},
    navController: NavHostController
) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    TopLevelScaffold(
        navController = navController,
        coroutineScope = coroutineScope,
        snackbarHostState = snackbarHostState,
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate(Screen.AddCat.route) }) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = stringResource(R.string.add_cat))
            }
        },
        snackbarContent = { data ->
            DefaultSnackbar(
                data = data,
                modifier = Modifier.padding(bottom = 4.dp),
                onDismiss = { data.dismiss() }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding).fillMaxSize()
        ) {
            val breedList = stringArrayResource(id = R.array.breed_array).toList()
            val genderList = stringArrayResource(id = R.array.gender_array).toList()
            val ageList = stringArrayResource(id = R.array.age_range_array).toList()
            val regionList = listOf("Any region", "Aberystwyth", "London", "Birmingham", "Manchester", "Liverpool", "Glasgow", "Other")

            SearchArea(
                catSearch = catSearch,
                breedList = breedList,
                genderList = genderList,
                ageList = ageList,
                regionList = regionList,
                isLoggedIn = isLoggedIn,
                currentDistance = currentDistance,
                onSearchChanged = onSearchChanged,
                onDistanceChange = onDistanceChange
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp),
                state = rememberLazyGridState(),
                modifier = Modifier.weight(1f)
            ) {
                items(catsList) { cat ->
                    CatCard(
                        cat = cat,
                        modifier = Modifier.padding(4.dp),
                        selectAction = { navController.navigate(Screen.CatDetails.createRoute(cat.id)) }
                    )
                }
            }
        }
    }
}