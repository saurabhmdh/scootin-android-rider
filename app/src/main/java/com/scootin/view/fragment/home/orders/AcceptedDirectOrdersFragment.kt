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
import com.scootin.util.Conversions
import com.scootin.util.OrderType
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

    private val orderId by lazy {
        args.orderId
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAcceptedDirectOrdersDetailsBinding.bind(view)
        binding.lifecycleOwner = this
        setAdaper()

        binding.pendingIcon.setImageResource(R.drawable.ic_accepted_icon)
        // Timber.i("Order Detail is loading for element $args and bundle $savedInstanceState")

        viewModel.getDirectOrder(orderId).observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    Timber.i("Samridhi direct ${it.data}")
                    binding.data = it.data
                    if (it.data?.extraData.isNullOrEmpty().not()) {
                        val extra = Conversions.convertExtraData(it.data?.extraData)
                        Timber.i("Extra $extra")
                        extraDataAdapter.submitList(extra)
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
            viewModel.deliverOrder(orderId.toString(), RequestOrderAcceptedByRider(OrderType.DIRECT.name, true)).observe(viewLifecycleOwner) {
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
        extraDataAdapter = ExtraDataAdapter(appExecutors)

        binding.recycler.apply {
            adapter = extraDataAdapter
        }
    }

}