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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.scootin.network.api.Status
import com.scootin.network.manager.AppHeaders
import com.scootin.network.request.RequestOrderAcceptedByRider
import com.scootin.util.OrderType
import com.scootin.util.constants.IntentConstants
import com.scootin.view.fragment.home.BaseFragment
import com.scootin.viewmodel.home.LoginViewModel
import timber.log.Timber

@AndroidEntryPoint
class PendingOrderDetailsFragment: BaseFragment(R.layout.fragment_pending_order_details) {
    private var binding by autoCleared<FragmentPendingOrderDetailsBinding>()

    private val viewModel: OrdersViewModel by viewModels()

    @Inject
    lateinit var appExecutors: AppExecutors
    private var pendingOrdersAdapter by autoCleared<PendingOrderDetailsItemAdapter>()

    private val args: PendingOrderDetailsFragmentArgs by navArgs()

    private val orderId by lazy {
        args.orderId
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPendingOrderDetailsBinding.bind(view)
        binding.pendingIcon.setImageResource(R.drawable.ic_pending_icon)
        binding.lifecycleOwner = this

        setAdaper()
        setUpListener()

        Timber.i("Order Detail is loading for element $args and bundle $savedInstanceState")
        viewModel.getNormalOrder(orderId).observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    Timber.i("Samridhi ${it.data}")
                    binding.data = it.data
                    pendingOrdersAdapter.submitList(it.data?.orderInventoryDetailsList)
                    //Enable and disable accept button
                    enableOrDisableVisibility(it.data?.orderDetails?.deliveryDetails != null)
                }
                Status.ERROR -> {

                }
                Status.LOADING -> {

                }
            }
        }

        binding.acceptButton.setOnClickListener {
            showLoading()
            viewModel.acceptOrder(AppHeaders.userID, orderId.toString(), RequestOrderAcceptedByRider(OrderType.NORMAL.name, true)).observe(viewLifecycleOwner) {
                when(it.status) {
                    Status.SUCCESS -> {
                        dismissLoading()
                        Toast.makeText(requireContext(), "Order Accepted", Toast.LENGTH_LONG).show()
                        enableOrDisableVisibility(true)
                        findNavController().popBackStack()
                    }
                    Status.ERROR -> {
                        dismissLoading()
                        Toast.makeText(requireContext(), "This Order has been Accepted by other rider", Toast.LENGTH_LONG).show()
                        enableOrDisableVisibility(true)
                    }
                    Status.LOADING -> {

                    }
                }

            }
        }
    }
    private fun setUpListener() {
        binding.deliveryAddressLine1.setOnClickListener {
            val address = binding.deliveryAddressLine1.text?.toString()
            if (address.isNullOrEmpty().not()) {
                IntentConstants.moveToMapWithDirection(requireContext(), address!!)
            }
        }
        binding.telephone.setOnClickListener {
            val mobileNumber = binding.telephone.text?.toString()
            if (mobileNumber.isNullOrEmpty().not()) {
                IntentConstants.makeCall(requireContext(), mobileNumber!!)
            }
        }
    }


    private fun setAdaper() {
        pendingOrdersAdapter = PendingOrderDetailsItemAdapter(appExecutors)

        binding.recyclerView.apply {
            adapter = pendingOrdersAdapter
        }
    }

    private fun enableOrDisableVisibility(completed: Boolean) {
        if (completed) {
            binding.acceptButton.visibility = View.GONE
        }
    }

}