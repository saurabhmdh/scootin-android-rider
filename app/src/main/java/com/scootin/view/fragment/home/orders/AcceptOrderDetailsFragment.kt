package com.scootin.view.fragment.home.orders

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.scootin.R
import com.scootin.databinding.FragmentAcceptedOrderDetailsBinding
import com.scootin.network.AppExecutors
import com.scootin.network.api.Status
import com.scootin.network.request.RequestOrderAcceptedByRider
import com.scootin.util.OrderType
import com.scootin.util.constants.IntentConstants
import com.scootin.util.fragment.autoCleared
import com.scootin.view.adapter.orders.PendingOrderDetailsItemAdapter
import com.scootin.view.fragment.home.BaseFragment
import com.scootin.viewmodel.order.OrdersViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject
@AndroidEntryPoint
class AcceptOrderDetailsFragment: BaseFragment(R.layout.fragment_accepted_order_details) {
    private var binding by autoCleared<FragmentAcceptedOrderDetailsBinding>()

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
        binding = FragmentAcceptedOrderDetailsBinding.bind(view)
        binding.lifecycleOwner = this

        binding.pendingIcon.setImageResource(R.drawable.ic_accepted_icon)
        setAdaper()
        setUpListener()
        Timber.i("Order Detail is loading for element $args and bundle $savedInstanceState")


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

        viewModel.getNormalOrder(orderId).observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    Timber.i("Samridhi ${it.data}")
                    binding.data = it.data
                    pendingOrdersAdapter.submitList(it.data?.orderInventoryDetailsList)
                }
                Status.ERROR -> { }
                Status.LOADING -> { }
            }
        }

        setupPickupListener()
    }

    private fun setupPickupListener() {
        binding.acceptButton.setOnClickListener {
            showLoading()
            viewModel.deliverOrder(orderId.toString(), RequestOrderAcceptedByRider(OrderType.NORMAL.name, true)).observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        dismissLoading()
                        Toast.makeText(requireContext(), "Order has been delivered to customer", Toast.LENGTH_SHORT).show()
                        binding.acceptButton.visibility = View.GONE
                        findNavController().popBackStack()
                    }
                    Status.ERROR -> {
                        dismissLoading()
                        Toast.makeText(requireContext(), "Server error", Toast.LENGTH_SHORT).show()
                    }
                    Status.LOADING -> {

                    }
                }
            }
        }
    }

    private fun setAdaper() {
        pendingOrdersAdapter = PendingOrderDetailsItemAdapter(appExecutors)

        binding.recyclerView.apply {
            adapter = pendingOrdersAdapter
        }
    }

}