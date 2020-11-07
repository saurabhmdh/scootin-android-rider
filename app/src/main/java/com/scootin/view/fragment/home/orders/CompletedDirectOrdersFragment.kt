package com.scootin.view.fragment.home.orders

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.navArgs
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.scootin.R
import com.scootin.databinding.FragmentDirectOrdersDetailsBinding
import com.scootin.network.AppExecutors
import com.scootin.network.api.Status
import com.scootin.util.Conversions
import com.scootin.util.fragment.autoCleared
import com.scootin.view.adapter.orders.ExtraDataAdapter
import com.scootin.view.adapter.orders.PendingOrderDetailsItemAdapter
import com.scootin.viewmodel.order.OrdersViewModel
import dagger.hilt.android.AndroidEntryPoint
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