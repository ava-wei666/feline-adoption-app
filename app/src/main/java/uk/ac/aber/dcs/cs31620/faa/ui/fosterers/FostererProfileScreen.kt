package uk.ac.aber.dcs.cs31620.faa.ui.fosterers

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import uk.ac.aber.dcs.cs31620.faa.model.FosterersViewModel
import uk.ac.aber.dcs.cs31620.faa.ui.components.CatCard
import uk.ac.aber.dcs.cs31620.faa.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FostererProfileScreen(
    navController: NavHostController,
    fostererId: Long,
    viewModel: FosterersViewModel = viewModel()
) {
    val fosterer = viewModel.getFosterer(fostererId)
    val cats by viewModel.getCatsForFosterer(fostererId).observeAsState(listOf())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(fosterer?.name ?: "Fosterer Profile") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding).fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
           //foster information
            if (fosterer != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Image(
                    painter = painterResource(id = fosterer.imageResId),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(120.dp).clip(CircleShape)
                )
                Text(
                    text = fosterer.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Text(
                    text = fosterer.regionName,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = "Phone: ${fosterer.phoneNumber}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 4.dp)
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

                Text(
                    text = "Cats with ${fosterer.name}",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.align(Alignment.Start).padding(start = 16.dp)
                )
            }

            // cat's grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(cats) { cat ->
                    CatCard(
                        cat = cat,
                        modifier = Modifier.padding(4.dp),
                       //click on the cat to see the cat's detail page
                        selectAction = {
                            navController.navigate(Screen.CatDetails.createRoute(it.id))
                        }
                    )
                }
            }
        }
    }
}