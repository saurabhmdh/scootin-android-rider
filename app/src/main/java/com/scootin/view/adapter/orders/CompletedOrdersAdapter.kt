package com.scootin.view.adapter.orders

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.scootin.databinding.AdapterCompletedOrdersBinding
import com.scootin.databinding.AdapterPendingOrdersBinding
import com.scootin.network.AppExecutors
import com.scootin.network.response.OrderListResponse
import com.scootin.network.response.PendingOrdersList
import com.scootin.view.adapter.DataBoundListAdapter
import timber.log.Timber

class CompletedOrdersAdapter (
    val appExecutors: AppExecutors,
    val itemAdapterClickListener: ItemAdapterClickLister
) : DataBoundListAdapter<OrderListResponse, AdapterCompletedOrdersBinding>(
    appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<OrderListResponse>() {
        override fun areItemsTheSame(
            oldItem: OrderListResponse,
            newItem: OrderListResponse
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: OrderListResponse,
            newItem: OrderListResponse
        ): Boolean {
            return oldItem == newItem
        }
    }
) {
    override fun createBinding(parent: ViewGroup): AdapterCompletedOrdersBinding =
        AdapterCompletedOrdersBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

    override fun bind(
        binding: AdapterCompletedOrdersBinding,
        item: OrderListResponse,
        position: Int,
        isLast: Boolean
    ) {
        Timber.i("item = $item")
        item.apply {
            binding.orderId.setText(item.id.toString())
            binding.orderDate.setText(item.orderStatus)
        }
        binding.orderListTab.setOnClickListener {
            if(item.directOrder){
                itemAdapterClickListener.onHandwrittenListOrderSelected(item)
            }
            else{
                itemAdapterClickListener.onItemSelected(item)
            }

        }
    }
    interface ItemAdapterClickLister {
        fun onItemSelected(view: OrderListResponse)
        fun onHandwrittenListOrderSelected(view: OrderListResponse)
    }
}