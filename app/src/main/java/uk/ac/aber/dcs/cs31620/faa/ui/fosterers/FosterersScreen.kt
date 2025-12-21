package uk.ac.aber.dcs.cs31620.faa.ui.fosterers

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import uk.ac.aber.dcs.cs31620.faa.model.Fosterer
import uk.ac.aber.dcs.cs31620.faa.model.FosterersViewModel
import uk.ac.aber.dcs.cs31620.faa.ui.components.TopLevelScaffold
import uk.ac.aber.dcs.cs31620.faa.ui.navigation.Screen //
@Composable
fun FosterersScreenTopLevel(
    navController: NavHostController,
    viewModel: FosterersViewModel = viewModel()
) {
    val fostererList by viewModel.fostererList.observeAsState(listOf())

    FosterersScreen(
        fostererList = fostererList,
        navController = navController
    )
}

@Composable
fun FosterersScreen(
    fostererList: List<Fosterer>,
    navController: NavHostController
) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    TopLevelScaffold(
        navController = navController,
        coroutineScope = coroutineScope,
        snackbarHostState = snackbarHostState
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(fostererList) { fosterer ->
                    FostererCard(
                        fosterer = fosterer,
                        clickAction = {
                            navController.navigate(Screen.FostererProfile.createRoute(fosterer.id))
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun FostererCard(
    fosterer: Fosterer,
    clickAction: () -> Unit // fix the clickAction error
) {
    // 计算距离的逻辑
    val userLat = 52.4180
    val userLon = -4.0657
    val results = FloatArray(1)
    android.location.Location.distanceBetween(userLat, userLon, fosterer.latitude, fosterer.longitude, results)
    val distanceString = "%.1f km".format(results[0] / 1000)

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(200.dp)
            .clickable { clickAction() }, // click the card to see the detail page
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // 图片
            Image(
                painter = painterResource(id = fosterer.imageResId),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = fosterer.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = distanceString,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}