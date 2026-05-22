package uk.ac.aber.dcs.cs31620.faa.ui.cats

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import uk.ac.aber.dcs.cs31620.faa.model.AdopterViewModel
import uk.ac.aber.dcs.cs31620.faa.model.Cat
import uk.ac.aber.dcs.cs31620.faa.model.CatsViewModel
import uk.ac.aber.dcs.cs31620.faa.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
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

    var showLoginDialog by remember { mutableStateOf(user == null) }

    if (cat != null) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Cat Details", fontSize = 20.sp) },
                    navigationIcon = {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFF0DFD8))
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .background(Color(0xFFF0DFD8))
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = rememberAsyncImagePainter(cat.imagePath),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
                        .clip(RoundedCornerShape(30.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(20.dp))

                Column(Modifier.fillMaxWidth()) {
                    Text(
                        text = cat.name,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = Color.Black
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "33 Pier St, Aberystwyth SY23 2LN",
                            color = Color.Black,
                            fontSize = 14.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                DetailCardRow(label = "age", value = "3 years")
                DetailCardRow(label = "breed", value = cat.breed)
                DetailCardRow(label = "gender", value = cat.gender.name)

                Spacer(modifier = Modifier.height(40.dp))

                Button(
                    onClick = {
                        if (user != null) {
                            navController.navigate(Screen.FostererProfile.createRoute(cat.fostererId))
                        } else {
                            showLoginDialog = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8D5533)),
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .height(52.dp),
                    shape = RoundedCornerShape(26.dp)
                ) {
                    Text("View Fosterer Info", fontSize = 16.sp, color = Color.White)
                }
            }

            if (showLoginDialog) {
                AlertDialog(
                    onDismissRequest = {
                        showLoginDialog = false
                        navController.navigateUp()
                    },
                    containerColor = Color(0xFFFDF7F2),
                    title = { Text("Login Required", fontWeight = FontWeight.Bold) },
                    text = {
                        Column {
                            Text("Please log in or register as an adopter to use the fosterer search and contact features.")
                            Spacer(modifier = Modifier.height(20.dp))
                            LinearProgressIndicator(
                                progress = { 0.5f },
                                modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                                color = Color(0xFF8D5533),
                                trackColor = Color(0xFFF2D8C7)
                            )
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            showLoginDialog = false
                            navController.navigate(Screen.Login.route)
                        }) {
                            Text("Log in", color = Color.Black, fontWeight = FontWeight.Bold)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            showLoginDialog = false
                            navController.navigateUp()
                        }) {
                            Text("Cancel", color = Color.Black)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun DetailCardRow(label: String, value: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(4.dp),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            //label颜色改成了黑色
            Text(text = label, color = Color.Black, fontSize = 16.sp)
            Text(text = value, color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}