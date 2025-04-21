package com.ulatina.proyectoprogra5.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ulatina.proyectoprogra5.data.database.model.EjerciciosFirebase
import com.ulatina.proyectoprogra5.viewModel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExercise(
    navController: NavController,
    rutinaId: String,
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    var ejercicioNombre by remember { mutableStateOf("") }
    var repeticiones by remember { mutableStateOf("") }
    var peso by remember { mutableStateOf("") }
    var guardando by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    fun validarCampos(): Boolean {
        return when {
            ejercicioNombre.isEmpty() -> {
                error = "El nombre del ejercicio no puede estar vacío"
                false
            }
            repeticiones.isEmpty() || repeticiones.toIntOrNull() == null -> {
                error = "Ingresa un número válido de repeticiones"
                false
            }
            peso.isEmpty() || peso.toDoubleOrNull() == null -> {
                error = "Ingresa un peso válido"
                false
            }
            else -> true
        }
    }

    fun guardarEjercicio() {
        if (!guardando && validarCampos()) {
            guardando = true
            error = null

            try {
                val nuevoEjercicio = EjerciciosFirebase(
                    name = ejercicioNombre,
                    reps = repeticiones.toLongOrNull() ?: 0,
                    peso = peso.toLongOrNull() ?: 0
                )

                navController.popBackStack()
            } catch (e: Exception) {
                error = "Error al guardar: ${e.message}"
                guardando = false
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nuevo Ejercicio") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
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
                    value = ejercicioNombre,
                    onValueChange = { ejercicioNombre = it },
                    label = { Text("Nombre del ejercicio") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    isError = error != null && ejercicioNombre.isEmpty()
                )

                OutlinedTextField(
                    value = repeticiones,
                    onValueChange = { repeticiones = it },
                    label = { Text("Repeticiones") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    isError = error != null && (repeticiones.isEmpty() || repeticiones.toIntOrNull() == null)
                )

                OutlinedTextField(
                    value = peso,
                    onValueChange = { peso = it },
                    label = { Text("Peso (kg)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    isError = error != null && (peso.isEmpty() || peso.toDoubleOrNull() == null)
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

                Button(
                    onClick = { guardarEjercicio() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    enabled = !guardando
                ) {
                    if (guardando) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Text("GUARDAR EJERCICIO")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Agrega este ejercicio a tu rutina de entrenamiento para seguir tu progreso.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Sugerencias:",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        SugerenciaEjercicio(nombre = "Press de banca", onSelect = {
                            ejercicioNombre = "Press de banca"
                        })

                        SugerenciaEjercicio(nombre = "Sentadillas", onSelect = {
                            ejercicioNombre = "Sentadillas"
                        })

                        SugerenciaEjercicio(nombre = "Peso muerto", onSelect = {
                            ejercicioNombre = "Peso muerto"
                        })

                        SugerenciaEjercicio(nombre = "Dominadas", onSelect = {
                            ejercicioNombre = "Dominadas"
                        })
                    }
                }
            }

            if (guardando) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Guardando ejercicio...")
                    }
                }
            }
        }
    }
}


@Composable
fun SugerenciaEjercicio(nombre: String, onSelect: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = nombre,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )

        TextButton(
            onClick = onSelect
        ) {
            Text("Seleccionar")
        }
    }
}