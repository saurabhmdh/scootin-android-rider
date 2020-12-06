package com.scootin.network.response

data class PaymentDetails(
    val id: Int,
    val totalAmount: Double,
    val orderReference: String,
    val paymentMode: String?,
    val paymentReferer: String,
    val paymentStatus: String,
    val deliveryFreeAmount: Int,
    val totalGSTAmount: Double
)