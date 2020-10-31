package com.scootin.view.adapter.orders

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.scootin.databinding.AdapterPendingOrdersBinding
import com.scootin.databinding.AdapterPendingOrdersItemListBinding
import com.scootin.network.AppExecutors
import com.scootin.network.response.NormalOrderResponse
import com.scootin.network.response.PendingOrderItemList
import com.scootin.network.response.PendingOrdersList
import com.scootin.network.response.UnAssignedOrderResponse
import com.scootin.view.adapter.DataBoundListAdapter
import timber.log.Timber

class PendingOrderDetailsItemAdapter (
    val appExecutors: AppExecutors
) : DataBoundListAdapter<NormalOrderResponse.OrderInventoryDetails, AdapterPendingOrdersItemListBinding>(
    appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<NormalOrderResponse.OrderInventoryDetails>() {
        override fun areItemsTheSame(
            oldItem: NormalOrderResponse.OrderInventoryDetails,
            newItem: NormalOrderResponse.OrderInventoryDetails
        ): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(
            oldItem: NormalOrderResponse.OrderInventoryDetails,
            newItem: NormalOrderResponse.OrderInventoryDetails
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
        item: NormalOrderResponse.OrderInventoryDetails,
        position: Int,
        isLast: Boolean
    ) {

        binding.data=item
    }
}