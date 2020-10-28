package com.scootin.network.api

import com.scootin.network.manager.AppHeaders
import com.scootin.network.response.ResponseUser
import com.scootin.network.response.TempleInfo
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.POST

interface APIService {

    @GET("get-all")
    suspend fun getAllTemples(): Response<List<TempleInfo>>


    @POST("/auth/refresh/rider")
    suspend fun refreshToken(@Body options: Map<String, String>, @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()): Response<ResponseUser>
}