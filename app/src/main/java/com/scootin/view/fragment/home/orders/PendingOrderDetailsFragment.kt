package com.scootin.view.fragment.home.orders

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.scootin.R
import com.scootin.databinding.FragmentPendingOrderDetailsBinding

import com.scootin.network.AppExecutors
import com.scootin.util.fragment.autoCleared
import com.scootin.view.adapter.orders.PendingOrderDetailsItemAdapter
import com.scootin.viewmodel.order.OrdersViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import androidx.lifecycle.observe
import androidx.navigation.fragment.navArgs
import com.scootin.network.api.Status
import com.scootin.network.manager.AppHeaders
import com.scootin.viewmodel.home.LoginViewModel
import timber.log.Timber

@AndroidEntryPoint
class PendingOrderDetailsFragment:Fragment(R.layout.fragment_pending_order_details) {
    private var binding by autoCleared<FragmentPendingOrderDetailsBinding>()

    private val viewModel: OrdersViewModel by viewModels()

    @Inject
    lateinit var appExecutors: AppExecutors
    private var pendingOrdersAdapter by autoCleared<PendingOrderDetailsItemAdapter>()
    private val viewModel2: LoginViewModel by viewModels()
    private val args: PendingOrderDetailsFragmentArgs by navArgs()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPendingOrderDetailsBinding.bind(view)
        binding.pendingIcon.setImageResource(R.drawable.ic_pending_icon)
        binding.lifecycleOwner = this

        setAdaper()
        Timber.i("Order Detail is loading for element $args and bundle $savedInstanceState")
        viewModel.getNormalOrder(args.orderId).observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    Timber.i("Samridhi ${it.data}")
                    binding.data = it.data
                    pendingOrdersAdapter.submitList(it.data?.orderInventoryDetailsList)

                }
                Status.ERROR -> {

                }
                Status.LOADING -> {

                }
            }
        }
        viewModel2.loginComplete.observe(viewLifecycleOwner) {networkResponse ->
            when (networkResponse?.status) {
                Status.LOADING -> {
                    //TODO: Showing loading..
                }
                Status.ERROR -> {
                    Toast.makeText(requireContext(), "User name or password is wrong", Toast.LENGTH_LONG).show()
                }
                Status.SUCCESS -> {
                    networkResponse.data?.let {
                        viewModel2.saveUserInfo(it)
                        AppHeaders.updateUserData(it)
                    }
                }
            }
        }
        binding.btnAcceptOrder.setOnClickListener {
            //viewModel2.doLogin(binding.txtOrderId.text.toString(), binding.txtItemList.text.toString())
            binding.btnAcceptOrder.setVisibility(View.INVISIBLE)
            Toast.makeText(requireContext(), "Order Accepted", Toast.LENGTH_LONG).show()
        }



    }

//    private fun setupListeners() {
//
//    }


    private fun setAdaper() {
        pendingOrdersAdapter = PendingOrderDetailsItemAdapter(appExecutors)

        binding.recyclerView.apply {
            adapter = pendingOrdersAdapter
        }
    }

}