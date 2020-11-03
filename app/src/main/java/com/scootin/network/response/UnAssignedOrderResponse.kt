package com.scootin.network.response


data class UnAssignedOrderResponse(
        val addressDetails: AddressDetails,
        val directOrder: Boolean,
        val expressDelivery: Boolean,
        val id: Long,
        val orderDate: Long,
        val orderStatus: String,
        val rejectReason: String?,
        val totalAmount: Double,
        val userInfo: UserInfo
)