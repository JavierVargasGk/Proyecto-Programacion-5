package com.ulatina.proyectoprogra5.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ulatina.proyectoprogra5.data.database.model.RutinaFirebase
import com.ulatina.proyectoprogra5.viewModel.LoginViewModel
import com.ulatina.proyectoprogra5.viewModel.UsuarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController,
    loginViewModel: LoginViewModel = hiltViewModel(),
    usuarioViewModel: UsuarioViewModel = hiltViewModel()
) {
    val usuarios by usuarioViewModel.usuarios.observeAsState(initial = emptyList())
    val loading by usuarioViewModel.loading.observeAsState(initial = true)
    val error by usuarioViewModel.error.observeAsState()
    val currentUser = loginViewModel.currentUser.value

    LaunchedEffect(Unit) {
        usuarioViewModel.getUsuarios(true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Rutinas") },
                actions = {
                    IconButton(onClick = {
                        loginViewModel.logout()
                        navController.navigate("authLoginScreen") {
                            popUpTo("mainScreen") { inclusive = true }
                        }
                    }) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Cerrar sesión")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("addRoutine") }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar rutina")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else if (error != null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = error ?: "Error desconocido",
                        color = MaterialTheme.colorScheme.error
                    )

                    Button(
                        onClick = { usuarioViewModel.getUsuarios(true) },
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        Text("Reintentar")
                    }
                }
            } else if (usuarios.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "No tienes rutinas creadas",
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Button(
                        onClick = { navController.navigate("addRoutine") },
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        Text("Crear rutina")
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    items(usuarios.flatMap { it.rutinas ?: emptyList() }) { rutina ->
                        RutinaItem(
                            rutina = rutina,
                            onClick = {
                                navController.navigate("viewRoutineDetails/${rutina.id}")
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RutinaItem(rutina: RutinaFirebase, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = rutina.name,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "Ver detalles",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}