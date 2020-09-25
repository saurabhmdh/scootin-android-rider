package com.scootin.view.fragment.home.orders

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.scootin.R
import com.scootin.databinding.FragmentPendingOrdersBinding
import com.scootin.network.AppExecutors
import com.scootin.network.response.PendingOrdersList
import com.scootin.util.fragment.autoCleared
import com.scootin.view.adapter.orders.PendingOrdersAdapter
import com.scootin.view.fragment.home.dashboard.DashboardFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class PendingOrdersFragment:Fragment(R.layout.fragment_pending_orders) {
    private var binding by autoCleared<FragmentPendingOrdersBinding>()

    @Inject
    lateinit var appExecutors: AppExecutors
    private lateinit var pendingOrdersAdapter: PendingOrdersAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPendingOrdersBinding.bind(view)
        setAdaper()
        pendingOrdersAdapter.submitList(setList())

    }

    private fun setAdaper() {
        pendingOrdersAdapter =
            PendingOrdersAdapter(
                appExecutors,
                object : PendingOrdersAdapter.ItemAdapterClickLister {
                    override fun onItemSelected(view: View) {
                  findNavController().navigate(PendingOrdersFragmentDirections.actionPendingOrdersFragmentToPendingOrderDetailsFragment())
                    }

                })

        binding.recyclerView.apply {
            adapter = pendingOrdersAdapter
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