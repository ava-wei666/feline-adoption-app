package uk.ac.aber.dcs.cs31620.faa.ui.cats

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import uk.ac.aber.dcs.cs31620.faa.model.AdopterViewModel
import uk.ac.aber.dcs.cs31620.faa.model.Cat
import uk.ac.aber.dcs.cs31620.faa.model.CatsViewModel
import uk.ac.aber.dcs.cs31620.faa.ui.navigation.Screen

@Composable
fun CatDetailsScreenTopLevel(
    navController: NavHostController,
    catId: Int,
    catsViewModel: CatsViewModel = viewModel(),
    adopterViewModel: AdopterViewModel = viewModel()
) {
    val catList by catsViewModel.catList.observeAsState(listOf())
    val cat = catList.find { it.id == catId }
    val user by adopterViewModel.user.observeAsState()

    if (cat != null) {
        CatDetailsScreen(
            cat = cat,
            navController = navController,
            isLoggedIn = user != null
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatDetailsScreen(
    cat: Cat,
    navController: NavHostController,
    isLoggedIn: Boolean
) {
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(cat.name) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding).padding(16.dp).fillMaxSize().verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = rememberAsyncImagePainter(cat.imagePath),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth().height(250.dp).clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))

            // 信息卡片展示
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(2.dp)) {
                Column(Modifier.padding(16.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Age")
                        Text("3 years", fontWeight = FontWeight.Bold)
                    }
                    Divider(Modifier.padding(vertical = 8.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Breed")
                        Text(cat.breed, fontWeight = FontWeight.Bold)
                    }
                    Divider(Modifier.padding(vertical = 8.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Gender")
                        Text(cat.gender.name, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // ✅ 解决图四：点击跳转 Fosterer 详情逻辑
            Button(
                onClick = {
                    if (isLoggedIn) {
                        // 登录用户跳转到寄养人详情页 (FostererProfile)
                        navController.navigate(Screen.FostererProfile.createRoute(cat.fostererId))
                    } else {
                        showDialog = true
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8D5533)),
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text("View Fosterer Info")
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Login Required") },
                text = { Text("Please log in or register as an adopter to use the fosterer search and contact features.") },
                confirmButton = { TextButton(onClick = { navController.navigate(Screen.Login.route); showDialog = false }) { Text("Log in") } },
                dismissButton = { TextButton(onClick = { showDialog = false }) { Text("Cancel") } }
            )
        }
    }
}