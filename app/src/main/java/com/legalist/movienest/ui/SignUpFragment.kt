package com.legalist.movienest.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.legalist.movienest.R
import com.legalist.movienest.base.BaseFragment
import com.legalist.movienest.databinding.FragmentSignUpBinding
import com.legalist.movienest.viewmodel.SignUpViewModel

class SignUpFragment : BaseFragment<FragmentSignUpBinding>() {
    private val signUpViewModel: SignUpViewModel by viewModels()

    private lateinit var mAuth: FirebaseAuth

    override fun buildUi() {

       binding.navigateTo(binding.tvLogin,R.id.action_signUpFragment_to_loginFragment)
        binding.btnSignUp.setOnClickListener {
            signUpViewModel.signUp(binding.etEmail.text.toString(), binding.etPassword.text.toString())

        }
        observeEven()
    }

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSignUpBinding {
        return FragmentSignUpBinding.inflate(inflater, container, false)
    }

    private fun observeEven() {
        signUpViewModel.signUpResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                Toast.makeText(requireContext(), "SignUp succeeded", Toast.LENGTH_SHORT).show()
              binding.navigateTo(binding.tvLogin,R.id.action_signUpFragment_to_boardingFragment)// Navigate to login fragment
            }.onFailure {
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
