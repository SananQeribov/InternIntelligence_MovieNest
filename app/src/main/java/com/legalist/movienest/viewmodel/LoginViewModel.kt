package com.legalist.movienest.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.legalist.data.util.Resource
import kotlinx.coroutines.launch


class LoginViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val _loginResult = MutableLiveData<Resource<String>>()
    val loginResult: LiveData<Resource<String>> get() = _loginResult


    fun login(email: String, password: String) {
        viewModelScope.launch {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                _loginResult.value = Resource.Loading()
                _loginResult.value = when {
                    task.isSuccessful -> Resource.Success("Login Successful")

                    else -> {
                        Resource.Error("Login failed")

                    }
                }

            }

        }


    }
}