package com.scootin.view.adapter.orders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.scootin.databinding.AdapterPendingOrdersItemListBinding
import com.scootin.network.AppExecutors
import com.scootin.network.response.UnAssignedOrderResponse
import com.scootin.view.adapter.DataBoundListAdapter
import timber.log.Timber

class AcceptedOrderDetailsAdapter (
    val appExecutors: AppExecutors
) : DataBoundListAdapter<UnAssignedOrderResponse, AdapterPendingOrdersItemListBinding>(
    appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<UnAssignedOrderResponse>() {
        override fun areItemsTheSame(
            oldItem: UnAssignedOrderResponse,
            newItem: UnAssignedOrderResponse
        ): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(
            oldItem: UnAssignedOrderResponse,
            newItem: UnAssignedOrderResponse
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
        item: UnAssignedOrderResponse,
        position: Int,
        isLast: Boolean
    ) {
        Timber.i("item = $item")
        item.apply {
            binding.itemName.setText(item.id.toString())
            binding.qty.setText(item.totalAmount.toString())
        }
    }
}