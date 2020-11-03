package com.scootin.network.response

data class DeliveryDetail(
    val deliveredDateTime: Long,
    val deliveryStatus: String,
    val id: Long,
    val orderId: Long,
    val orderType: String,
    val rejectReason: String,
    val riderInfo: RiderInfo
)