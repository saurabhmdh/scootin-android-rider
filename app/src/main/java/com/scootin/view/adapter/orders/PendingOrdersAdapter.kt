package com.scootin.view.adapter.orders

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.scootin.databinding.AdapterPendingOrdersBinding
import com.scootin.network.AppExecutors
import com.scootin.network.response.OrderListResponse
import com.scootin.network.response.PendingOrdersList
import com.scootin.network.response.UnAssignedOrderResponse
import com.scootin.view.adapter.DataBoundListAdapter
import timber.log.Timber

class PendingOrdersAdapter (
    val appExecutors: AppExecutors,
    val itemAdapterClickListener: ItemAdapterClickLister
) : DataBoundListAdapter<UnAssignedOrderResponse, AdapterPendingOrdersBinding>(
    appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<UnAssignedOrderResponse>() {
        override fun areItemsTheSame(
            oldItem: UnAssignedOrderResponse,
            newItem: UnAssignedOrderResponse
        ): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: UnAssignedOrderResponse,
            newItem: UnAssignedOrderResponse
        ): Boolean {
            return oldItem == newItem
        }
    }
) {
    override fun createBinding(parent: ViewGroup): AdapterPendingOrdersBinding =
        AdapterPendingOrdersBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

    override fun bind(
        binding: AdapterPendingOrdersBinding,
        item: UnAssignedOrderResponse,
        position: Int,
        isLast: Boolean
    ) {
        Timber.i("item = $item")
        item.apply {
            binding.orderId.setText(item.id.toString())
            binding.orderDate.setText(item.orderStatus)
        }
        binding.orderListTab.setOnClickListener {
            if(item.directOrder) {
                itemAdapterClickListener.onHandwrittenListOrderSelected(item)
            }
            else{
                itemAdapterClickListener.onItemSelected(item)
            }
        }
    }
    interface ItemAdapterClickLister {
        fun onItemSelected(view: UnAssignedOrderResponse)
        fun onHandwrittenListOrderSelected(view: UnAssignedOrderResponse)
    }
}