package com.ulatina.proyectoprogra5.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ulatina.proyectoprogra5.data.database.model.EjerciciosFirebase
import com.ulatina.proyectoprogra5.data.database.model.RutinaFirebase
import com.ulatina.proyectoprogra5.viewModel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewRoutineDetails(
    navController: NavController,
    rutinaId: Long
) {

    var rutina by remember { mutableStateOf<RutinaFirebase?>(null) }
    var ejercicios by remember { mutableStateOf<List<EjerciciosFirebase>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var confirmDeleteDialog by remember { mutableStateOf(false) }
    var ejercicioToDelete by remember { mutableStateOf<EjerciciosFirebase?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(rutina?.name ?: "Detalles de rutina") },
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
                    navController.navigate("addExercise/$rutinaId")
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar ejercicio")
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
                        onClick = {

                            loading = true
                            error = null
                        },
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        Text("Reintentar")
                    }
                }
            } else if (ejercicios.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "No hay ejercicios en esta rutina",
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Button(
                        onClick = { navController.navigate("addExercise/$rutinaId") },
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        Text("Agregar ejercicio")
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    items(ejercicios) { ejercicio ->
                        EjercicioItem(
                            ejercicio = ejercicio,
                            onDelete = {
                                ejercicioToDelete = ejercicio
                                confirmDeleteDialog = true
                            }
                        )
                    }
                }
            }
        }
    }

    if (confirmDeleteDialog) {
        AlertDialog(
            onDismissRequest = { confirmDeleteDialog = false },
            title = { Text("Eliminar ejercicio") },
            text = { Text("¿Estás seguro de que deseas eliminar '${ejercicioToDelete?.name}'?") },
            confirmButton = {
                TextButton(
                    onClick = {

                        ejercicios = ejercicios.filter { it.id != ejercicioToDelete?.id }
                        confirmDeleteDialog = false
                    }
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { confirmDeleteDialog = false }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun EjercicioItem(ejercicio: EjerciciosFirebase, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = ejercicio.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${ejercicio.reps} repeticiones x ${ejercicio.peso} kg",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}