package com.scootin.network.response

data class DirectOrderResponse(
    val addressDetails: AddressDetails,
    val expressDelivery: Boolean,
    val id: Int,
    val media: Media,
    val orderDate: OrderDate,
    val order_status: String,
    val paymentDetails: PaymentDetails,
    val reviewDoneIndicator: Boolean,
    val shopManagement: ShopManagement,
    val userInfo: UserInfo,
    val orderDetails: OrderDetails
) {
    data class OrderDate(
        val nanos: Int,
        val seconds: Int
    )

    data class PaymentDetails(
        val id: Int,
        val orderReference: String,
        val payment_mode: String,
        val payment_referer: String,
        val payment_status: String
    )

    data class ShopManagement(
        val address: AddressDetails,
        val amount: Int,
        val closeTime: String,
        val createdAt: Long,
        val deleted: Boolean,
        val discountType: String,
        val id: Int,
        val latitude: Double,
        val longitude: Double,
        val modified: Long,
        val name: String,
        val openTime: String,

        val status: Boolean
    )
}