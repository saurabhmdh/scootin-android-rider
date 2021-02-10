package com.scootin.view.fragment.home.orders

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.scootin.R
import com.scootin.databinding.FragmentDirectOrdersDetailsBinding
import com.scootin.network.AppExecutors
import com.scootin.network.api.Status
import com.scootin.network.response.Media
import com.scootin.util.Conversions
import com.scootin.util.constants.IntentConstants
import com.scootin.util.fragment.autoCleared
import com.scootin.view.adapter.orders.ExtraDataAdapter
import com.scootin.view.adapter.orders.PendingOrderDetailsItemAdapter
import com.scootin.viewmodel.order.OrdersViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.adapter_temple_item.view.*
import timber.log.Timber
import javax.inject.Inject
@AndroidEntryPoint
class CompletedDirectOrdersFragment : Fragment(R.layout.fragment_direct_orders_details) {
    private var binding by autoCleared<FragmentDirectOrdersDetailsBinding>()

    private val viewModel: OrdersViewModel by viewModels()

    @Inject
    lateinit var appExecutors: AppExecutors
    private var extraDataAdapter by autoCleared<ExtraDataAdapter>()
    private val args: DirectOrderDetailsFragmentArgs by navArgs()

    private var media: Media? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDirectOrdersDetailsBinding.bind(view)
        binding.lifecycleOwner = this
        enableOrDisableVisibility(true)
        setAdaper()
        binding.pendingIcon.setImageResource(R.drawable.ic_completed_icon)
         Timber.i("Order Detail is loading for element $args and bundle $savedInstanceState")

        viewModel.getDirectOrder(args.orderId).observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    Timber.i("Samridhi direct ${it.data}")
                    binding.data = it.data
                    media = it.data?.media
                    if (it.data?.deliveryDetails?.deliveredDateTime.isNullOrEmpty()) {
                        binding.txtDeliveryDate.visibility = View.GONE
                        binding.deliveryDate.visibility = View.GONE
                    } else {
                        binding.txtDeliveryDate.visibility = View.VISIBLE
                        binding.deliveryDate.visibility = View.VISIBLE
                        // binding.deliveryDate.text = it.data?.orderDetails?.deliveryDetails?.deliveredDateTime
                    }
                    if (it.data?.extraData.isNullOrEmpty().not()) {
                        val extra = Conversions.convertExtraData(it.data?.extraData)
                        Timber.i("Extra $extra")
                        extraDataAdapter.submitList(extra)
                    }
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

        setUpListener()
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

    private fun enableOrDisableVisibility(completed: Boolean) {
        if (completed) {
            binding.acceptButton.visibility = View.GONE
        }
    }
    private fun setAdaper() {
        extraDataAdapter = ExtraDataAdapter(appExecutors)

        binding.recycler.apply {
            adapter = extraDataAdapter
        }
    }
}