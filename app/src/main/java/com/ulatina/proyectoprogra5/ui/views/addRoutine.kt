package com.ulatina.proyectoprogra5.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ulatina.proyectoprogra5.data.database.model.RutinaFirebase
import com.ulatina.proyectoprogra5.data.database.model.UsuarioFirebase
import com.ulatina.proyectoprogra5.viewModel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRoutine(
    navController: NavController,
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    var rutinaNombre by remember { mutableStateOf("") }
    var guardando by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    val currentUser = loginViewModel.currentUser.value

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nueva Rutina") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (rutinaNombre.isNotEmpty()) {
                        guardando = true
                        error = null

                        try {
                            val nuevaRutina = RutinaFirebase(
                                name = rutinaNombre,
                                isSelected = false,
                                ejercicios = listOf()
                            )

                            navController.popBackStack()
                        } catch (e: Exception) {
                            error = "Error al guardar: ${e.message}"
                            guardando = false
                        }
                    } else {
                        error = "El nombre de la rutina no puede estar vacío"
                    }
                }
            ) {
                Icon(Icons.Default.Save, contentDescription = "Guardar")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = rutinaNombre,
                    onValueChange = { rutinaNombre = it },
                    label = { Text("Nombre de la rutina") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    isError = error != null && rutinaNombre.isEmpty()
                )

                error?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Después de crear la rutina, podrás agregar ejercicios.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            if (guardando) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}