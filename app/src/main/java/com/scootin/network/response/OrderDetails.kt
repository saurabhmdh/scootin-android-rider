package com.scootin.network.response

data class OrderDetails(
    val addressDetails: AddressDetails,
    val deliveryDetails: DeliveryDetail?,
    val expressDelivery: Boolean,
    val id: Long,
    val orderDate: String,
    val orderStatus: String,
    val paymentDetails: PaymentDetails,
    val rejectReason: String,
    val reviewDoneIndicator: Boolean,
    val userInfo: UserInfo,
    val totalAmount: Double
)