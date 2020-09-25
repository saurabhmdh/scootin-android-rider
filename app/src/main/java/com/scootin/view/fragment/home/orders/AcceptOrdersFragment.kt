package com.scootin.view.fragment.home.orders

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.scootin.R
import com.scootin.databinding.FragmentAcceptOrdersBinding
import com.scootin.databinding.FragmentCompletedOrdersBinding
import com.scootin.network.AppExecutors
import com.scootin.network.response.PendingOrdersList
import com.scootin.util.fragment.autoCleared
import com.scootin.view.adapter.orders.AcceptOrderAdapter
import com.scootin.view.adapter.orders.CompletedOrdersAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class AcceptOrdersFragment:Fragment(R.layout.fragment_accept_orders) {
    private var binding by autoCleared<FragmentAcceptOrdersBinding>()

    @Inject
    lateinit var appExecutors: AppExecutors
    private lateinit var completedOrdersAdapter: AcceptOrderAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAcceptOrdersBinding.bind(view)
        setAdaper()
        completedOrdersAdapter.submitList(setList())

    }

    private fun setAdaper() {
        completedOrdersAdapter =
            AcceptOrderAdapter(
                appExecutors,
                object : AcceptOrderAdapter.ItemAdapterClickLister {
                    override fun onItemSelected(view: View) {
                        findNavController().navigate(AcceptOrdersFragmentDirections.actionAcceptOrdersFragmentToAcceptOrderDetailsFragment())
                    }

                })

        binding.recyclerView.apply {
            adapter = completedOrdersAdapter
        }
    }


    private fun setList(): ArrayList<PendingOrdersList> {
        val list = ArrayList<PendingOrdersList>()
        list.add(
            PendingOrdersList(
                "DBP/00009",
                "20-10-05",
                0
            )
        )
        list.add(
            PendingOrdersList(
                "DBP/00009",
                "20-10-05",
                0
            )
        )
        list.add(
            PendingOrdersList(
                "DBP/00009",
                "20-10-05",
                0
            )
        )

        return list
    }



}