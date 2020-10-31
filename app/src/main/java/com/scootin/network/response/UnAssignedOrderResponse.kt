package com.scootin.network.response


data class UnAssignedOrderResponse(
        val addressDetails: AddressDetails,
        val directOrder: Boolean,
        val expressDelivery: Boolean,
        val id: Long,
        val orderDate: OrderDate,
        val orderStatus: String,
        val rejectReason: String?,
        val totalAmount: Double,
        val userInfo: UserInfo
) {
    data class OrderDate(
        val epochSecond: Int,
        val nano: Int
    )
    

}