package com.scootin.view.fragment.home.orders

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.scootin.R
import com.scootin.databinding.FragmentAcceptOrdersBinding
import com.scootin.databinding.FragmentCompletedOrdersBinding
import com.scootin.network.AppExecutors
import com.scootin.network.api.Status
import com.scootin.network.manager.AppHeaders
import com.scootin.network.response.PendingOrdersList
import com.scootin.util.fragment.autoCleared
import com.scootin.view.adapter.orders.AcceptOrderAdapter
import com.scootin.view.adapter.orders.CompletedOrdersAdapter
import com.scootin.viewmodel.order.OrdersViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject
@AndroidEntryPoint
class AcceptOrdersFragment:Fragment(R.layout.fragment_accept_orders) {
    private var binding by autoCleared<FragmentAcceptOrdersBinding>()
    private val viewModel: OrdersViewModel by viewModels()

    @Inject
    lateinit var appExecutors: AppExecutors
    private lateinit var completedOrdersAdapter: AcceptOrderAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAcceptOrdersBinding.bind(view)
        setAdaper()
        setupListeners()

    }

    private fun setAdaper() {
        completedOrdersAdapter =
            AcceptOrderAdapter(
                appExecutors,
                object : AcceptOrderAdapter.ItemAdapterClickLister {
                    override fun onItemSelected(view: View) {
                        findNavController().navigate(AcceptOrdersFragmentDirections.actionAcceptOrdersFragmentToAcceptOrderDetailsFragment())
                    }

                })

        binding.recyclerView.apply {
            adapter = completedOrdersAdapter
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