package com.scootin.view.fragment.home.orders

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.scootin.R
import com.scootin.databinding.FragmentCompletedOrdersBinding
import com.scootin.databinding.FragmentPendingOrdersBinding
import com.scootin.network.AppExecutors
import com.scootin.network.response.PendingOrdersList
import com.scootin.util.fragment.autoCleared
import com.scootin.view.adapter.orders.CompletedOrdersAdapter
import com.scootin.view.adapter.orders.PendingOrdersAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class CompletedOrdersFragment:Fragment(R.layout.fragment_completed_orders) {
    private var binding by autoCleared<FragmentCompletedOrdersBinding>()

    @Inject
    lateinit var appExecutors: AppExecutors
    private lateinit var completedOrdersAdapter: CompletedOrdersAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCompletedOrdersBinding.bind(view)
        setAdaper()
        completedOrdersAdapter.submitList(setList())

    }

    private fun setAdaper() {
        completedOrdersAdapter =
            CompletedOrdersAdapter(
                appExecutors,
                object : CompletedOrdersAdapter.ItemAdapterClickLister {
                    override fun onItemSelected(view: View) {
                        findNavController().navigate(CompletedOrdersFragmentDirections.actionCompletedOrdersFragmentToCompletedOrderDetailsFragment())
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