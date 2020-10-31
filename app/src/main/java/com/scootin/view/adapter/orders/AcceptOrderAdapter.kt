package com.scootin.view.adapter.orders

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.scootin.databinding.AdapterAcceptOrdersBinding
import com.scootin.databinding.AdapterCompletedOrdersBinding
import com.scootin.network.AppExecutors
import com.scootin.network.response.AcceptedOrderResponse
import com.scootin.network.response.PendingOrdersList
import com.scootin.view.adapter.DataBoundListAdapter
import timber.log.Timber

class AcceptOrderAdapter (
    val appExecutors: AppExecutors,
    val itemAdapterClickListener: ItemAdapterClickLister
) : DataBoundListAdapter<AcceptedOrderResponse, AdapterAcceptOrdersBinding>(
    appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<AcceptedOrderResponse>() {
        override fun areItemsTheSame(
            oldItem: AcceptedOrderResponse,
            newItem: AcceptedOrderResponse
        ): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: AcceptedOrderResponse,
            newItem: AcceptedOrderResponse
        ): Boolean {
            return oldItem == newItem
        }
    }
) {
    override fun createBinding(parent: ViewGroup): AdapterAcceptOrdersBinding =
        AdapterAcceptOrdersBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

    override fun bind(
        binding: AdapterAcceptOrdersBinding,
        item: AcceptedOrderResponse,
        position: Int,
        isLast: Boolean
    ) {
        binding.data=item

        binding.orderListTab.setOnClickListener {
            itemAdapterClickListener.onItemSelected(it)
        }
    }
    interface ItemAdapterClickLister {
        fun onItemSelected(view: View)
    }
}