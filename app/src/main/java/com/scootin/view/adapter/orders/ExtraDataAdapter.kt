package com.scootin.view.adapter.orders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.scootin.databinding.AdapterExtraDetailsBinding
import com.scootin.databinding.AdapterPendingOrdersItemListBinding
import com.scootin.network.AppExecutors
import com.scootin.network.response.DirectOrderResponse
import com.scootin.network.response.NormalOrderResponse
import com.scootin.network.response.SearchISuggestiontem
import com.scootin.view.adapter.DataBoundListAdapter

class ExtraDataAdapter (
    val appExecutors: AppExecutors
) : DataBoundListAdapter<SearchISuggestiontem, AdapterExtraDetailsBinding>(
    appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<SearchISuggestiontem>() {
        override fun areItemsTheSame(
            oldItem: SearchISuggestiontem,
            newItem: SearchISuggestiontem
        ): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(
            oldItem: SearchISuggestiontem,
            newItem: SearchISuggestiontem
        ): Boolean {
            return oldItem == newItem
        }
    }
) {
    override fun createBinding(parent: ViewGroup): AdapterExtraDetailsBinding =
        AdapterExtraDetailsBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

    override fun bind(
        binding: AdapterExtraDetailsBinding,
        item: SearchISuggestiontem,
        position: Int,
        isLast: Boolean
    ) {

        binding.data=item
    }
}