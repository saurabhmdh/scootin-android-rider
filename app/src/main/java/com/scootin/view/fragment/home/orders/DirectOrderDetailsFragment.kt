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
import com.scootin.util.Conversions
import com.scootin.util.OrderType
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

    private val orderId by lazy {
        args.orderId
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDirectOrdersDetailsBinding.bind(view)
        binding.lifecycleOwner = this
        binding.pendingIcon.setImageResource(R.drawable.ic_pending_icon)
        setAdaper()

       // Timber.i("Order Detail is loading for element $args and bundle $savedInstanceState")

        viewModel.getDirectOrder(args.orderId).observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    Timber.i("Samridhi direct ${it.data?.extraData} ")
                    if (it.data?.extraData.isNullOrEmpty().not()) {
                        Timber.i("we have data ${it.data?.extraData}")
                        val extra = Conversions.convertExtraData(it.data?.extraData)
                        Timber.i("Extra $extra")
                        extraDataAdapter.submitList(extra)

                    }
                    binding.data = it.data


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