package com.scootin.viewmodel.order

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.scootin.repository.OrderRepository
import com.scootin.viewmodel.base.ObservableViewModel
import kotlinx.coroutines.Dispatchers

class OrderDetailsViewModel @ViewModelInject
internal constructor(
    private val orderRepository: OrderRepository
) : ObservableViewModel() {

    fun getAcceptedOrderDetails(orderId: Long) = orderRepository.getAcceptedOrderDetails(orderId, viewModelScope.coroutineContext + Dispatchers.IO)

}