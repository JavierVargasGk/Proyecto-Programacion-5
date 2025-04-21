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
import com.ulatina.proyectoprogra5.ui.views.AddExercise
import com.ulatina.proyectoprogra5.ui.views.AddRoutine
import com.ulatina.proyectoprogra5.ui.views.AuthSignInScreen
import com.ulatina.proyectoprogra5.ui.views.AuthLoginScreen
import com.ulatina.proyectoprogra5.ui.views.MainScreen
import com.ulatina.proyectoprogra5.ui.views.ViewRoutineDetails
import com.ulatina.proyectoprogra5.viewModel.LoginViewModel
import com.ulatina.proyectoprogra5.viewModel.UsuarioViewModel

@Composable
fun Navigation(viewModelStoreOwner : ViewModelStoreOwner = LocalViewModelStoreOwner.current!!){
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = "authLoginScreen"
        ){
            composable("MainScreen"){
                val viewModel: UsuarioViewModel = hiltViewModel(viewModelStoreOwner)
                val loginViewModel : LoginViewModel = hiltViewModel(viewModelStoreOwner)
                MainScreen(navController = navController, loginViewModel ,viewModel)
            }
            composable("routineDetails/{rutinaId}") { backStackEntry ->
                val rutinaId = backStackEntry.arguments?.getString("rutinaId") ?: ""
                val viewModel: UsuarioViewModel = hiltViewModel(viewModelStoreOwner)
                val loginViewModel : LoginViewModel = hiltViewModel(viewModelStoreOwner)
                ViewRoutineDetails(navController = navController, rutinaId = rutinaId,loginViewModel)
            }
            composable("authLoginScreen") {
                val loginViewModel: LoginViewModel = hiltViewModel(viewModelStoreOwner)
                AuthLoginScreen(navController, loginViewModel)
            }
            composable("authSignInScreen") {
                AuthSignInScreen(navController = navController)
            }
            composable(
                route = "addExercise/{rutinaId}",
                arguments = listOf(navArgument("rutinaId") { type = NavType.StringType })
            ) { backStackEntry ->
                val rutinaId = backStackEntry.arguments?.getString("rutinaId") ?: ""
                val viewModel: UsuarioViewModel = hiltViewModel(viewModelStoreOwner)
                val loginViewModel : LoginViewModel = hiltViewModel(viewModelStoreOwner)
                AddExercise(navController = navController, rutinaId = rutinaId, loginViewModel, viewModel)
            }
            composable("addRoutine") {
                AddRoutine(navController = navController)
            }


        }
    }