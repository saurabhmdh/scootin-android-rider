package com.scootin.view.fragment.home.orders

import android.os.Bundle
import android.view.View
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
import timber.log.Timber

@AndroidEntryPoint
class PendingOrderDetailsFragment:Fragment(R.layout.fragment_pending_order_details) {
    private var binding by autoCleared<FragmentPendingOrderDetailsBinding>()

    private val viewModel: OrdersViewModel by viewModels()

    @Inject
    lateinit var appExecutors: AppExecutors
    private var pendingOrdersAdapter by autoCleared<PendingOrderDetailsItemAdapter>()
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