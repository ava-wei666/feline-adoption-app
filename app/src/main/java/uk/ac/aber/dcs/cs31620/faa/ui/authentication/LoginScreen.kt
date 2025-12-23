package uk.ac.aber.dcs.cs31620.faa.ui.authentication

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay // 1. 记得导入这个 delay
import uk.ac.aber.dcs.cs31620.faa.model.AdopterViewModel
import uk.ac.aber.dcs.cs31620.faa.ui.components.TopLevelScaffold
import uk.ac.aber.dcs.cs31620.faa.ui.navigation.Screen

@Composable
fun LoginScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    adopterViewModel: AdopterViewModel = viewModel()
) {
    val user by adopterViewModel.user.observeAsState()

    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    TopLevelScaffold(
        navController = navController,
        coroutineScope = coroutineScope,
        snackbarHostState = snackbarHostState
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (user == null) {
                //display the login form
                LoginForm(
                    onLoginClick = { u, p ->
                        adopterViewModel.doLogin(u, p)
                    }
                )
            } else {
                //welcomescreen
                WelcomeScreen(
                    username = user!!.name,
                    onLogoutClick = { adopterViewModel.doLogout() }
                )

                // wait 2 seconds and then redirect to home
                LaunchedEffect(Unit) {
                    delay(2000)
                    navController.navigate(Screen.Home.route) {
                        //clear the backstack
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            }
        }
    }
}

@Composable
fun LoginForm(onLoginClick: (String, String) -> Unit) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    Text(text = "Please Log In", style = MaterialTheme.typography.headlineMedium)
    Spacer(modifier = Modifier.height(16.dp))

    OutlinedTextField(
        value = username,
        onValueChange = { username = it },
        label = { Text("Username") },
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(8.dp))

    OutlinedTextField(
        value = password,
        onValueChange = { password = it },
        label = { Text("Password") },
        visualTransformation = PasswordVisualTransformation(),
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(24.dp))

    Button(
        onClick = {
            if (username.isNotEmpty() && password.isNotEmpty()) {
                onLoginClick(username, password)
            } else {
                isError = true
            }
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Login")
    }

    if (isError) {
        Text("Please enter both username and password", color = MaterialTheme.colorScheme.error)
    }
}

//wait seconds and then redirect to home
@Composable
fun WelcomeScreen(username: String, onLogoutClick: () -> Unit) {
    Text(text = "Login Successful!", style = MaterialTheme.typography.headlineMedium)
    Spacer(modifier = Modifier.height(8.dp))
    Text(text = "Welcome back, $username")
    Spacer(modifier = Modifier.height(8.dp))
    Text(text = "Redirecting to Home...", style = MaterialTheme.typography.bodySmall)
}