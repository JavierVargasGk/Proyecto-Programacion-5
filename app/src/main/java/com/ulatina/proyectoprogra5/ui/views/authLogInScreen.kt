package com.ulatina.proyectoprogra5.ui.views


import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.ulatina.proyectoprogra5.viewModel.LoginViewModel

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
){
    val iuState by viewModel.uiState
    val currentUser by viewModel.currentUser

    val email by remember { mutableStateOf("")}
    val pass by remember { mutableStateOf("") }

    LaunchedEffect(currentUser) {
        if (currentUser != null){
            onLoginSuccess()
        }
    }

}
@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    LoginScreen(
        onLoginSuccess = { /* Handle login success */ },
        onNavigateToRegister = { /* Handle navigation to register screen */ }
    )
}