package com.scootin.view.adapter.orders

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.scootin.databinding.AdapterPendingOrdersBinding
import com.scootin.network.AppExecutors
import com.scootin.network.response.PendingOrdersList
import com.scootin.view.adapter.DataBoundListAdapter
import timber.log.Timber

class PendingOrdersAdapter (
    val appExecutors: AppExecutors,
    val itemAdapterClickListener: ItemAdapterClickLister
) : DataBoundListAdapter<PendingOrdersList, AdapterPendingOrdersBinding>(
    appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<PendingOrdersList>() {
        override fun areItemsTheSame(
            oldItem: PendingOrdersList,
            newItem: PendingOrdersList
        ): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: PendingOrdersList,
            newItem: PendingOrdersList
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
        item: PendingOrdersList,
        position: Int,
        isLast: Boolean
    ) {
        Timber.i("item = $item")
        item.apply {
            binding.orderId.setText(order_id)
            binding.orderDate.setText(order_date)
        }
        binding.orderListTab.setOnClickListener {
            itemAdapterClickListener.onItemSelected(it)
        }
    }
    interface ItemAdapterClickLister {
        fun onItemSelected(view: View)
    }
}