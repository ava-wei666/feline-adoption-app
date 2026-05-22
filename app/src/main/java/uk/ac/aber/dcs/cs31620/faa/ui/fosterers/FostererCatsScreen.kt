package uk.ac.aber.dcs.cs31620.faa.ui.fosterers

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import uk.ac.aber.dcs.cs31620.faa.model.FosterersViewModel
import uk.ac.aber.dcs.cs31620.faa.ui.components.CatCard
import uk.ac.aber.dcs.cs31620.faa.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FostererCatsScreen(
    navController: NavHostController,
    fostererId: Long,
    viewModel: FosterersViewModel = viewModel()
) {
    val fostererState by viewModel.getFosterer(fostererId).observeAsState()
    val cats by viewModel.getCatsForFosterer(fostererId).observeAsState(listOf())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(fostererState?.name ?: "Cats List", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            Text(
                text = "Cats being fostered by ${fostererState?.name ?: "..."}",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(16.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(cats) { cat ->
                    CatCard(
                        cat = cat,
                        modifier = Modifier.padding(8.dp),
                        selectAction = {
                            navController.navigate(Screen.CatDetails.createRoute(cat.id))
                        }
                    )
                }
            }
        }
    }
}