package com.ulatina.proyectoprogra5.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.ulatina.proyectoprogra5.ui.views.AuthSignInScreen
import com.ulatina.proyectoprogra5.ui.views.MainScreen
import com.ulatina.proyectoprogra5.viewModel.LoginViewModel
import com.ulatina.proyectoprogra5.viewModel.UsuarioViewModel

@Composable
fun Navigation(viewModelStoreOwner : ViewModelStoreOwner = LocalViewModelStoreOwner.current!!){
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = "authLoginScreen"
        ){
            composable("authLoginScreen"){
                val viewModel: UsuarioViewModel = hiltViewModel(viewModelStoreOwner)
                val loginViewModel : LoginViewModel = hiltViewModel(viewModelStoreOwner)
                MainScreen(navController = navController, loginViewModel ,viewModel)
            }

        }
    }