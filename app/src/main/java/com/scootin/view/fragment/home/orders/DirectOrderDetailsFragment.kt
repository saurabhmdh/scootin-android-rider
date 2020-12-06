package com.scootin.view.fragment.home.orders

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.scootin.R
import com.scootin.databinding.FragmentDirectOrdersDetailsBinding
import com.scootin.network.AppExecutors
import com.scootin.network.api.Status
import com.scootin.network.manager.AppHeaders
import com.scootin.network.request.RequestOrderAcceptedByRider
import com.scootin.network.response.Media
import com.scootin.util.Conversions
import com.scootin.util.OrderType
import com.scootin.util.constants.IntentConstants
import com.scootin.util.fragment.autoCleared
import com.scootin.view.adapter.orders.ExtraDataAdapter
import com.scootin.view.adapter.orders.PendingOrderDetailsItemAdapter
import com.scootin.view.fragment.home.BaseFragment
import com.scootin.viewmodel.order.OrdersViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class DirectOrderDetailsFragment:BaseFragment(R.layout.fragment_direct_orders_details) {
    private var binding by autoCleared<FragmentDirectOrdersDetailsBinding>()

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
        binding = FragmentDirectOrdersDetailsBinding.bind(view)
        binding.lifecycleOwner = this
        binding.pendingIcon.setImageResource(R.drawable.ic_pending_icon)
        setAdaper()
        setUpListener()
       // Timber.i("Order Detail is loading for element $args and bundle $savedInstanceState")

        viewModel.getDirectOrder(args.orderId).observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    Timber.i("Samridhi direct ${it.data?.extraData} ")
                    media = it.data?.media
                    if (it.data?.extraData.isNullOrEmpty().not()) {
                        Timber.i("we have data ${it.data?.extraData}")
                        val extra = Conversions.convertExtraData(it.data?.extraData)
                        Timber.i("Extra $extra")
                        extraDataAdapter.submitList(extra)

                    }
                    binding.data = it.data
                    if(media==null){
                        binding.imageMedia.setVisibility(View.GONE)
                    }

                }
                Status.ERROR -> {

                }
                Status.LOADING -> {

                }
            }
        }

        binding.acceptButton.setOnClickListener {
            showLoading()
            viewModel.acceptOrder(
                AppHeaders.userID, orderId.toString(), RequestOrderAcceptedByRider(
                    OrderType.DIRECT.name, true)
            ).observe(viewLifecycleOwner) {
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
        binding.imageMedia.setOnClickListener {
            launchGallery()
        }
    }

    private fun launchGallery() {
        Timber.i("launchGallery with media $media")
        media?.let {
            findNavController().navigate(AcceptedDirectOrdersFragmentDirections.directOrderFragmentToImageGallery(it))
        }
    }

    private fun setAdaper() {
        extraDataAdapter = ExtraDataAdapter(appExecutors)

        binding.recycler.apply {
            adapter = extraDataAdapter
        }
    }

    private fun enableOrDisableVisibility(completed: Boolean) {
        if (completed) {
            binding.acceptButton.visibility = View.GONE
        }
    }

}