package com.scootin.view.fragment.home.orders

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.graphics.toColor
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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


    @SuppressLint("ResourceAsColor")
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
        viewModel.doNormalOrder(orderId)

        viewModel.loadOrder.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    Timber.i("Samridhi ${it.data}")
                    updateButtonVisibility(it.data?.orderDetails?.orderStatus)
                    binding.data = it.data
                    pendingOrdersAdapter.submitList(it.data?.orderInventoryDetailsList)

                    setupDeliveryListener(it.data?.orderDetails?.paymentDetails?.paymentStatus)

                    if(it.data?.orderDetails?.orderStatus=="CANCEL"){
                        binding.cancelTxt.visibility=View.VISIBLE
                        binding.pickupButton.visibility=View.GONE
                        binding.status.setTextColor(Color.parseColor("#fe0000"))
                    }
                }
                Status.ERROR -> { }
                Status.LOADING -> { }
            }
        }

        setupPickupListener()

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

    //It should be pickup
    private fun setupPickupListener() {
        binding.pickupButton.setOnClickListener {
            showLoading()
            viewModel.pickupOrder(orderId.toString(), RequestOrderAcceptedByRider(OrderType.NORMAL.name, true)).observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        dismissLoading()
                        Toast.makeText(requireContext(), "Order has been pickup", Toast.LENGTH_SHORT).show()
                        binding.pickupButton.visibility = View.GONE
                        viewModel.doNormalOrder(orderId)
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


    //It should be pickup
    private fun setupDeliveryListener(paymentStatus: String? ) {
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
    private fun setDelivered(){
        showLoading()
            viewModel.deliverOrder(orderId.toString(), RequestOrderAcceptedByRider(OrderType.NORMAL.name, true)).observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        dismissLoading()
                        Toast.makeText(requireContext(), "Order has been delivered to customer", Toast.LENGTH_SHORT).show()
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


    private fun setAdaper() {
        pendingOrdersAdapter = PendingOrderDetailsItemAdapter(appExecutors)

        binding.recyclerView.apply {
            adapter = pendingOrdersAdapter
        }
    }

}