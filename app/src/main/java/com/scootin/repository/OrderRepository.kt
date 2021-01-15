package com.scootin.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.scootin.network.api.APIService
import com.scootin.network.api.NetworkBoundResource
import com.scootin.network.api.Resource
import com.scootin.network.request.RequestCitywideOrder
import com.scootin.network.request.RequestOrderAcceptedByRider
import com.scootin.network.response.*
import com.scootin.pages.AcceptedOrderDataSource
import com.scootin.pages.CompletedOrderDataSource
import com.scootin.pages.UnassignedOrderDataSource
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Singleton
class OrderRepository @Inject constructor(
    private val services: APIService
) {

    fun getAllUnAssigned(riderId: String): Flow<PagingData<UnAssignedOrderResponse>> {
        return Pager(config = PagingConfig(pageSize = 20, initialLoadSize = 20)) {
            UnassignedOrderDataSource(services, riderId)
        }.flow
    }

    fun getAcceptedOrders(riderId: String): Flow<PagingData<OrderListResponse>> {
        return Pager(config = PagingConfig(pageSize = 20, initialLoadSize = 20)) {
            AcceptedOrderDataSource(services,riderId)
        }.flow
    }

    fun getCompletedOrders(riderId: String): Flow<PagingData<OrderListResponse>> {
        return Pager(config = PagingConfig(pageSize = 20, initialLoadSize = 20)) {
            CompletedOrderDataSource(services,riderId)
        }.flow
    }

    fun getOrder(id: Long, context: CoroutineContext):
            LiveData<Resource<NormalOrderResponse>> = object : NetworkBoundResource<NormalOrderResponse>(context) {
        override suspend fun createCall() = services.getOrderDetail(id)
    }.asLiveData()

    fun getDirectOrder(id: Long, context: CoroutineContext):
            LiveData<Resource<DirectOrderResponse>> = object : NetworkBoundResource<DirectOrderResponse>(context) {
        override suspend fun createCall() = services.getDirectOrderDetail(id)
    }.asLiveData()

    fun getCityWideOrder(
        orderId: Long,
        context: CoroutineContext
    ): LiveData<Resource<CityWideOrderResponse>> = object : NetworkBoundResource<CityWideOrderResponse>(context) {
        override suspend fun createCall(): Response<CityWideOrderResponse> =
            services.getCityWideOrder(orderId)
    }.asLiveData()

    suspend fun uploadImage(filePart: MultipartBody.Part) = services.uploadImage(filePart)

    fun acceptOrder(riderId: String, orderId: String, requestOrderAcceptedByRider: RequestOrderAcceptedByRider, context: CoroutineContext):
            LiveData<Resource<String>> = object : NetworkBoundResource<String>(context) {
        override suspend fun createCall() = services.acceptOrder(riderId, orderId, requestOrderAcceptedByRider)
    }.asLiveData()

    fun deliverOrder(orderId: String, requestOrderAcceptedByRider: RequestOrderAcceptedByRider, context: CoroutineContext):
            LiveData<Resource<String>> = object : NetworkBoundResource<String>(context) {
        override suspend fun createCall() = services.deliverOrder(orderId, requestOrderAcceptedByRider)
    }.asLiveData()


    fun pickupOrder(orderId: String, requestOrderAcceptedByRider: RequestOrderAcceptedByRider, context: CoroutineContext):
            LiveData<Resource<String>> = object : NetworkBoundResource<String>(context) {
        override suspend fun createCall() = services.pickupOrder(orderId, requestOrderAcceptedByRider)
    }.asLiveData()

    fun countDeliverOrders(orderId: String, context: CoroutineContext):
            LiveData<Resource<String>> = object : NetworkBoundResource<String>(context) {
        override suspend fun createCall() = services.countDeliverOrders(orderId)
    }.asLiveData()

    fun countReceivedOrders(orderId: String, context: CoroutineContext):
            LiveData<Resource<String>> = object : NetworkBoundResource<String>(context) {
        override suspend fun createCall() = services.countReceivedOrders(orderId)
    }.asLiveData()


    fun acceptCityWideOrder(riderId: String, orderId: String, request: RequestCitywideOrder, context: CoroutineContext):
            LiveData<Resource<CityWideOrderResponse>> = object : NetworkBoundResource<CityWideOrderResponse>(context) {
        override suspend fun createCall() = services.acceptCityWideOrder(riderId, orderId, request)
    }.asLiveData()
}