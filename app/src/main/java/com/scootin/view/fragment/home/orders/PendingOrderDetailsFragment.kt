package com.scootin.view.fragment.home.orders

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.scootin.R
import com.scootin.databinding.FragmentPendingOrderDetailsBinding
import com.scootin.databinding.FragmentPendingOrdersBinding
import com.scootin.network.AppExecutors
import com.scootin.network.response.PendingOrderItemList
import com.scootin.network.response.PendingOrdersList
import com.scootin.util.fragment.autoCleared
import com.scootin.view.adapter.orders.PendingOrderDetailsItemAdapter
import com.scootin.view.adapter.orders.PendingOrdersAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class PendingOrderDetailsFragment:Fragment(R.layout.fragment_pending_order_details) {
    private var binding by autoCleared<FragmentPendingOrderDetailsBinding>()

    @Inject
    lateinit var appExecutors: AppExecutors
    private lateinit var pendingOrdersAdapter: PendingOrderDetailsItemAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPendingOrderDetailsBinding.bind(view)
        setAdaper()
        pendingOrdersAdapter.submitList(setList())

    }

    private fun setAdaper() {
        pendingOrdersAdapter =
            PendingOrderDetailsItemAdapter(
                appExecutors)

        binding.recyclerView.apply {
            adapter = pendingOrdersAdapter
        }
    }


    private fun setList(): ArrayList<PendingOrderItemList> {
        val list = ArrayList<PendingOrderItemList>()
        list.add(
            PendingOrderItemList(
                "earpod",
                "2",
                "",
                0
            )
        )
        list.add(
            PendingOrderItemList(
                "ipod",
                "1",
                "",
                0
            )
        )
        list.add(
            PendingOrderItemList(
                "snacks",
                "1",
                "",
                0
            )
        )

        return list
    }



}