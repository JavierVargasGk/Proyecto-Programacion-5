package com.ulatina.proyectoprogra5.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.ulatina.proyectoprogra5.data.database.model.RutinaFirebase
import com.ulatina.proyectoprogra5.data.database.model.UsuarioFirebase
import com.ulatina.proyectoprogra5.viewModel.LoginViewModel
import com.ulatina.proyectoprogra5.viewModel.UsuarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRoutine(
    navController: NavController,
    usuarioViewModel: UsuarioViewModel = hiltViewModel(),
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    var rutinaNombre by remember { mutableStateOf("") }
    var guardando by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    val currentUser = loginViewModel.currentUser.value

    fun validarCampos(): Boolean {
        return when {
            rutinaNombre.isEmpty() -> {
                error = "El nombre de la rutina no puede estar vacío"
                false
            }
            else -> true
        }
    }

    fun guardarRutina() {
        if (!guardando && validarCampos()) {
            guardando = true
            error = null

            val user = currentUser
            val userId = user?.uid
            if (userId == null) {
                error = "Usuario no autenticado"
                guardando = false
                return
            }

            val database = FirebaseDatabase.getInstance()
            val userRef = database.reference.child("Usuarios").child(userId).child(userId)

            userRef.get().addOnSuccessListener { snapshot ->
                val usuario = snapshot.getValue(UsuarioFirebase::class.java)
                if (usuario == null) {
                    error = "Usuario no encontrado en la base de datos"
                    guardando = false
                    return@addOnSuccessListener
                }

                val rutinaId = userRef.push().key ?: run {
                    error = "No se pudo generar ID para la rutina"
                    guardando = false
                    return@addOnSuccessListener
                }

                val nuevaRutina = RutinaFirebase(
                    id = rutinaId,
                    name = rutinaNombre,
                    isSelected = false,
                    ejercicios = listOf()
                )

                val rutinasActualizadas = usuario.rutinas.toMutableList().apply { add(nuevaRutina) }
                val usuarioActualizado = usuario.copy(rutinas = rutinasActualizadas)

                // Save the entire user again (including new rutina list)
                userRef.setValue(usuarioActualizado)
                    .addOnSuccessListener {
                        guardando = false
                        navController.popBackStack()
                    }
                    .addOnFailureListener { e ->
                        error = "Error al guardar rutina: ${e.message}"
                        guardando = false
                    }
            }.addOnFailureListener { e ->
                error = "Error al acceder al usuario: ${e.message}"
                guardando = false
            }
        }
    }



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


                Button(
                    onClick = { guardarRutina()
                              guardando = false},
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
                    Text("GUARDAR RUTINA")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Después de crear la rutina, podrás agregar ejercicios.",
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

                        SugerenciaRutina(nombre = "Rutina de Pecho", onSelect = {
                            rutinaNombre = "Rutina de Pecho"
                        })

                        SugerenciaRutina(nombre = "Rutina de Piernas", onSelect = {
                            rutinaNombre = "Rutina de Piernas"
                        })

                        SugerenciaRutina(nombre = "Rutina de Espalda", onSelect = {
                            rutinaNombre = "Rutina de Espalda"
                        })

                        SugerenciaRutina(nombre = "Rutina Full Body", onSelect = {
                            rutinaNombre = "Rutina Full Body"
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
                        Text("Guardando rutina...")
                    }
                }
            }
        }
    }
}

@Composable
fun SugerenciaRutina(nombre: String, onSelect: () -> Unit) {
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
