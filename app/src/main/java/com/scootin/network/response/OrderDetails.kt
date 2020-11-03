package com.scootin.network.response

data class OrderDetails(
    val addressDetails: AddressDetails,
    val deliveryDetails: DeliveryDetail?,
    val expressDelivery: Boolean,
    val id: Long,
    val orderDate: Long,
    val orderStatus: String,
    val paymentDetails: PaymentDetails,
    val rejectReason: String,
    val reviewDoneIndicator: Boolean,
    val userInfo: UserInfo,
    val totalAmount: Double
) {
    data class PaymentDetails(
        val amount: Double,
        val deliveryFreeAmount: Double,
        val id: Long,
        val orderReference: String,
        val paymentMode: String,
        val paymentStatus: String,
        val totalAmount: Double,
        val totalGSTAmount: Double
    )
}