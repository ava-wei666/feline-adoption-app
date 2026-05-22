package uk.ac.aber.dcs.cs31620.faa.ui.fosterers

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.ui.unit.sp
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
        snackbarHostState = snackbarHostState,
        adopterViewModel = viewModel()
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
                        clickAction = {
                            navController.navigate(Screen.FostererCats.createRoute(fosterer.id))
                        }
                    )
                }
            }
        }

        if (showDistanceDialog) {
            DistanceSearchDialog(
                initialDistance = currentDistance,
                onDismiss = { showDistanceDialog = false },
                onConfirm = { newDist ->
                    onDistanceChange(newDist)
                    showDistanceDialog = false
                }
            )
        }
    }
}

@Composable
fun FilterSection(
    currentDistance: Float,
    currentRegion: String,
    onDistanceClick: () -> Unit,
    onRegionSelected: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp, color = Color.Black)
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FilterPillButton(text = "Search: ${currentDistance.roundToInt()}km", onClick = onDistanceClick, modifier = Modifier.weight(1f))
            RegionDropdownPill(selectedRegion = currentRegion, onRegionSelected = onRegionSelected, modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
fun FilterPillButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier, hasIcon: Boolean = false) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(44.dp),
        shape = RoundedCornerShape(22.dp),
        border = BorderStroke(1.dp, Color.Black),
        color = Color.Transparent
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
            Text(text = text, style = MaterialTheme.typography.bodyMedium)
            if (hasIcon) Icon(Icons.Default.ArrowDropDown, null)
        }
    }
}

@Composable
fun RegionDropdownPill(selectedRegion: String, onRegionSelected: (String) -> Unit, modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }
    val regions = listOf("Any region", "Aberystwyth", "London", "Birmingham", "Other")
    Box(modifier = modifier) {
        FilterPillButton(text = selectedRegion, onClick = { expanded = true }, hasIcon = true, modifier = Modifier.fillMaxWidth())
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
        Card(shape = RoundedCornerShape(28.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFF5E9E2)), modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Distance to travel", style = MaterialTheme.typography.headlineSmall)
                Text("Indicate how far between you and cats!", style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(20.dp))
                Text(text = "${sliderValue.roundToInt()} km", fontWeight = FontWeight.Bold)
                Slider(value = sliderValue, onValueChange = { sliderValue = it }, valueRange = 0f..50f, colors = SliderDefaults.colors(thumbColor = Color(0xFF8D5533), activeTrackColor = Color(0xFF8D5533)))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) { Text("Dismiss", color = Color.Black) }
                    TextButton(onClick = { onConfirm(sliderValue) }) { Text("Confirm", color = Color.Black) }
                }
            }
        }
    }
}

@Composable
fun FostererCard(fosterer: Fosterer, clickAction: () -> Unit) {
    val userLat = 52.4180
    val userLon = -4.0657
    val results = FloatArray(1)
    android.location.Location.distanceBetween(userLat, userLon, fosterer.latitude, fosterer.longitude, results)
    Card(
        modifier = Modifier.padding(8.dp).fillMaxWidth().height(210.dp).clickable { clickAction() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column {
            Image(
                painter = painterResource(id = fosterer.imageResId),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth().height(140.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(8.dp)) {
                Text(text = fosterer.name, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(text = "%.1f km".format(results[0] / 1000), color = Color(0xFF8D5533), style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}