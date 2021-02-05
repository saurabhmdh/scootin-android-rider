package com.scootin.view.adapter.orders

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.scootin.databinding.AdapterAcceptOrdersBinding
import com.scootin.databinding.AdapterCompletedOrdersBinding
import com.scootin.databinding.AdapterPendingOrdersBinding
import com.scootin.network.AppExecutors
import com.scootin.network.response.OrderListResponse
import com.scootin.network.response.PendingOrdersList
import com.scootin.network.response.UnAssignedOrderResponse
import com.scootin.view.adapter.DataBoundListAdapter
import com.scootin.view.holders.DataBoundViewHolder
import timber.log.Timber

class AcceptOrderAdapter (
    val appExecutors: AppExecutors,
    val itemAdapterClickListener: ItemAdapterClickLister
) : PagingDataAdapter<OrderListResponse, DataBoundViewHolder<AdapterAcceptOrdersBinding>>(diffCallback = object : DiffUtil.ItemCallback<OrderListResponse>() {
    override fun areItemsTheSame(oldItem: OrderListResponse, newItem: OrderListResponse): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: OrderListResponse, newItem: OrderListResponse): Boolean = oldItem == newItem
}) {

    override fun onBindViewHolder(
        holder: DataBoundViewHolder<AdapterAcceptOrdersBinding>,
        position: Int
    ) {
        val binding = holder.binding

        getItem(position)?.let { item ->

            Timber.i("item = $item ${item.id}")
            item.apply {
                binding.orderId.setText(item.orderId.toString())
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
    ): DataBoundViewHolder<AdapterAcceptOrdersBinding> {
        return DataBoundViewHolder(AdapterAcceptOrdersBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    }

    interface ItemAdapterClickLister {
        fun onItemSelected(view: OrderListResponse)
        fun onHandwrittenListOrderSelected(view: OrderListResponse)
        fun onCitywideOrderSelected(view: OrderListResponse)
    }

}