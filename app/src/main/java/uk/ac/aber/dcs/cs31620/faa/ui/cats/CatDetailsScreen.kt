package uk.ac.aber.dcs.cs31620.faa.ui.cats

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import uk.ac.aber.dcs.cs31620.faa.model.Cat
import uk.ac.aber.dcs.cs31620.faa.model.CatsViewModel


@Composable
fun CatDetailsScreenTopLevel(
    navController: NavHostController,
    catId: Int,
    catsViewModel: CatsViewModel = viewModel()
) {
    val catList by catsViewModel.catList.observeAsState(listOf())
    val cat = catList.find { it.id == catId }

    if (cat != null) {
        CatDetailsScreen(cat = cat, navController = navController)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatDetailsScreen(
    cat: Cat,
    navController: NavHostController
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(cat.name) })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Image(
                painter = rememberAsyncImagePainter(cat.imagePath),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth().height(200.dp)
            )
            Text(text = "Name: ${cat.name}", style = MaterialTheme.typography.headlineMedium)
            Text(text = "Breed: ${cat.breed}")
            Text(text = "Description: ${cat.description}")
        }
    }
}