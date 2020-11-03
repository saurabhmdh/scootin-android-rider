package com.scootin.repository

import androidx.lifecycle.LiveData
import com.scootin.network.api.APIService
import com.scootin.network.api.NetworkBoundResource
import com.scootin.network.api.Resource
import com.scootin.network.request.RequestOrderAcceptedByRider
import com.scootin.network.response.DirectOrderResponse
import com.scootin.network.response.NormalOrderResponse
import com.scootin.network.response.OrderListResponse
import com.scootin.network.response.UnAssignedOrderResponse
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Singleton
class OrderRepository @Inject constructor(
    private val services: APIService
) {

    fun getAllUnAssigned(
        context: CoroutineContext
    ): LiveData<Resource<List<UnAssignedOrderResponse>>> = object : NetworkBoundResource<List<UnAssignedOrderResponse>>(context) {
        override suspend fun createCall() = services.getAllUnAssigned()
    }.asLiveData()


    fun getAcceptedOrders(
        riderId: String,
        context: CoroutineContext
    ): LiveData<Resource<List<OrderListResponse>>> = object : NetworkBoundResource<List<OrderListResponse>>(context) {
        override suspend fun createCall() = services.getAcceptedOrders(riderId)
    }.asLiveData()

    fun getCompletedOrders(
        riderId: String,
        context: CoroutineContext
    ): LiveData<Resource<List<OrderListResponse>>> = object : NetworkBoundResource<List<OrderListResponse>>(context) {
        override suspend fun createCall() = services.getCompletedOrders(riderId)
    }.asLiveData()

    fun getOrder(id: Long, context: CoroutineContext):
            LiveData<Resource<NormalOrderResponse>> = object : NetworkBoundResource<NormalOrderResponse>(context) {
        override suspend fun createCall() = services.getOrderDetail(id)
    }.asLiveData()

    fun getDirectOrder(id: Long, context: CoroutineContext):
            LiveData<Resource<DirectOrderResponse>> = object : NetworkBoundResource<DirectOrderResponse>(context) {
        override suspend fun createCall() = services.getDirectOrderDetail(id)
    }.asLiveData()


    fun acceptOrder(riderId: String, orderId: String, requestOrderAcceptedByRider: RequestOrderAcceptedByRider, context: CoroutineContext):
            LiveData<Resource<String>> = object : NetworkBoundResource<String>(context) {
        override suspend fun createCall() = services.acceptOrder(riderId, orderId, requestOrderAcceptedByRider)
    }.asLiveData()
}