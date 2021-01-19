package com.scootin.view.fragment.home.orders

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.scootin.R
import com.scootin.databinding.FragmentAcceptedCitywideOrdersBinding
import com.scootin.network.AppExecutors
import com.scootin.network.api.Status
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
class AcceptedCityWideOrdersFragment : BaseFragment(R.layout.fragment_accepted_citywide_orders) {
    private var binding by autoCleared<FragmentAcceptedCitywideOrdersBinding>()

    private val viewModel: OrdersViewModel by viewModels()

    @Inject
    lateinit var appExecutors: AppExecutors
    private val args: DirectOrderDetailsFragmentArgs by navArgs()

    private var media: Media? = null

    private val orderId by lazy {
        args.orderId
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAcceptedCitywideOrdersBinding.bind(view)
        binding.lifecycleOwner = this
        setUpListener()

        binding.pendingIcon.setImageResource(R.drawable.ic_accepted_icon)
        // Timber.i("Order Detail is loading for element $args and bundle $savedInstanceState")

        viewModel.doDirectOrder(orderId)

        viewModel.getCitywideOrder(args.orderId).observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    Timber.i("Samridhi direct ${it.data}")
                    binding.data = it.data
                    media = it.data?.media
                    setupDeliveryListener(it.data?.paymentDetails?.paymentStatus)

                    if (media == null) {
                        binding.imageMedia.setVisibility(View.GONE)
                    }
                    if(it.data?.orderStatus=="CANCEL"){
                        binding.cancelTxt.visibility= View.VISIBLE
                        binding.deliveredButton.visibility= View.GONE
                        binding.status.setTextColor(Color.parseColor("#fe0000"))
                    }
                }
                Status.ERROR -> {

                }
                Status.LOADING -> {

                }
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


    private fun setupDeliveryListener(paymentStatus: String?) {
        binding.deliveredButton.setOnClickListener {
            if (paymentStatus != "COMPLETED") {
                val alertDialog = MaterialAlertDialogBuilder(context)

                alertDialog.setMessage(R.string.dialogMessage)
                alertDialog.setIcon(android.R.drawable.ic_dialog_alert)


                alertDialog.setPositiveButton("Yes") { dialogInterface, which ->
                    setDelivered()
                }

                alertDialog.setNegativeButton("No") { dialogInterface, which ->
                    Toast.makeText(context,"Please collect cash from customer", Toast.LENGTH_LONG).show()
                }
                alertDialog.setCancelable(false)

                alertDialog.show()
            } else {
                setDelivered()
            }
        }
    }
    private fun setDelivered() {
        showLoading()
        viewModel.deliverOrder(
            orderId.toString(),
            RequestOrderAcceptedByRider(OrderType.CITYWIDE.name, true)
        ).observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissLoading()
                    Toast.makeText(
                        requireContext(),
                        "Order has been delivered to customer",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.deliveredButton.visibility = View.GONE
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