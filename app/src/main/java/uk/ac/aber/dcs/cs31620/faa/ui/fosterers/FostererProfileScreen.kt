package uk.ac.aber.dcs.cs31620.faa.ui.fosterers

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    val fostererState by viewModel.getFosterer(fostererId).observeAsState()
    val cats by viewModel.getCatsForFosterer(fostererId).observeAsState(listOf())

    Scaffold(
        containerColor = Color(0xFFF0DFD8)
    ) { innerPadding ->
        fostererState?.let { currentFosterer ->
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(
                    top = innerPadding.calculateTopPadding(),
                    bottom = 16.dp,
                    start = 16.dp,
                    end = 16.dp
                ),
                modifier = Modifier.fillMaxSize()
            ) {
                // 1. personal information section at the top
                item(span = { GridItemSpan(2) }) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        IconButton(
                            onClick = { navController.navigateUp() },
                            modifier = Modifier.align(Alignment.Start)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.Black)
                        }

                        //fosters portrait
                        Image(
                            painter = painterResource(id = currentFosterer.imageResId),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth(0.85f)
                                .height(220.dp)
                                .clip(RoundedCornerShape(32.dp))
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        //name and address
                        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                            Text(text = currentFosterer.name, fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.LocationOn, null, Modifier.size(18.dp), tint = Color.Black)
                                Spacer(Modifier.width(4.dp))
                                Text(text = currentFosterer.address, color = Color.Black)
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        //information card
                        FostererInfoRow(label = "age", value = "46 years")
                        Spacer(Modifier.height(12.dp))
                        FostererInfoRow(label = "phone number", value = currentFosterer.phoneNumber)
                        Spacer(Modifier.height(12.dp))
                        FostererInfoRow(label = "gender", value = "Female")

                        Spacer(modifier = Modifier.height(32.dp))

                        // title
                        Text(
                            text = "Cats being fostered by ${currentFosterer.name}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.align(Alignment.Start).padding(start = 8.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                // cat list section
                items(cats) { cat ->
                    CatCard(
                        cat = cat,
                        modifier = Modifier.padding(6.dp),
                        selectAction = {
                            navController.navigate(Screen.CatDetails.createRoute(cat.id))
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun FostererInfoRow(label: String, value: String) {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp).height(56.dp),
        color = Color.White,
        shape = RoundedCornerShape(4.dp),
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, color = Color.Black, fontSize = 16.sp)
            Text(text = value, fontWeight = FontWeight.Bold, color = Color.Black, fontSize = 16.sp)
        }
    }
}