package uk.ac.aber.dcs.cs31620.faa.ui.fosterers

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import uk.ac.aber.dcs.cs31620.faa.model.AdopterViewModel
import uk.ac.aber.dcs.cs31620.faa.model.Fosterer
import uk.ac.aber.dcs.cs31620.faa.model.FosterersViewModel
import uk.ac.aber.dcs.cs31620.faa.ui.components.TopLevelScaffold
import uk.ac.aber.dcs.cs31620.faa.ui.navigation.Screen
import kotlin.math.roundToInt

@Composable
fun FosterersScreenTopLevel(
    navController: NavHostController,
    viewModel: FosterersViewModel = viewModel(),
    adopterViewModel: AdopterViewModel
) {
    val fostererList by viewModel.fostererList.observeAsState(listOf())
    val searchDistance by viewModel.searchDistance.observeAsState(50f)
    val searchRegion by viewModel.searchRegion.observeAsState("Any region")
    val currentUser by adopterViewModel.user.observeAsState()

    LaunchedEffect(currentUser) {
        viewModel.updateUserLocation(currentUser)
    }

    FosterersScreen(
        fostererList = fostererList,
        navController = navController,
        isLoggedIn = currentUser != null,
        currentDistance = searchDistance,
        currentRegion = searchRegion,
        onDistanceChange = { viewModel.updateDistance(it) },
        onRegionChange = { viewModel.updateRegion(it) }
    )
}

@Composable
fun FosterersScreen(
    fostererList: List<Fosterer>,
    navController: NavHostController,
    isLoggedIn: Boolean,
    currentDistance: Float,
    currentRegion: String,
    onDistanceChange: (Float) -> Unit,
    onRegionChange: (String) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var showDistanceDialog by remember { mutableStateOf(false) }

    TopLevelScaffold(
        navController = navController,
        coroutineScope = coroutineScope,
        snackbarHostState = snackbarHostState
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            if (isLoggedIn) {
                FilterSection(
                    currentDistance = currentDistance,
                    currentRegion = currentRegion,
                    onDistanceClick = { showDistanceDialog = true },
                    onRegionSelected = onRegionChange
                )
            }
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(fostererList) { fosterer ->
                    FostererCard(
                        fosterer = fosterer,
                        clickAction = { navController.navigate(Screen.FostererProfile.createRoute(fosterer.id)) }
                    )
                }
            }
        }
        if (showDistanceDialog) {
            DistanceSearchDialog(
                initialDistance = currentDistance,
                onDismiss = { showDistanceDialog = false },
                onConfirm = { newDist -> onDistanceChange(newDist); showDistanceDialog = false }
            )
        }
    }
}

@Composable
fun FilterSection(currentDistance: Float, currentRegion: String, onDistanceClick: () -> Unit, onRegionSelected: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        HorizontalDivider(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp), thickness = 1.dp, color = Color.Black)
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            FilterButton(text = "Search: ${currentDistance.roundToInt()}km", onClick = onDistanceClick, modifier = Modifier.weight(1f))
            RegionDropdown(selectedRegion = currentRegion, onRegionSelected = onRegionSelected, modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun FilterButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier, showDropdownIcon: Boolean = false) {
    Surface(onClick = onClick, modifier = modifier.height(40.dp), shape = RoundedCornerShape(20.dp), border = androidx.compose.foundation.BorderStroke(1.dp, Color.Black), color = MaterialTheme.colorScheme.surface) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center, modifier = Modifier.padding(horizontal = 12.dp)) {
            Text(text = text, style = MaterialTheme.typography.bodyMedium)
            if (showDropdownIcon) { Spacer(modifier = Modifier.width(4.dp)); Icon(Icons.Default.ArrowDropDown, contentDescription = null) }
        }
    }
}

@Composable
fun RegionDropdown(selectedRegion: String, onRegionSelected: (String) -> Unit, modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }
    val regions = listOf("Any region", "Aberystwyth", "London", "Birmingham", "Manchester", "Liverpool", "Glasgow", "Other")
    Box(modifier = modifier) {
        FilterButton(text = selectedRegion, onClick = { expanded = true }, showDropdownIcon = true, modifier = Modifier.fillMaxWidth())
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            regions.forEach { region ->
                DropdownMenuItem(text = { Text(region) }, onClick = { onRegionSelected(region); expanded = false })
            }
        }
    }
}

@Composable
fun DistanceSearchDialog(initialDistance: Float, onDismiss: () -> Unit, onConfirm: (Float) -> Unit) {
    var sliderValue by remember { mutableStateOf(initialDistance) }
    Dialog(onDismissRequest = onDismiss) {
        Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant), modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Distance to travel", style = MaterialTheme.typography.headlineSmall)
                Text("Indicate how far between you and cats!", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(24.dp))
                Text(text = "${sliderValue.roundToInt()} km", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Slider(value = sliderValue, onValueChange = { sliderValue = it }, valueRange = 0f..50f, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(24.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    TextButton(onClick = onDismiss) { Text("Dismiss", color = Color.Black) }
                    TextButton(onClick = { onConfirm(sliderValue) }) { Text("Confirm", color = Color.Black) }
                }
            }
        }
    }
}

@Composable
fun FostererCard(fosterer: Fosterer, clickAction: () -> Unit) {
    val userLat = 52.4180; val userLon = -4.0657; val results = FloatArray(1)
    android.location.Location.distanceBetween(userLat, userLon, fosterer.latitude, fosterer.longitude, results)
    Card(modifier = Modifier.padding(8.dp).fillMaxWidth().height(200.dp).clickable { clickAction() }, elevation = CardDefaults.cardElevation(4.dp), shape = MaterialTheme.shapes.medium) {
        Column(modifier = Modifier.fillMaxSize()) {
            Image(painter = painterResource(id = fosterer.imageResId), contentDescription = null, modifier = Modifier.fillMaxWidth().height(130.dp), contentScale = ContentScale.Crop)
            Column(Modifier.padding(8.dp).fillMaxWidth()) {
                Text(fosterer.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text("%.1f km".format(results[0] / 1000), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}