package com.ulatina.proyectoprogra5.viewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.ulatina.proyectoprogra5.data.database.di.FirebaseModule

@HiltViewModel
class LoginViewModel @Inject constructor(private val auth : FirebaseAuth) : ViewModel(){
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
                _uiState.value = AuthUiState.Error("Ha ocurrido un error durante el proceso de ingreso")
            }
        }
    }

    private fun IsValidEmail(email: String): Boolean {
        val allowedChar = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\n"
        return email.matches(allowedChar.toRegex())
    }
    fun register(email: String, pass: String, username: String){
        _uiState.value = AuthUiState.Loading
        auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener{task->
            if (task.isSuccessful){
                val profileUpdates = UserProfileChangeRequest.Builder().setDisplayName(username).build()
                val user = auth.currentUser
                user?.updateProfile(profileUpdates)?.addOnCompleteListener{task2->
                    if(task2.isSuccessful){
                        _uiState.value = AuthUiState.Success(user)
                    } else {
                        _uiState.value = AuthUiState.Error(task2.exception?.message ?:"Se produjo un error a la hora de crear el perfil")
                    }
                }
            } else { _uiState.value = AuthUiState.Error(task.exception?.message ?: "Se produjo un error a la hora de crear el perfil")}
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