package com.scootin.view.fragment.home.orders

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.scootin.R
import com.scootin.databinding.FragmentCompletedOrdersBinding
import com.scootin.databinding.FragmentPendingOrdersBinding
import com.scootin.network.AppExecutors
import com.scootin.network.api.Status
import com.scootin.network.manager.AppHeaders
import com.scootin.network.response.OrderListResponse
import com.scootin.network.response.PendingOrdersList
import com.scootin.util.fragment.autoCleared
import com.scootin.view.adapter.orders.CompletedOrdersAdapter
import com.scootin.view.adapter.orders.PendingOrdersAdapter
import com.scootin.viewmodel.order.OrdersViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
@AndroidEntryPoint
class CompletedOrdersFragment:Fragment(R.layout.fragment_completed_orders) {
    private var binding by autoCleared<FragmentCompletedOrdersBinding>()
    private val viewModel: OrdersViewModel by viewModels()

    @Inject
    lateinit var appExecutors: AppExecutors
    private lateinit var completedOrdersAdapter: CompletedOrdersAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCompletedOrdersBinding.bind(view)
        setAdaper()

        setupListeners()
    }

    private fun setAdaper() {
        completedOrdersAdapter =
            CompletedOrdersAdapter(
                appExecutors,
                object : CompletedOrdersAdapter.ItemAdapterClickLister {
                    override fun onItemSelected(view: OrderListResponse) {

                    findNavController().navigate(CompletedOrdersFragmentDirections.actionCompletedOrdersFragmentToPendingOrderDetailsFragment(view.orderId))
                }
                    override fun onHandwrittenListOrderSelected(view: OrderListResponse) {
                        findNavController().navigate(CompletedOrdersFragmentDirections.actionCompletedOrdersFragmentToDirectOrderDetailsFragment(view.orderId))
                    }

                    override fun onCitywideOrderSelected(view: OrderListResponse) {
                        findNavController().navigate(CompletedOrdersFragmentDirections.actionCompletedOrdersFragmentToCityWideOrderDetailsFragment(view.orderId))
                    }

                })

        binding.recyclerView.apply {
            adapter = completedOrdersAdapter
        }
    }

    private fun setupListeners() {
        viewModel.getCompletedOrders(AppHeaders.userID).observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                completedOrdersAdapter.submitData(it)
            }
        }
    }


}