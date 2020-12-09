package com.scootin.network.response

data class OrderListResponse(
    val addressDetails: AddressDetails,
    val directOrder: Boolean,
    val expressDelivery: Boolean,
    val id: Long,
    val orderDate: String,
    val orderStatus: String,
    val rejectReason: String,
    val totalAmount: Double,
    val userInfo: UserInfo,
    val orderType: String
)