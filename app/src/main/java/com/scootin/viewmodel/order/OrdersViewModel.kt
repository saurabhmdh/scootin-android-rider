package com.scootin.viewmodel.order

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.scootin.network.request.RequestCitywideOrder
import com.scootin.network.request.RequestOrderAcceptedByRider
import com.scootin.repository.OrderRepository
import com.scootin.viewmodel.base.ObservableViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import timber.log.Timber
import java.io.File

class OrdersViewModel  @ViewModelInject
internal constructor(
    private val orderRepository: OrderRepository
) : ObservableViewModel() {

    private val handler = CoroutineExceptionHandler { _, exception ->
        Timber.i("Caught  $exception")
    }


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
    fun getAllUnAssigned() =
        orderRepository.getAllUnAssigned().cachedIn(viewModelScope).asLiveData()

    fun getAcceptedOrders(riderId: String) =
        orderRepository.getAcceptedOrders(riderId).cachedIn(viewModelScope).asLiveData()

    fun getCompletedOrders(riderId: String) =
        orderRepository.getCompletedOrders(riderId).cachedIn(viewModelScope).asLiveData()

   // fun getAllUnAssigned() = orderRepository.getAllUnAssigned(viewModelScope.coroutineContext + Dispatchers.IO)

    fun getNormalOrder(orderId: Long) = orderRepository.getOrder(orderId, viewModelScope.coroutineContext + Dispatchers.IO)

    fun getDirectOrder(orderId: Long) = orderRepository.getDirectOrder(orderId, viewModelScope.coroutineContext + Dispatchers.IO)

    fun getCitywideOrder(orderId: Long) = orderRepository.getCityWideOrder(orderId, viewModelScope.coroutineContext + Dispatchers.IO)

   // fun getCompletedOrders(riderId: String) = orderRepository.getCompletedOrders(riderId, viewModelScope.coroutineContext + Dispatchers.IO)

   // fun getAcceptedOrders(riderId: String) = orderRepository.getAcceptedOrders(riderId, viewModelScope.coroutineContext + Dispatchers.IO)

    fun acceptOrder(riderId: String, orderId: String, requestAcceptOffer: RequestOrderAcceptedByRider) = orderRepository.acceptOrder(riderId, orderId, requestAcceptOffer, viewModelScope.coroutineContext + Dispatchers.IO)

    fun deliverOrder(orderId: String, requestAcceptOffer: RequestOrderAcceptedByRider) = orderRepository.deliverOrder(orderId, requestAcceptOffer, viewModelScope.coroutineContext + Dispatchers.IO)

    fun pickupOrder(orderId: String, requestAcceptOffer: RequestOrderAcceptedByRider) = orderRepository.pickupOrder(orderId, requestAcceptOffer, viewModelScope.coroutineContext + Dispatchers.IO)

    fun uploadMedia(file: File) = liveData(context = viewModelScope.coroutineContext + Dispatchers.IO + handler) {
        val filePart = MultipartBody.Part.createFormData("multipartFile", file.name, file.asRequestBody())
        Timber.i("Media uploadMedia")
        emit(orderRepository.uploadImage(filePart))
    }


    fun getCitywideOrder(riderId: String, orderId: String, request: RequestCitywideOrder) = orderRepository.acceptCityWideOrder(riderId, orderId, request, viewModelScope.coroutineContext + Dispatchers.IO)
}