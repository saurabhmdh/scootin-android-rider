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
import com.scootin.util.constants.Validation
import timber.log.Timber

@AndroidEntryPoint
class LoginFragment: BaseFragment(R.layout.fragment_login) {
    private var binding by autoCleared<FragmentLoginBinding>()

    private val viewModel: LoginViewModel by viewModels()

    @Inject
    lateinit var appExecutors: AppExecutors

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)

        viewModel.loginComplete.observe(viewLifecycleOwner) { networkResponse ->
            when (networkResponse?.status) {
                Status.LOADING -> {
                    showLoading()
                }
                Status.ERROR -> {
                    dismissLoading()
                    Toast.makeText(context, "Mobile number or otp is incorrect.", Toast.LENGTH_SHORT).show()
                }
                Status.SUCCESS -> {
                    dismissLoading()
                    Timber.i("Successful response ${networkResponse.data}")
                    networkResponse.data?.let {
                        viewModel.saveUserInfo(it)
                        AppHeaders.updateUserData(it)
                        openHomeScreen()
                    }
                }
            }
        }

        viewModel.requestOTPComplete.observe(viewLifecycleOwner){ networkResponse ->
            when (networkResponse?.status) {
                Status.LOADING -> {
                    showLoading()
                }
                Status.ERROR -> {
                    dismissLoading()
                    Toast.makeText(context, R.string.server_error, Toast.LENGTH_SHORT).show()
                }
                Status.SUCCESS -> {
                    dismissLoading()
                    Toast.makeText(context, R.string.success_message_otp, Toast.LENGTH_SHORT).show()
                    binding.sendOtp.visibility = View.GONE
                }
            }
        }

        binding.signIn.setOnClickListener {
            val mobileNumber = binding.editTextPhnNo.text?.toString()
            val otp = binding.editTextOTP.text?.toString()

            if (mobileNumber.isNullOrEmpty() || Validation.REGEX_VALID_MOBILE_NUMBER.matcher(mobileNumber).matches().not()) {
                Toast.makeText(context, R.string.error_message_invalid_mobile, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (otp.isNullOrEmpty() || Validation.REGEX_VALID_OTP.matcher(otp).matches().not()) {
                Toast.makeText(context, R.string.error_message_invalid_otp, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.doLogin(mobileNumber, otp)

        }

        binding.sendOtp.setOnClickListener {
            val mobileNumber = binding.editTextPhnNo.text?.toString()

            if (mobileNumber.isNullOrEmpty() || Validation.REGEX_VALID_MOBILE_NUMBER.matcher(mobileNumber).matches().not()) {
                Toast.makeText(context, R.string.error_message_invalid_mobile, Toast.LENGTH_SHORT).show()
            } else {
                viewModel.sendOTP(mobileNumber)
            }
        }

        binding.senAgainLoginOtp.setOnClickListener {
            val mobileNumber = binding.editTextPhnNo.text?.toString()
            if (mobileNumber.isNullOrEmpty() || Validation.REGEX_VALID_MOBILE_NUMBER.matcher(mobileNumber).matches().not()) {
                Toast.makeText(context, R.string.error_message_invalid_mobile, Toast.LENGTH_SHORT).show()
            } else {
                viewModel.sendOTP(mobileNumber)
            }
        }
    }

    private fun openHomeScreen() {
        startActivity(Intent(requireContext(), MainActivity::class.java))
        activity?.overridePendingTransition(R.anim.enter, R.anim.exit)
        activity?.finish()
    }


}