package com.scootin.repository

import androidx.lifecycle.LiveData
import com.scootin.network.api.APIService
import com.scootin.network.api.NetworkBoundResource
import com.scootin.network.api.Resource
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

}