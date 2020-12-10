package com.scootin.view.adapter.orders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.scootin.databinding.AdapterAcceptOrdersBinding
import com.scootin.databinding.AdapterCompletedOrdersBinding
import com.scootin.network.AppExecutors
import com.scootin.network.response.OrderListResponse
import com.scootin.view.adapter.DataBoundListAdapter
import com.scootin.view.holders.DataBoundViewHolder
import timber.log.Timber

class CompletedOrdersAdapter (
    val appExecutors: AppExecutors,
    val itemAdapterClickListener: ItemAdapterClickLister
) : PagingDataAdapter<OrderListResponse, DataBoundViewHolder<AdapterCompletedOrdersBinding>>(diffCallback = object : DiffUtil.ItemCallback<OrderListResponse>() {
    override fun areItemsTheSame(oldItem: OrderListResponse, newItem: OrderListResponse): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: OrderListResponse, newItem: OrderListResponse): Boolean = oldItem == newItem
}) {

    override fun onBindViewHolder(
        holder: DataBoundViewHolder<AdapterCompletedOrdersBinding>,
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
    ): DataBoundViewHolder<AdapterCompletedOrdersBinding> {
        return DataBoundViewHolder(AdapterCompletedOrdersBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    }

    interface ItemAdapterClickLister {
        fun onItemSelected(view: OrderListResponse)
        fun onHandwrittenListOrderSelected(view: OrderListResponse)
        fun onCitywideOrderSelected(view: OrderListResponse)
    }

}
//    val appExecutors: AppExecutors,
//    val itemAdapterClickListener: ItemAdapterClickLister
//) : DataBoundListAdapter<OrderListResponse, AdapterCompletedOrdersBinding>(
//    appExecutors,
//    diffCallback = object : DiffUtil.ItemCallback<OrderListResponse>() {
//        override fun areItemsTheSame(
//            oldItem: OrderListResponse,
//            newItem: OrderListResponse
//        ): Boolean {
//            return oldItem.id == newItem.id
//        }
//
//        override fun areContentsTheSame(
//            oldItem: OrderListResponse,
//            newItem: OrderListResponse
//        ): Boolean {
//            return oldItem == newItem
//        }
//    }
//) {
//    override fun createBinding(parent: ViewGroup): AdapterCompletedOrdersBinding =
//        AdapterCompletedOrdersBinding.inflate(
//            LayoutInflater.from(parent.context), parent, false
//        )
//
//    override fun bind(
//        binding: AdapterCompletedOrdersBinding,
//        item: OrderListResponse,
//        position: Int,
//        isLast: Boolean
//    ) {
//        Timber.i("item = $item")
//        item.apply {
//            binding.orderId.setText(item.id.toString())
//            when(item.orderType){
//                "DIRECT"-> {
//                    if (item.expressDelivery){
//                        binding.orderDate.setText("Express")
//                    }
//                    else{
//                        binding.orderDate.setText("Normal")
//                    }
//                }
//                "NORMAL"-> binding.orderDate.setText("Normal")
//
//                "CITYWIDE"->binding.orderDate.setText("Citywide")
//            }
//        }
//        binding.orderListTab.setOnClickListener {
//            when(item.orderType) {
//                "DIRECT"->itemAdapterClickListener.onHandwrittenListOrderSelected(item)
//
//                "NORMAL"->itemAdapterClickListener.onItemSelected(item)
//
//                "CITYWIDE"->itemAdapterClickListener.onCitywideOrderSelected(item)
//            }
//        }
//    }
//    interface ItemAdapterClickLister {
//        fun onItemSelected(view: OrderListResponse)
//        fun onHandwrittenListOrderSelected(view: OrderListResponse)
//        fun onCitywideOrderSelected(view: OrderListResponse)
//    }
//}