package com.scootin.view.fragment.home.orders

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.scootin.R
import com.scootin.databinding.FragmentPendingOrdersBinding
import com.scootin.network.AppExecutors
import com.scootin.network.api.Status
import com.scootin.network.response.UnAssignedOrderResponse
import com.scootin.util.fragment.autoCleared
import com.scootin.view.adapter.orders.PendingOrdersAdapter
import com.scootin.viewmodel.order.OrdersViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class PendingOrdersFragment:Fragment(R.layout.fragment_pending_orders) {
    private var binding by autoCleared<FragmentPendingOrdersBinding>()
    private val viewModel: OrdersViewModel by viewModels()

    @Inject
    lateinit var appExecutors: AppExecutors
    private lateinit var pendingOrdersAdapter: PendingOrdersAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPendingOrdersBinding.bind(view)

        setAdaper()

        setupListeners()
    }

    private fun setupListeners() {
        viewModel.getAllUnAssigned().observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    pendingOrdersAdapter.submitList(it.data)
                }
                Status.ERROR -> {

                }
                Status.LOADING -> {

                }
            }
        }
    }

    private fun setAdaper() {
        pendingOrdersAdapter =
            PendingOrdersAdapter(
                appExecutors,
                object : PendingOrdersAdapter.ItemAdapterClickLister {
                    override fun onItemSelected(item:UnAssignedOrderResponse) {
                        findNavController().navigate(PendingOrdersFragmentDirections.actionPendingOrdersFragmentToPendingOrderDetailsFragment(item.id))
                    }

                    override fun onHandwrittenListOrderSelected(item: UnAssignedOrderResponse) {
                        findNavController().navigate(PendingOrdersFragmentDirections.actionPendingOrdersFragmentToDirectOrderDetailsFragment(item.id))
                    }

                    override fun onCitywideOrderSelected(item: UnAssignedOrderResponse) {
                        findNavController().navigate(PendingOrdersFragmentDirections.actionPendingOrdersFragmentToCitywideOrderDetailsFragment(item.id))
                    }

                })

        binding.recyclerView.apply {
            adapter = pendingOrdersAdapter
        }
    }

}