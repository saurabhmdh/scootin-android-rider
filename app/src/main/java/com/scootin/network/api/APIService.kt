package com.scootin.network.api

import com.scootin.network.RequestFCM
import com.scootin.network.RequestHistory
import com.scootin.network.manager.AppHeaders
import com.scootin.network.request.RequestActive
import com.scootin.network.request.RequestCitywideOrder
import com.scootin.network.request.RequestOrderAcceptedByRider
import com.scootin.network.request.RiderLocationDTO
import com.scootin.network.response.*
import okhttp3.MultipartBody


import retrofit2.Response
import retrofit2.http.*

interface APIService {

    @POST("auth/login/rider")
    suspend fun doLogin(
        @Body options: Map<String, String>,
        @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()
    ): Response<ResponseUser>

    @POST("/auth/refresh/rider")
    suspend fun refreshToken(
        @Body options: Map<String, String>,
        @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()
    ): Response<ResponseUser>

    @POST("/notification/rider/{id}/update-fcm")
    suspend fun updateFCMID(
        @Path("id") userMobileNumber: String,
        @Body requestFCM: RequestFCM,
        @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()
    ): Response<ResponseUser>

    @POST("/register/rider/active-for-order/{riderId}")
    suspend fun updateStatus(
        @Path("riderId") riderId: String,
        @Body requestActive: RequestActive,
        @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()
    ): Response<ResponseUser>

    @POST("/register/rider/update-location/{riderId}")
    suspend fun updateLocation(
        @Path("riderId") riderId: String,
        @Body requestActive: RiderLocationDTO,
        @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()
    ): Response<ResponseUser>

    @Multipart
    @POST("/media/upload-image-android")
    suspend fun uploadImage(
        @Part multipartFile: MultipartBody.Part,
        @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()
    ): Response<Media>


    @GET("order/orders/get-order/{orderId}")
    suspend fun getOrderDetail(
        @Path("orderId") orderId: Long,
        @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()
    ) : Response<NormalOrderResponse>


    @GET("order/orders/get-direct-order/{orderId}")
    suspend fun getDirectOrderDetail(
        @Path("orderId") orderId: Long,
        @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()
    ) : Response<DirectOrderResponse>


    @GET("register/rider/get-info/{riderId}")
    suspend fun getRiderInfo(
        @Path("riderId") riderId: String,
        @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()
    ) : Response<RiderInfo>


    @GET("/order/orders/get-city-wide-order/{id}")
    suspend fun getCityWideOrder(
        @Path("id") id: Long,
        @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()
    ): Response<CityWideOrderResponse>


    @POST("/order/orders/accept-city-wide-order-by-rider/{userId}/{orderId}")
    suspend fun acceptCityWideOrder(
        @Path("userId") userId: String,
        @Path("orderId") orderId: String,
        @Body request: RequestCitywideOrder,
        @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()
    ): Response<CityWideOrderResponse>


    @POST("/order/orders/accept-order-by-rider/{riderId}/{orderId}")
    suspend fun acceptOrder(
        @Path("riderId") riderId: String,
        @Path("orderId") orderId: String,
        @Body requestOrderAcceptedByRider: RequestOrderAcceptedByRider,
        @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()
    ): Response<String>

    @POST("/order/orders/deliver-order-by-rider/{orderId}")
    suspend fun deliverOrder(
        @Path("orderId") orderId: String,
        @Body requestOrderAcceptedByRider: RequestOrderAcceptedByRider,
        @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()
    ): Response<String>


    @POST("/order/orders/pickup-order-by-rider/{orderId}")
    suspend fun pickupOrder(
        @Path("orderId") orderId: String,
        @Body requestOrderAcceptedByRider: RequestOrderAcceptedByRider,
        @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()
    ): Response<String>


    //History section -------------------- Start ----------------
    @POST("/order-history/rider/count/delivered/{riderId}")
    suspend fun countDeliverOrders(
        @Path("riderId") riderId: String,
        @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()
    ): Response<String>

    @POST("/order-history/rider/count/accepted/{riderId}")
    suspend fun countReceivedOrders(
        @Path("riderId") riderId: String,
        @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()
    ): Response<String>

    @POST("/order-history/riders/unassigned/{riderId}")
    suspend fun getAllUnAssigned(
        @Path("riderId") riderId: String,
        @Query("page") offset: Int = 0,
        @Query("size") limit: Int = 10,
        @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()
    ): Response<List<UnAssignedOrderResponse>>


    @POST("/order-history/riders/accepted/{riderId}")
    suspend fun getAcceptedOrders(
        @Path("riderId") riderId: String,
        @Query("page") offset: Int = 0,
        @Query("size") limit: Int = 10,
        @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()
    ): Response<List<OrderListResponse>>


    @POST("/order-history/riders/completed/{riderId}")
    suspend fun getCompletedOrders(
        @Path("riderId") riderId: String,
        @Query("page") offset: Int = 0,
        @Query("size") limit: Int = 10,
        @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()
    ): Response<List<OrderListResponse>>

    //History section -------------------- End ----------------
}