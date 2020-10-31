package com.scootin.network.api

import com.scootin.network.RequestFCM
import com.scootin.network.RequestHistory
import com.scootin.network.manager.AppHeaders

import com.scootin.network.response.OrderListResponse

import com.scootin.network.response.AcceptedOrderResponse

import com.scootin.network.response.ResponseUser
import com.scootin.network.response.TempleInfo
import retrofit2.Response
import retrofit2.http.*

interface APIService {

    @GET("get-all")
    suspend fun getAllTemples(): Response<List<TempleInfo>>

    @POST("auth/login/rider")
    suspend fun doLogin(@Body options: Map<String, String>, @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()): Response<ResponseUser>

    @POST("/auth/refresh/rider")
    suspend fun refreshToken(@Body options: Map<String, String>, @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()): Response<ResponseUser>

    @POST("/notification/rider/{id}/update-fcm")
    suspend fun updateFCMID(@Path("id") userMobileNumber: String, @Body requestFCM: RequestFCM): Response<ResponseUser>

    @POST("/order-history/riders/accepted/{riderId}")
    suspend fun getAcceptedOrders(@Path("riderId") riderId: String, @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()): Response<List<OrderListResponse>>

    @POST("/order-history/riders/completed/{riderId}")
    suspend fun getCompletedOrders(@Path("riderId") riderId: String, @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()): Response<List<OrderListResponse>>

}