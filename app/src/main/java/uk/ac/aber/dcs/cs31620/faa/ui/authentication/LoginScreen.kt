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
import androidx.compose.ui.draw.clip
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

// --- 颜色定义 (严格吸取设计稿颜色) ---
private val BeigeBackground = Color(0xFFF5E9E2)
private val InputWhiteBackground = Color.White
private val DarkButtonColor = Color(0xFF2C2C2C)
private val ErrorRed = Color(0xFFB00020)
private val IconGray = Color(0xFF9E9E9E)

@Composable
fun LoginScreen(
    navController: NavHostController,
    adopterViewModel: AdopterViewModel,
    isSignUpMode: Boolean = false
) {
    val currentUser by adopterViewModel.user.observeAsState()

    // 表单状态
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") } // 新增：确认密码

    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    // 错误状态控制
    var isError by remember { mutableStateOf(false) }

    // 登录成功后的自动跳转逻辑
    LaunchedEffect(currentUser) {
        if (currentUser != null) {
            delay(2000) // 保持登录成功状态2秒
            navController.navigate(Screen.Home.route) {
                popUpTo(Screen.Login.route) { inclusive = true }
                popUpTo(Screen.SignUp.route) { inclusive = true }
            }
        }
    }

    Scaffold(
        containerColor = BeigeBackground,
        contentWindowInsets = WindowInsets(0.dp) // 消除顶部白条
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .systemBarsPadding()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- 顶部导航栏 ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                contentAlignment = Alignment.Center
            ) {
                // 返回箭头 (靠左)
                IconButton(
                    onClick = { navController.navigateUp() },
                    modifier = Modifier.align(Alignment.CenterStart).offset(x = (-12).dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = "Back",
                        tint = DarkButtonColor
                    )
                }
                // 标题
                Text(
                    text = if (isSignUpMode) "sign up" else "sign in",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = DarkButtonColor
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            if (currentUser == null) {
                // ================= 表单区域 =================
                Column(modifier = Modifier.fillMaxWidth()) {

                    // 1. Username 标签 (出错时显示红色警告图标)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
                    ) {
                        Text(
                            text = "Username",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = DarkButtonColor
                        )
                        if (isError) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(Icons.Outlined.Warning, null, tint = ErrorRed, modifier = Modifier.size(18.dp))
                        }
                    }

                    // 1. Username 输入框
                    OutlinedTextField(
                        value = username,
                        onValueChange = {
                            username = it
                            if (isError) isError = false
                        },
                        placeholder = { Text("ziw11@aber.ac.uk", color = Color.LightGray) }, // 恢复了你图中的灰色占位符
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(percent = 50),
                        colors = getTextFieldColors(isError),
                        trailingIcon = {
                            if (isError) {
                                Icon(Icons.Outlined.Warning, null, tint = ErrorRed)
                            } else {
                                Icon(Icons.Outlined.Email, null, tint = IconGray)
                            }
                        },
                        singleLine = true,
                        isError = isError
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // 2. Password 标签
                    Text(
                        text = "password",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = DarkButtonColor,
                        modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
                    )

                    // 2. Password 输入框
                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            if (isError) isError = false
                        },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(percent = 50),
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        colors = getTextFieldColors(isError),
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                val icon = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                                Icon(icon, null, tint = IconGray)
                            }
                        },
                        singleLine = true,
                        isError = isError,
                        placeholder = { Text("*********", color = Color.LightGray) }
                    )

                    // 3. Confirm Password (仅在注册模式显示)
                    if (isSignUpMode) {
                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = "Confirm password", // 对应PDF: sign in_Enter home-1.pdf
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = DarkButtonColor,
                            modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
                        )

                        OutlinedTextField(
                            value = confirmPassword,
                            onValueChange = {
                                confirmPassword = it
                                if (isError) isError = false
                            },
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            shape = RoundedCornerShape(percent = 50),
                            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            colors = getTextFieldColors(isError),
                            trailingIcon = {
                                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                                    val icon = if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                                    Icon(icon, null, tint = IconGray)
                                }
                            },
                            singleLine = true,
                            isError = isError
                        )
                    }

                    // 错误文字提示
                    if (isError) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "The input information is incorrect,\nplease re-enter",
                            color = ErrorRed,
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                // ================= 按钮区域 =================
                Button(
                    onClick = {
                        // 1. 基础非空验证
                        if (username.isBlank() || password.isBlank()) {
                            isError = true
                        }
                        // 2. 注册模式下的密码一致性验证
                        else if (isSignUpMode && password != confirmPassword) {
                            isError = true
                        }
                        else {
                            // 执行操作
                            if (isSignUpMode) {
                                adopterViewModel.doRegister(username, password)
                            } else {
                                adopterViewModel.doLogin(username, password)
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DarkButtonColor),
                    shape = RoundedCornerShape(12.dp),
                    // 注册按钮文字较长，宽度需要自适应或设宽一点
                    modifier = Modifier
                        .height(50.dp)
                        .then(if (isSignUpMode && !isError) Modifier.fillMaxWidth(0.6f) else Modifier.width(130.dp))
                ) {
                    // 按钮文案逻辑：出错 -> Retry; 注册 -> Register and Enter; 登录 -> Enter
                    Text(
                        text = when {
                            isError -> "Retry"
                            isSignUpMode -> "Register and Enter"
                            else -> "Enter"
                        },
                        color = Color.White,
                        fontSize = if (isSignUpMode && !isError) 16.sp else 18.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // ================= 底部切换链接 =================
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 32.dp)
                ) {
                    Text(
                        text = if (isSignUpMode) "Already have an account? " else "Don't have an account? ",
                        fontSize = 15.sp,
                        color = DarkButtonColor
                    )
                    Text(
                        text = if (isSignUpMode) "sign in" else "sign up",
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .clickable {
                                // 切换模式时重置所有状态
                                isError = false
                                username = ""
                                password = ""
                                confirmPassword = ""
                                navController.navigate(if (isSignUpMode) Screen.Login.route else Screen.SignUp.route)
                            },
                        textDecoration = TextDecoration.Underline,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = DarkButtonColor
                    )
                }

            } else {
                // ================= 登录成功状态 =================
                // 对应需求2：保持状态2秒
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Welcome back!",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkButtonColor
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "${currentUser?.username}",
                            fontSize = 18.sp,
                            color = DarkButtonColor
                        )
                    }
                }
            }
        }
    }
}

// 提取颜色配置以保持代码整洁
@Composable
private fun getTextFieldColors(isError: Boolean) = OutlinedTextFieldDefaults.colors(
    focusedContainerColor = InputWhiteBackground,
    unfocusedContainerColor = InputWhiteBackground,
    errorContainerColor = InputWhiteBackground,
    focusedBorderColor = Color.Transparent,
    unfocusedBorderColor = Color.Transparent,
    errorBorderColor = ErrorRed, // 只有出错时显示红色边框
    errorCursorColor = ErrorRed
)