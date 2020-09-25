package com.scootin.view.adapter.orders

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.scootin.databinding.AdapterPendingOrdersBinding
import com.scootin.databinding.AdapterPendingOrdersItemListBinding
import com.scootin.network.AppExecutors
import com.scootin.network.response.PendingOrderItemList
import com.scootin.network.response.PendingOrdersList
import com.scootin.view.adapter.DataBoundListAdapter
import timber.log.Timber

class PendingOrderDetailsItemAdapter (
    val appExecutors: AppExecutors
) : DataBoundListAdapter<PendingOrderItemList, AdapterPendingOrdersItemListBinding>(
    appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<PendingOrderItemList>() {
        override fun areItemsTheSame(
            oldItem: PendingOrderItemList,
            newItem: PendingOrderItemList
        ): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: PendingOrderItemList,
            newItem: PendingOrderItemList
        ): Boolean {
            return oldItem == newItem
        }
    }
) {
    override fun createBinding(parent: ViewGroup): AdapterPendingOrdersItemListBinding =
        AdapterPendingOrdersItemListBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

    override fun bind(
        binding: AdapterPendingOrdersItemListBinding,
        item: PendingOrderItemList,
        position: Int,
        isLast: Boolean
    ) {
        Timber.i("item = $item")
        item.apply {
            binding.itemName.setText(item_name)
            binding.qty.setText(item_quantity)
        }
    }
}