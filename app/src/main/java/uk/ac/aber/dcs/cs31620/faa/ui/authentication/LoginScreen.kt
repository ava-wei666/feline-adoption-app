package uk.ac.aber.dcs.cs31620.faa.ui.authentication

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import uk.ac.aber.dcs.cs31620.faa.model.AdopterViewModel
import uk.ac.aber.dcs.cs31620.faa.ui.navigation.Screen

@Composable
fun LoginScreen(
    navController: NavHostController,
    adopterViewModel: AdopterViewModel,
    isSignUpMode: Boolean = false
) {
    val currentUser by adopterViewModel.user.observeAsState()
    val authError by adopterViewModel.authError.observeAsState(false) // 观察 ViewModel 报错

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    var localError by remember { mutableStateOf(false) }
    val isError = localError || authError

    val themeBackground = Color(0xFFF0DFD8)
    val errorRed = Color(0xFFB00020)
    val darkBtn = Color(0xFF2C2C2C)

    LaunchedEffect(currentUser) {
        if (currentUser != null) {
            delay(2000)
            navController.navigate(Screen.Home.route) {
                popUpTo(Screen.Login.route) { inclusive = true }
            }
        }
    }

    Scaffold(containerColor = themeBackground) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //top navigation
            Box(Modifier.fillMaxWidth().height(56.dp), contentAlignment = Alignment.Center) {
                IconButton(onClick = { navController.navigateUp() }, Modifier.align(Alignment.CenterStart)) {
                    Icon(Icons.AutoMirrored.Outlined.ArrowBack, "Back")
                }
                Text(if (isSignUpMode) "sign up" else "sign in", fontSize = 20.sp, fontWeight = FontWeight.Medium)
            }

            Spacer(Modifier.height(40.dp))

            Column(Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Username", fontSize = 16.sp, color = darkBtn)
                    if (isError) {
                        Spacer(Modifier.width(8.dp))
                        Icon(Icons.Outlined.Warning, null, tint = errorRed, modifier = Modifier.size(18.dp))
                    }
                }
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it; localError = false; adopterViewModel.clearError() },
                    placeholder = { Text("ziw11@aber.ac.uk", color = Color.LightGray) },
                    modifier = Modifier.fillMaxWidth().height(56.dp).padding(top = 8.dp),
                    shape = RoundedCornerShape(50),
                    isError = isError,
                    trailingIcon = { Icon(if (isError) Icons.Outlined.Warning else Icons.Outlined.Email, null, tint = if (isError) errorRed else Color.Gray) },
                    colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = Color.White, unfocusedContainerColor = Color.White, focusedBorderColor = Color.Transparent, unfocusedBorderColor = Color.Transparent, errorBorderColor = errorRed)
                )

                Spacer(Modifier.height(24.dp))

                Text("password", fontSize = 16.sp, color = darkBtn)
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it; localError = false; adopterViewModel.clearError() },
                    modifier = Modifier.fillMaxWidth().height(56.dp).padding(top = 8.dp),
                    shape = RoundedCornerShape(50),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    isError = isError,
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, null, tint = Color.Gray)
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = Color.White, unfocusedContainerColor = Color.White, focusedBorderColor = Color.Transparent, unfocusedBorderColor = Color.Transparent, errorBorderColor = errorRed)
                )

                if (isSignUpMode) {
                    Spacer(Modifier.height(24.dp))
                    Text("Confirm password", fontSize = 16.sp, color = darkBtn)
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it; localError = false },
                        modifier = Modifier.fillMaxWidth().height(56.dp).padding(top = 8.dp),
                        shape = RoundedCornerShape(50),
                        visualTransformation = PasswordVisualTransformation(),
                        isError = isError,
                        colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = Color.White, unfocusedContainerColor = Color.White, focusedBorderColor = Color.Transparent, unfocusedBorderColor = Color.Transparent, errorBorderColor = errorRed)
                    )
                }

                if (isError) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "The input information is incorrect,\nplease re-enter",
                        color = errorRed, fontSize = 14.sp, lineHeight = 20.sp
                    )
                }
            }

            Spacer(Modifier.height(40.dp))

            Button(
                onClick = {
                    if (isError) {
                        username = ""; password = ""; confirmPassword = ""; localError = false; adopterViewModel.clearError()
                    } else {
                        if (username.isBlank() || password.isBlank()) { localError = true }
                        else if (isSignUpMode && password != confirmPassword) { localError = true }
                        else {
                            if (isSignUpMode) adopterViewModel.doRegister(username, password)
                            else adopterViewModel.doLogin(username, password)
                        }
                    }
                },
                modifier = Modifier.height(50.dp).width(if (isSignUpMode && !isError) 200.dp else 130.dp),
                colors = ButtonDefaults.buttonColors(containerColor = darkBtn),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = when {
                        isError -> "Retry"
                        isSignUpMode -> "Register and Enter"
                        else -> "Enter"
                    },
                    color = Color.White, fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.weight(1f))

            Row(modifier = Modifier.padding(bottom = 32.dp)) {
                Text(if (isSignUpMode) "Already have an account? " else "Don't have an account? ")
                Text(
                    text = if (isSignUpMode) "sign in" else "sign up",
                    modifier = Modifier.clickable {
                        localError = false; adopterViewModel.clearError()
                        navController.navigate(if (isSignUpMode) Screen.Login.route else Screen.SignUp.route)
                    },
                    textDecoration = TextDecoration.Underline, fontWeight = FontWeight.Bold
                )
            }
        }
    }
}