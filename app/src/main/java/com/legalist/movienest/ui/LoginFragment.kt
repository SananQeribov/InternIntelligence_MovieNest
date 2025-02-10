package com.legalist.movienest.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.legalist.data.util.Resource
import com.legalist.movienest.R
import com.legalist.movienest.base.BaseFragment
import com.legalist.movienest.databinding.FragmentLoginBinding
import com.legalist.movienest.viewmodel.LoginViewModel
import kotlinx.coroutines.launch

class LoginFragment : BaseFragment<FragmentLoginBinding>() {
    private val viewModel by viewModels<LoginViewModel>()

    override fun buildUi() {
        binding.navigateTo(binding.tvRegister, R.id.action_loginFragment_to_signUpFragment)

        binding.btnLogin.setOnClickListener {
            val email = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(context, "Email and password cannot be empty.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.login(email, password)
        }
        observeLogin()
    }

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentLoginBinding {
        return FragmentLoginBinding.inflate(inflater, container, false)
    }

    private fun observeLogin() {
        viewModel.loginResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Loading -> {

                    binding.btnLogin.isEnabled = false
                }

                is Resource.Success -> {
                    binding.btnLogin.isEnabled = true
                    binding.navigateTo(
                        binding.btnLogin,
                        R.id.action_loginFragment_to_boardingFragment
                    )
                    Toast.makeText(context, "Login successful!", Toast.LENGTH_SHORT).show()
                }

                is Resource.Error -> {
                    binding.btnLogin.isEnabled = true
                    Toast.makeText(context, result.message ?: "Login Failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
