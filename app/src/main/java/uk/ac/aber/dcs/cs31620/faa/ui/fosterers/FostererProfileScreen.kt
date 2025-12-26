package uk.ac.aber.dcs.cs31620.faa.ui.fosterers

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
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
    val currentFosterer = fostererState
    val cats by viewModel.getCatsForFosterer(fostererId).observeAsState(listOf())

    // ✅ 修复图1：删除了 Scaffold 的 TopAppBar，让页面内容直接置顶
    Scaffold { innerPadding ->
        if (currentFosterer != null) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = innerPadding,
                modifier = Modifier.fillMaxSize()
            ) {
                item(span = { GridItemSpan(2) }) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // 允许用户通过顶部的系统返回键或者你在UI里加个浮动返回
                        IconButton(
                            onClick = { navController.navigateUp() },
                            modifier = Modifier.align(Alignment.Start)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }

                        Image(
                            painter = painterResource(id = currentFosterer.imageResId),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth(0.85f)
                                .height(200.dp)
                                .clip(RoundedCornerShape(48.dp))
                        )

                        Column(modifier = Modifier.fillMaxWidth().padding(32.dp, 16.dp)) {
                            Text(text = currentFosterer.name, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.LocationOn, null, Modifier.size(18.dp))
                                Spacer(Modifier.width(4.dp))
                                Text(text = currentFosterer.address, color = Color.Gray)
                            }
                        }

                        FostererInfoRow(label = "age", value = "46 years")
                        Spacer(Modifier.height(12.dp))
                        FostererInfoRow(label = "phone number", value = currentFosterer.phoneNumber)
                        Spacer(Modifier.height(12.dp))
                        FostererInfoRow(label = "gender", value = "Female")

                        Spacer(modifier = Modifier.height(32.dp))

                        Text(
                            text = "Cats with ${currentFosterer.name}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.align(Alignment.Start).padding(start = 16.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
                items(cats) { cat ->
                    CatCard(cat = cat, modifier = Modifier.padding(8.dp), selectAction = {
                        navController.navigate(Screen.CatDetails.createRoute(cat.id))
                    })
                }
            }
        }
    }
}

@Composable
fun FostererInfoRow(label: String, value: String) {
    Surface(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp).height(56.dp), color = Color.White, shadowElevation = 2.dp, shape = RoundedCornerShape(4.dp)) {
        Row(modifier = Modifier.padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = label, color = Color.Gray)
            Text(text = value, fontWeight = FontWeight.Medium)
        }
    }
}