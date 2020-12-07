package com.scootin.view.fragment.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.scootin.R
import com.scootin.databinding.FragmentLoginBinding
import com.scootin.network.AppExecutors
import com.scootin.network.api.Status
import com.scootin.network.manager.AppHeaders
import com.scootin.util.fragment.autoCleared
import com.scootin.view.activity.MainActivity
import com.scootin.viewmodel.home.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import androidx.lifecycle.observe

@AndroidEntryPoint
class LoginFragment: Fragment(R.layout.fragment_login) {
    private var binding by autoCleared<FragmentLoginBinding>()

    private val viewModel: LoginViewModel by viewModels()

    @Inject
    lateinit var appExecutors: AppExecutors

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)


        viewModel.loginComplete.observe(viewLifecycleOwner) {networkResponse ->
            when (networkResponse?.status) {
                Status.LOADING -> {
                    //TODO: Showing loading..
                }
                Status.ERROR -> {
                    Toast.makeText(requireContext(), "User name or password is wrong", Toast.LENGTH_LONG).show()
                }
                Status.SUCCESS -> {
                    networkResponse.data?.let {
                        viewModel.saveUserInfo(it)
                        AppHeaders.updateUserData(it)
                        gotoHomePage()
                    }
                }
            }
        }

        binding.btnSignIn.setOnClickListener {
            if (binding.userNumber.text.isNullOrEmpty() ||  binding.passwordText.text.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Please enter valid input", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.doLogin(binding.userNumber.text.toString(), binding.passwordText.text.toString())
        }

    }

    private fun gotoHomePage() {
        startActivity(Intent(requireContext(), MainActivity::class.java))
        activity?.overridePendingTransition(R.anim.enter, R.anim.exit)
        activity?.finish()
    }
}