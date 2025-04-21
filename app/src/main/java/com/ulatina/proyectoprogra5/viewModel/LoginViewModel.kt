package com.ulatina.proyectoprogra5.viewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.ulatina.proyectoprogra5.data.database.Firebase.FirebaseDb
import com.ulatina.proyectoprogra5.data.database.Firebase.FirebaseRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.ulatina.proyectoprogra5.data.database.di.FirebaseModule
import com.ulatina.proyectoprogra5.data.database.model.UsuarioFirebase

@HiltViewModel
class LoginViewModel @Inject constructor(private val auth : FirebaseAuth, private val repo: FirebaseRepo) : ViewModel(){
    private val _uiState = mutableStateOf<AuthUiState>(AuthUiState.Initial)
    val uiState: State<AuthUiState> = _uiState

    private val _currentUser = mutableStateOf<FirebaseUser?>(null)
    val currentUser: State<FirebaseUser?> = _currentUser

    init {
        auth.addAuthStateListener { firebaseAuth ->
            _currentUser.value = firebaseAuth.currentUser
        }
    }
    fun login( email : String, pass : String){
        _uiState.value = AuthUiState.Loading
        if (!IsValidEmail(email)){
            _uiState.value = AuthUiState.Error("Por favor ingresa un email válido")
            return
        }
        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {task ->
            if (task.isSuccessful){
                _uiState.value = AuthUiState.Success(auth.currentUser)
            } else {
                _uiState.value = AuthUiState.Error("Email o contraseña incorrecto")
            }
        }
    }

    private fun IsValidEmail(email: String): Boolean {
        val allowedChar = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}\$")
        return email.matches(allowedChar)
    }
    fun register(email: String, pass: String, username: String) {
        _uiState.value = AuthUiState.Loading
        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(username)
                    .build()

                user?.updateProfile(profileUpdates)?.addOnCompleteListener { task2 ->
                    if (task2.isSuccessful && user != null) {
                        val newUser = UsuarioFirebase(
                            id = user.uid,
                            name = username,
                            edad = 0,
                            peso = 0,
                            rutinas = listOf(),
                            nivelActividadFisica = 0,
                            isSelected = false
                        )
                        repo.insertOrUpdate(newUser)
                        _uiState.value = AuthUiState.Success(user)
                    } else {
                        _uiState.value = AuthUiState.Error(
                            task2.exception?.message
                                ?: "Se produjo un error al actualizar el perfil"
                        )
                    }
                }
            } else {
                _uiState.value = AuthUiState.Error(
                    task.exception?.message
                        ?: "Se produjo un error a la hora de crear el perfil"
                )
            }
        }
    }

    fun logout(){
        auth.signOut()
        _uiState.value = AuthUiState.Initial
    }
    fun fuckup(){
        _uiState.value = AuthUiState.Initial
    }
}
sealed class AuthUiState {
    object Initial : AuthUiState()
    object Loading : AuthUiState()
    data class Success(val user: FirebaseUser?) : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}