package com.scootin.view.fragment.home.orders

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.scootin.R
import com.scootin.databinding.FragmentAcceptedDirectOrdersDetailsBinding
import com.scootin.databinding.FragmentDirectOrdersDetailsBinding
import com.scootin.network.AppExecutors
import com.scootin.network.api.Status
import com.scootin.network.request.RequestOrderAcceptedByRider
import com.scootin.network.response.Media
import com.scootin.util.Conversions
import com.scootin.util.OrderType
import com.scootin.util.constants.IntentConstants
import com.scootin.util.fragment.autoCleared
import com.scootin.view.adapter.orders.ExtraDataAdapter
import com.scootin.view.fragment.home.BaseFragment
import com.scootin.viewmodel.order.OrdersViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class AcceptedDirectOrdersFragment: BaseFragment (R.layout.fragment_accepted_direct_orders_details) {
    private var binding by autoCleared<FragmentAcceptedDirectOrdersDetailsBinding>()

    private val viewModel: OrdersViewModel by viewModels()

    @Inject
    lateinit var appExecutors: AppExecutors
    private var extraDataAdapter by autoCleared<ExtraDataAdapter>()
    private val args: DirectOrderDetailsFragmentArgs by navArgs()

    private var media: Media? = null

    private val orderId by lazy {
        args.orderId
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAcceptedDirectOrdersDetailsBinding.bind(view)
        binding.lifecycleOwner = this
        setAdaper()
        setUpListener()

        binding.pendingIcon.setImageResource(R.drawable.ic_accepted_icon)
        // Timber.i("Order Detail is loading for element $args and bundle $savedInstanceState")

        viewModel.doDirectOrder(orderId)

        viewModel.loadDirectOrder.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    Timber.i("Samridhi direct ${it.data}")
                    binding.data = it.data
                    media = it.data?.media
                    if (it.data?.extraData.isNullOrEmpty().not()) {
                        val extra = Conversions.convertExtraData(it.data?.extraData)
                        Timber.i("Extra $extra")
                        extraDataAdapter.submitList(extra)
                    }
                    updateButtonVisibility(it.data?.orderStatus)
                }
                Status.ERROR -> {

                }
                Status.LOADING -> {

                }
            }
        }

        setupDeliveryListener()
        setupPickupListener()
        binding.imageMedia.setOnClickListener {
            launchGallery()
        }
    }

    private fun updateButtonVisibility(orderStatus: String?) {
        when (orderStatus) {
            "DISPATCHED" -> {
                binding.pickupButton.visibility = View.GONE
                binding.deliveredButton.visibility = View.VISIBLE
            }
            else -> {
                binding.pickupButton.visibility = View.VISIBLE
                binding.deliveredButton.visibility = View.GONE
            }
        }
    }

    private fun launchGallery() {
        Timber.i("launchGallery with media $media")
        media?.let {
            findNavController().navigate(AcceptedDirectOrdersFragmentDirections.directOrderFragmentToImageGallery(it))
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
        extraDataAdapter = ExtraDataAdapter(appExecutors)

        binding.recycler.apply {
            adapter = extraDataAdapter
        }
    }

    private fun setupDeliveryListener() {
        binding.deliveredButton.setOnClickListener {
            showLoading()
            viewModel.deliverOrder(orderId.toString(), RequestOrderAcceptedByRider(OrderType.DIRECT.name, true)).observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        dismissLoading()
                        Toast.makeText(requireContext(), "Order has been delivered to customer", Toast.LENGTH_SHORT).show()
                        binding.deliveredButton.visibility = View.GONE
                        findNavController().popBackStack()
                    }
                    Status.ERROR -> {
                        dismissLoading()
                        Toast.makeText(requireContext(), "Server error", Toast.LENGTH_SHORT).show()
                    }
                    Status.LOADING -> { }
                }
            }
        }
    }

    private fun setupPickupListener() {
        binding.pickupButton.setOnClickListener {
            showLoading()
            viewModel.pickupOrder(orderId.toString(), RequestOrderAcceptedByRider(OrderType.DIRECT.name, true)).observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        dismissLoading()
                        Toast.makeText(requireContext(), "Order has been pickup", Toast.LENGTH_SHORT).show()
                        binding.pickupButton.visibility = View.GONE
                        viewModel.doDirectOrder(orderId)
                    }
                    Status.ERROR -> {
                        dismissLoading()
                        Toast.makeText(requireContext(), "Server error", Toast.LENGTH_SHORT).show()
                    }
                    Status.LOADING -> { }
                }
            }
        }
    }
}