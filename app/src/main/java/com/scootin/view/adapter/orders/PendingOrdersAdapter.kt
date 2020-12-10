package com.scootin.view.adapter.orders

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.scootin.R
import com.scootin.databinding.AdapterPendingOrdersBinding
import com.scootin.network.AppExecutors
import com.scootin.network.response.OrderListResponse
import com.scootin.network.response.PendingOrdersList
import com.scootin.network.response.UnAssignedOrderResponse
import com.scootin.view.adapter.DataBoundListAdapter
import com.scootin.view.holders.DataBoundViewHolder
import timber.log.Timber

class PendingOrdersAdapter (
    val appExecutors: AppExecutors,
    val itemAdapterClickListener: ItemAdapterClickLister
) : PagingDataAdapter<UnAssignedOrderResponse, DataBoundViewHolder<AdapterPendingOrdersBinding>>(diffCallback = object : DiffUtil.ItemCallback<UnAssignedOrderResponse>() {
    override fun areItemsTheSame(oldItem: UnAssignedOrderResponse, newItem: UnAssignedOrderResponse): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: UnAssignedOrderResponse, newItem: UnAssignedOrderResponse): Boolean = oldItem == newItem
}) {

    override fun onBindViewHolder(
        holder: DataBoundViewHolder<AdapterPendingOrdersBinding>,
        position: Int
    ) {
        val binding = holder.binding

        getItem(position)?.let { item ->

            Timber.i("item = $item ${item.id}")
            item.apply {
                binding.orderId.setText(item.id.toString())
                when(item.orderType){
                    "DIRECT"-> {
                        if (item.expressDelivery){
                            binding.orderDate.setText("Express")
                        }
                        else{
                            binding.orderDate.setText("Normal")
                        }
                    }
                    "NORMAL"-> binding.orderDate.setText("Normal")

                    "CITYWIDE"->binding.orderDate.setText("Citywide")
                }
            }
            binding.orderListTab.setOnClickListener {
                when(item.orderType) {
                    "DIRECT"->itemAdapterClickListener.onHandwrittenListOrderSelected(item)

                    "NORMAL"->itemAdapterClickListener.onItemSelected(item)

                    "CITYWIDE"->itemAdapterClickListener.onCitywideOrderSelected(item)
                }
            }
        }


    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DataBoundViewHolder<AdapterPendingOrdersBinding> {
        return DataBoundViewHolder(AdapterPendingOrdersBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    }
    interface ItemAdapterClickLister {
        fun onItemSelected(view: UnAssignedOrderResponse)
        fun onHandwrittenListOrderSelected(view: UnAssignedOrderResponse)
        fun onCitywideOrderSelected(view: UnAssignedOrderResponse)

    }
}