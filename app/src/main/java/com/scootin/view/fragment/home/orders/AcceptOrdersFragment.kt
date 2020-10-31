package com.scootin.view.fragment.home.orders

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.scootin.R
import com.scootin.databinding.FragmentAcceptOrdersBinding
import com.scootin.databinding.FragmentCompletedOrdersBinding
import com.scootin.network.AppExecutors
import com.scootin.network.api.Status
import com.scootin.network.manager.AppHeaders
import com.scootin.network.response.PendingOrdersList
import com.scootin.util.fragment.autoCleared
import com.scootin.view.adapter.orders.AcceptOrderAdapter
import com.scootin.view.adapter.orders.CompletedOrdersAdapter
import com.scootin.viewmodel.orders.AcceptedOrderViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject
@AndroidEntryPoint
class AcceptOrdersFragment:Fragment(R.layout.fragment_accept_orders) {
    private var binding by autoCleared<FragmentAcceptOrdersBinding>()

    @Inject
    lateinit var appExecutors: AppExecutors
    private lateinit var acceptOrderAdapter: AcceptOrderAdapter
    private val viewModel: AcceptedOrderViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAcceptOrdersBinding.bind(view)
        setAdaper()
        incomingOrdersList()


    }

    private fun setAdaper() {
        acceptOrderAdapter =
            AcceptOrderAdapter(
                appExecutors,
                object : AcceptOrderAdapter.ItemAdapterClickLister {
                    override fun onItemSelected(view: View) {
                        findNavController().navigate(AcceptOrdersFragmentDirections.actionAcceptOrdersFragmentToAcceptOrderDetailsFragment())
                    }

                })

        binding.recyclerView.apply {
            adapter = acceptOrderAdapter
        }
    }
    private fun incomingOrdersList() {
        viewModel.acceptedOrders(AppHeaders.userID).observe(viewLifecycleOwner){
            Timber.i("We got the data.. $it")
            when(it.status) {
                Status.ERROR -> {}
                Status.LOADING -> {
                }
                Status.SUCCESS -> {
                    acceptOrderAdapter.submitList(it.data)
                }
            }

        }
    }
//    private fun setList(): ArrayList<PendingOrdersList> {
//        val list = ArrayList<PendingOrdersList>()
//        list.add(
//            PendingOrdersList(
//                "DBP/00009",
//                "20-10-05",
//                0
//            )
//        )
//        list.add(
//            PendingOrdersList(
//                "DBP/00009",
//                "20-10-05",
//                0
//            )
//        )
//        list.add(
//            PendingOrdersList(
//                "DBP/00009",
//                "20-10-05",
//                0
//            )
//        )
//
//        return list
//    }
//


}