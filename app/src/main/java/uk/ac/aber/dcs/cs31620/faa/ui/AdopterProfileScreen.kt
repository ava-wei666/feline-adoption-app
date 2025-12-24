package uk.ac.aber.dcs.cs31620.faa.ui.profile

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import uk.ac.aber.dcs.cs31620.faa.model.AdopterViewModel
import uk.ac.aber.dcs.cs31620.faa.ui.components.TopLevelScaffold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdopterProfileScreen(
    navController: NavHostController,
    adopterViewModel: AdopterViewModel
) {
    val user by adopterViewModel.user.observeAsState()
    val context = LocalContext.current

    if (user == null) {
        navController.navigateUp()
        return
    }

    //input box
    var name by remember { mutableStateOf(user!!.name) }
    var address by remember { mutableStateOf(user!!.address) }

    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Profile") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    val newLat = if(address != user!!.address) user!!.latitude + 0.01 else user!!.latitude

                    val updatedUser = user!!.copy(
                        name = name,
                        address = address,
                        latitude = newLat
                    )

                    // call viewModel to update user message
                    adopterViewModel.saveUserChanges(updatedUser)

                    Toast.makeText(context, "Profile Updated!", Toast.LENGTH_SHORT).show()
                    navController.navigateUp()
                },
                icon = { Icon(Icons.Filled.Save, "Save") },
                text = { Text("Save Changes") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Image(
                painter = painterResource(id = user!!.imageResId),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text("Address") },
                supportingText = { Text("Changing address will update your location for distance search.") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}