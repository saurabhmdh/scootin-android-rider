package com.scootin.view.fragment.home.orders

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.scootin.R
import com.scootin.databinding.FragmentCitywideOrderDetailsBinding
import com.scootin.network.AppExecutors
import com.scootin.network.api.Status
import com.scootin.network.manager.AppHeaders
import com.scootin.network.request.RequestOrderAcceptedByRider
import com.scootin.network.response.Media
import com.scootin.util.OrderType
import com.scootin.util.constants.IntentConstants
import com.scootin.util.fragment.autoCleared
import com.scootin.view.fragment.home.BaseFragment
import com.scootin.viewmodel.order.OrdersViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class CityWideOrderDetailsFragment:BaseFragment(R.layout.fragment_citywide_order_details) {

    private var binding by autoCleared<FragmentCitywideOrderDetailsBinding>()

    private val viewModel: OrdersViewModel by viewModels()

    @Inject
    lateinit var appExecutors: AppExecutors

    private val args: CityWideOrderDetailsFragmentArgs by navArgs()

    private var media: Media? = null

    private val orderId by lazy {
        args.orderId
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCitywideOrderDetailsBinding.bind(view)
        binding.lifecycleOwner = this
        binding.pendingIcon.setImageResource(R.drawable.ic_pending_icon)
        setUpListener()
        // Timber.i("Order Detail is loading for element $args and bundle $savedInstanceState")

        viewModel.getCitywideOrder(args.orderId).observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    media = it.data?.media
                    binding.data = it.data
                    if(it.data?.message!=null){
                        binding.instructionTxt.visibility=View.VISIBLE
                        binding.instructionTxt.text="Additional Instruction/Remark: "+it.data.message
                    }
                }
                Status.ERROR -> {

                }
                Status.LOADING -> {

                }
            }
        }

        //Lets think for acc
        binding.acceptButton.setOnClickListener {
            acceptOrder()
            //Incase of pack
            //findNavController().navigate(CityWideOrderDetailsFragmentDirections.citywideOrderFragmentToAcceptOrder(orderId))
        }
    }

    private fun acceptOrder() {
        showLoading()
        viewModel.acceptCityWideOrder(
            AppHeaders.userID, orderId.toString(), RequestOrderAcceptedByRider(
                OrderType.CITYWIDE.name, true)
        ).observe(viewLifecycleOwner) {
            Timber.i("Saurabh Citywide acceptOrder status = ${it.status}")
            when(it.status) {
                Status.SUCCESS -> {
                    Timber.i("Saurabh Citywide acceptOrder sss")
                    dismissLoading()
                    Toast.makeText(requireContext(), "Order Accepted", Toast.LENGTH_LONG).show()
                    enableOrDisableVisibility(true)
                    findNavController().popBackStack()
                }
                Status.ERROR -> {
                    Timber.i("Saurabh Citywide acceptOrder error..")
                    dismissLoading()
                    Toast.makeText(requireContext(), "This Order has been Accepted by other rider", Toast.LENGTH_LONG).show()
                    enableOrDisableVisibility(true)
                }
                Status.LOADING -> {

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

        binding.pickupAddressLine1.setOnClickListener {
            val address = binding.pickupAddressLine1.text?.toString()
            if (address.isNullOrEmpty().not()) {
                IntentConstants.moveToMapWithDirection(requireContext(), address!!)
            }
        }

        binding.pickupTelephone.setOnClickListener {
            val mobileNumber = binding.pickupTelephone.text?.toString()
            if (mobileNumber.isNullOrEmpty().not()) {
                IntentConstants.makeCall(requireContext(), mobileNumber!!)
            }
        }

        binding.deliveryTelephone.setOnClickListener {
            val mobileNumber = binding.deliveryTelephone.text?.toString()
            if (mobileNumber.isNullOrEmpty().not()) {
                IntentConstants.makeCall(requireContext(), mobileNumber!!)
            }
        }

        binding.imageMedia.setOnClickListener {
            launchGallery()
        }
    }

    private fun launchGallery() {
        Timber.i("launchGallery with media $media")
        media?.let {
            findNavController().navigate(CityWideOrderDetailsFragmentDirections.citywideOrderFragmentToImageGallery(it))
        }
    }

    private fun enableOrDisableVisibility(completed: Boolean) {
        if (completed) {
            binding.acceptButton.visibility = View.GONE
        }
    }
}