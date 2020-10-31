package com.scootin.view.fragment.home.orders

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.scootin.R
import com.scootin.databinding.FragmentCompletedOrderDetailsBinding
import com.scootin.databinding.FragmentCompletedOrdersBinding
import com.scootin.databinding.FragmentPendingOrderDetailsBinding
import com.scootin.network.AppExecutors
import com.scootin.network.response.PendingOrderItemList
import com.scootin.util.fragment.autoCleared
import com.scootin.view.adapter.orders.PendingOrderDetailsItemAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class CompletedOrderDetailsFragment:Fragment(R.layout.fragment_completed_order_details) {
    private var binding by autoCleared<FragmentCompletedOrderDetailsBinding>()

    @Inject
    lateinit var appExecutors: AppExecutors
    private lateinit var pendingOrdersAdapter: PendingOrderDetailsItemAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCompletedOrderDetailsBinding.bind(view)
        setAdaper()
//        pendingOrdersAdapter.submitList(setList())
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