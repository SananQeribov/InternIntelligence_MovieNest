package com.legalist.movienest.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.legalist.movienest.R
import com.legalist.movienest.base.BaseFragment
import com.legalist.movienest.databinding.FragmentSplashBinding
import com.legalist.movienest.viewmodel.SplashViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SplashFragment : BaseFragment<FragmentSplashBinding>() {
    private val viewModel by viewModels<SplashViewModel>()
    override fun buildUi() {
        viewModel.navigateToSplash()
        lifecycleScope.launch {
            viewModel.splash.collectLatest { navigate ->
                if (navigate) {
                    findNavController().navigate(R.id.action_splashFragment_to_signUpFragment)
                }
            }
        }
    }

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSplashBinding {
        return FragmentSplashBinding.inflate(inflater, container, false)

    }
}
