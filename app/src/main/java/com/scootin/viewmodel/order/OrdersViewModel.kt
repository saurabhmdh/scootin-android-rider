package com.scootin.viewmodel.order

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.scootin.network.request.RequestOrderAcceptedByRider
import com.scootin.repository.OrderRepository
import com.scootin.viewmodel.base.ObservableViewModel
import com.scootin.viewmodel.home.LoginViewModel
import kotlinx.coroutines.Dispatchers

class OrdersViewModel  @ViewModelInject
internal constructor(
    private val orderRepository: OrderRepository
) : ObservableViewModel() {

    private val _order = MutableLiveData<Long>()

    fun doNormalOrder(orderId: Long) {
        _order.postValue(orderId)
    }

    val loadOrder = Transformations.switchMap(_order) {
        orderRepository.getOrder(it, viewModelScope.coroutineContext + Dispatchers.IO)
    }

    private val _direct_order = MutableLiveData<Long>()

    fun doDirectOrder(orderId: Long) {
        _direct_order.postValue(orderId)
    }

    val loadDirectOrder = Transformations.switchMap(_direct_order) {
        orderRepository.getDirectOrder(it, viewModelScope.coroutineContext + Dispatchers.IO)
    }

    fun getAllUnAssigned() = orderRepository.getAllUnAssigned(viewModelScope.coroutineContext + Dispatchers.IO)

    fun getNormalOrder(orderId: Long) = orderRepository.getOrder(orderId, viewModelScope.coroutineContext + Dispatchers.IO)

    fun getDirectOrder(orderId: Long) = orderRepository.getDirectOrder(orderId, viewModelScope.coroutineContext + Dispatchers.IO)

    fun getCitywideOrder(orderId: Long) = orderRepository.getCityWideOrder(orderId, viewModelScope.coroutineContext + Dispatchers.IO)

    fun getCompletedOrders(riderId: String) = orderRepository.getCompletedOrders(riderId, viewModelScope.coroutineContext + Dispatchers.IO)

    fun getAcceptedOrders(riderId: String) = orderRepository.getAcceptedOrders(riderId, viewModelScope.coroutineContext + Dispatchers.IO)

    fun acceptOrder(riderId: String, orderId: String, requestAcceptOffer: RequestOrderAcceptedByRider) = orderRepository.acceptOrder(riderId, orderId, requestAcceptOffer, viewModelScope.coroutineContext + Dispatchers.IO)

    fun deliverOrder(orderId: String, requestAcceptOffer: RequestOrderAcceptedByRider) = orderRepository.deliverOrder(orderId, requestAcceptOffer, viewModelScope.coroutineContext + Dispatchers.IO)

    fun pickupOrder(orderId: String, requestAcceptOffer: RequestOrderAcceptedByRider) = orderRepository.pickupOrder(orderId, requestAcceptOffer, viewModelScope.coroutineContext + Dispatchers.IO)
}