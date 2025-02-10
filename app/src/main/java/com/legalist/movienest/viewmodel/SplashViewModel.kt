package com.legalist.movienest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SplashViewModel:ViewModel() {

    private val  _splash = MutableStateFlow(false)
    var splash = _splash.asStateFlow()

    fun navigateToSplash(){

        viewModelScope.launch{
          delay(5000)
         _splash.value = true

        }

    }
}