package com.scootin.view.fragment.home.orders

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.scootin.R
import com.scootin.databinding.FragmentAcceptOrdersBinding

import com.scootin.network.AppExecutors
import com.scootin.network.api.Status
import com.scootin.network.manager.AppHeaders

import com.scootin.util.fragment.autoCleared
import com.scootin.view.adapter.orders.AcceptOrderAdapter
import com.scootin.viewmodel.order.OrdersViewModel

import com.scootin.viewmodel.orders.AcceptedOrderViewModel

import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject
@AndroidEntryPoint
class AcceptOrdersFragment:Fragment(R.layout.fragment_accept_orders) {
    private var binding by autoCleared<FragmentAcceptOrdersBinding>()
    private val viewModel: OrdersViewModel by viewModels()

    @Inject
    lateinit var appExecutors: AppExecutors
    private lateinit var acceptOrderAdapter: AcceptOrderAdapter
    private val viewModel: AcceptedOrderViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAcceptOrdersBinding.bind(view)
        setAdaper()

        setupListeners()
    }

    private fun setAdaper() {
        acceptOrderAdapter =
            AcceptOrderAdapter(
                appExecutors,
                object : AcceptOrderAdapter.ItemAdapterClickLister {
                    override fun onItemSelected(view: View) {
                        findNavController().navigate(AcceptOrdersFragmentDirections.actionAcceptOrdersFragmentToAcceptOrderDetailsFragment())
                    }

                })

        binding.recyclerView.apply {
            adapter = acceptOrderAdapter
        }
    }

    private fun setupListeners() {
        Timber.i("Saurabh Rider Id? ${AppHeaders.userID}")
        viewModel.getAcceptedOrders(AppHeaders.userID).observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    completedOrdersAdapter.submitList(it.data)
                }
                Status.ERROR -> {

                }
                Status.LOADING -> {

                }
            }
        }
    }


}