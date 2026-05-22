package uk.ac.aber.dcs.cs31620.faa.ui.profile

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import uk.ac.aber.dcs.cs31620.faa.R
import uk.ac.aber.dcs.cs31620.faa.model.AdopterViewModel
import uk.ac.aber.dcs.cs31620.faa.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdopterProfileScreen(
    navController: NavHostController,
    adopterViewModel: AdopterViewModel
) {
    val currentUser by adopterViewModel.user.observeAsState()
    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var region by remember { mutableStateOf("") }
    var lat by remember { mutableStateOf("") }
    var lng by remember { mutableStateOf("") }


    LaunchedEffect(currentUser) {
        currentUser?.let {
            name = it.name
            address = it.address
            phone = it.phoneNumber
            region = it.region
            lat = it.latitude.toString()
            lng = it.longitude.toString()
        }
    }

    Scaffold(
        topBar = { TopSmallAppBar(navController, "profile") }
    ) { padding ->
        currentUser?.let { user ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Image(
                    painter = painterResource(id = if (user.imageResId != 0) user.imageResId else R.drawable.shin_chan),
                    contentDescription = null,
                    modifier = Modifier.size(120.dp).clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(Modifier.height(24.dp))

                ProfileTextField("Name", name) { name = it }
                ProfileTextField("Address", address) { address = it }
                ProfileTextField("Phone Number", phone) { phone = it }
                ProfileTextField("Region", region) { region = it }

                Row(Modifier.fillMaxWidth()) {
                    Box(Modifier.weight(1f)) { ProfileTextField("Lat", lat) { lat = it } }
                    Spacer(Modifier.width(8.dp))
                    Box(Modifier.weight(1f)) { ProfileTextField("Lng", lng) { lng = it } }
                }

                Spacer(Modifier.height(32.dp))

                Button(
                    onClick = {
                        val updated = user.copy(
                            name = name,
                            address = address,
                            phoneNumber = phone,
                            region = region,
                            latitude = lat.toDoubleOrNull() ?: user.latitude,
                            longitude = lng.toDoubleOrNull() ?: user.longitude
                        )
                        adopterViewModel.saveUserChanges(updated)
                        Toast.makeText(context, "Saved successfully!", Toast.LENGTH_SHORT).show()
                        navController.navigate(Screen.Home.route)
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                ) {
                    Text("Save Changes", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun ProfileTextField(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color.Black)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopSmallAppBar(navController: NavHostController, title: String) {
    CenterAlignedTopAppBar(title = { Text(title) })
}