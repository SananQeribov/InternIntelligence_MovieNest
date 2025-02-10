package com.legalist.movienest.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class SignUpViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _signUpResult = MutableLiveData<Result<Boolean>>()
    val signUpResult: LiveData<Result<Boolean>> get() = _signUpResult

    fun signUp(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _signUpResult.value = Result.failure(Exception("Email or password cannot be empty"))
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _signUpResult.value = Result.success(true)
                } else {
                    _signUpResult.value = Result.failure(task.exception ?: Exception("SignUp failed"))
                }
            }
    }
}
