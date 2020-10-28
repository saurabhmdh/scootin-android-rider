package com.scootin.repository

import androidx.lifecycle.LiveData
import com.scootin.network.RequestFCM
import com.scootin.network.api.APIService
import com.scootin.network.api.NetworkBoundResource
import com.scootin.network.api.Resource
import com.scootin.network.response.ResponseUser
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext


@Singleton
class UserRepository @Inject constructor(
    private val services: APIService
) {
    fun doLogin(
        options: Map<String, String>,
        context: CoroutineContext
    ): LiveData<Resource<ResponseUser>> = object : NetworkBoundResource<ResponseUser>(context) {
        override suspend fun createCall(): Response<ResponseUser> = services.doLogin(options)
    }.asLiveData()

    suspend fun updateFCMId(id: String, request: RequestFCM) = services.updateFCMID(id, request)
}