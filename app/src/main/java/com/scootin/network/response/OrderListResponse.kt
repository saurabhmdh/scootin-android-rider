package com.scootin.network.response

data class OrderListResponse(
    val addressDetails: AddressDetails,
    val directOrder: Boolean,
    val expressDelivery: Boolean,
    val id: Int,
    val orderDate: OrderDate,
    val orderStatus: String,
    val rejectReason: Any,
    val totalAmount: Double,
    val userInfo: UserInfo
) {
    data class OrderDate(
        val epochSecond: Int,
        val nano: Int
    )
}