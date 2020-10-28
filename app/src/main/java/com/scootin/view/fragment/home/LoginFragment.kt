package com.scootin.view.fragment.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.scootin.R
import com.scootin.databinding.FragmentAcceptOrderDetailsBinding
import com.scootin.databinding.FragmentLoginBinding
import com.scootin.network.AppExecutors
import com.scootin.network.response.PendingOrderItemList
import com.scootin.util.fragment.autoCleared
import com.scootin.view.activity.MainActivity
import com.scootin.view.adapter.orders.PendingOrderDetailsItemAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class LoginFragment :Fragment(R.layout.fragment_login) {
    private var binding by autoCleared<FragmentLoginBinding>()

    @Inject
    lateinit var appExecutors: AppExecutors

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)
       binding.btnLogin.setOnClickListener {
           gotoHomePage()
       }

    }

    private fun gotoHomePage() {
        startActivity(Intent(requireContext(), MainActivity::class.java))
        activity?.overridePendingTransition(R.anim.enter, R.anim.exit)
        activity?.finish()
    }
}