package com.scootin.repository

import androidx.lifecycle.LiveData
import com.scootin.network.api.APIService
import com.scootin.network.api.NetworkBoundResource
import com.scootin.network.api.Resource
import com.scootin.network.response.AcceptedOrderResponse
import retrofit2.Response
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class AcceptedOrdersRepository @Inject constructor(
    private val services: APIService
) {

    fun postAcceptedOrder(
        rider_id: String, context: CoroutineContext
    ): LiveData<Resource<List<AcceptedOrderResponse>>> = object : NetworkBoundResource<List<AcceptedOrderResponse>>(context) {
        override suspend fun createCall(): Response<List<AcceptedOrderResponse>> = services.postAcceptedOrder(rider_id)
    }.asLiveData()


}