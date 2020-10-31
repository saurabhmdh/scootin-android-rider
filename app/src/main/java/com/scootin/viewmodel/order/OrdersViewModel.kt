package com.scootin.viewmodel.order

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.scootin.repository.OrderRepository
import com.scootin.viewmodel.base.ObservableViewModel
import kotlinx.coroutines.Dispatchers

class OrdersViewModel  @ViewModelInject
internal constructor(
    private val orderRepository: OrderRepository
) : ObservableViewModel() {

    fun getAllUnAssigned() = orderRepository.getAllUnAssigned(viewModelScope.coroutineContext + Dispatchers.IO)

    fun getCompletedOrders(riderId: String) = orderRepository.getCompletedOrders(riderId, viewModelScope.coroutineContext + Dispatchers.IO)

    fun getAcceptedOrders(riderId: String) = orderRepository.getAcceptedOrders(riderId, viewModelScope.coroutineContext + Dispatchers.IO)
}