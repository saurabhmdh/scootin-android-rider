package com.scootin.viewmodel.order

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.scootin.network.request.RequestOrderAcceptedByRider
import com.scootin.repository.OrderRepository
import com.scootin.viewmodel.base.ObservableViewModel
import kotlinx.coroutines.Dispatchers

class OrdersViewModel  @ViewModelInject
internal constructor(
    private val orderRepository: OrderRepository
) : ObservableViewModel() {

    fun getAllUnAssigned() = orderRepository.getAllUnAssigned(viewModelScope.coroutineContext + Dispatchers.IO)

    fun getNormalOrder(orderId: Long) = orderRepository.getOrder(orderId, viewModelScope.coroutineContext + Dispatchers.IO)

    fun getDirectOrder(orderId: Long) = orderRepository.getDirectOrder(orderId, viewModelScope.coroutineContext + Dispatchers.IO)

    fun getCompletedOrders(riderId: String) = orderRepository.getCompletedOrders(riderId, viewModelScope.coroutineContext + Dispatchers.IO)

    fun getAcceptedOrders(riderId: String) = orderRepository.getAcceptedOrders(riderId, viewModelScope.coroutineContext + Dispatchers.IO)

    fun acceptOrder(riderId: String, orderId: String, requestAcceptOffer: RequestOrderAcceptedByRider) = orderRepository.acceptOrder(riderId, orderId, requestAcceptOffer, viewModelScope.coroutineContext + Dispatchers.IO)

    fun deliverOrder(riderId: String, orderId: String, requestAcceptOffer: RequestOrderAcceptedByRider) = orderRepository.deliverOrder(orderId, requestAcceptOffer, viewModelScope.coroutineContext + Dispatchers.IO)
}