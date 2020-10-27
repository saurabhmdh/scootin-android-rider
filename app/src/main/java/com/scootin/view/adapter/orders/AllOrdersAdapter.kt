package com.scootin.view.adapter.orders

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.scootin.R
import com.scootin.databinding.AdapterAllOrdersBinding
import com.scootin.databinding.AdapterCompletedOrdersBinding
import com.scootin.network.AppExecutors
import com.scootin.network.response.AllOrdersList
import com.scootin.network.response.PendingOrdersList
import com.scootin.view.adapter.DataBoundListAdapter
import timber.log.Timber

class AllOrdersAdapter(
    val appExecutors: AppExecutors,
    val itemAdapterClickListener: ItemAdapterClickLister
    ) : DataBoundListAdapter<AllOrdersList,AdapterAllOrdersBinding>(
    appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<AllOrdersList>() {
        override fun areItemsTheSame(
            oldItem: AllOrdersList,
            newItem: AllOrdersList
        ): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: AllOrdersList,
            newItem: AllOrdersList
        ): Boolean {
            return oldItem == newItem
        }
    }
    ) {
        override fun createBinding(parent: ViewGroup): AdapterAllOrdersBinding =
            AdapterAllOrdersBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )

        override fun bind(
            binding: AdapterAllOrdersBinding,
            item: AllOrdersList,
            position: Int,
            isLast: Boolean
        ) {
            Timber.i("item = $item")
            item.apply {
                binding.orderId.setText(order_id)
                binding.orderDate.setText(order_date)
                if(order_status){
                    binding.statusIcon.setImageResource(R.drawable.ic_completed_icon)
                    binding.orderListTab.setOnClickListener {
                        itemAdapterClickListener.onCompletedOrderSelected(it)
                    }
                }
                else{
                    binding.statusIcon.setImageResource(R.drawable.ic_clock)
                    binding.orderListTab.setOnClickListener {
                        itemAdapterClickListener.onPendingOrderSelected(it)
                    }
                }
            }

        }
        interface ItemAdapterClickLister {
            fun onPendingOrderSelected(view: View)
            fun onCompletedOrderSelected(view: View)
        }


}
