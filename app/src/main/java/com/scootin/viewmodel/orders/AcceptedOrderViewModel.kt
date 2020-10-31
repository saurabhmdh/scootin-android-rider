package com.scootin.viewmodel.orders

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.scootin.repository.AcceptedOrdersRepository
import com.scootin.viewmodel.base.ObservableViewModel
import kotlinx.coroutines.Dispatchers

class AcceptedOrderViewModel @ViewModelInject
internal constructor(
    private val acceptedOrdersRepository: AcceptedOrdersRepository
) : ObservableViewModel() {


    fun acceptedOrders(riderId: String) = acceptedOrdersRepository.postAcceptedOrder(
        riderId,
        viewModelScope.coroutineContext + Dispatchers.IO
    )

}
