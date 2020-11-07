package com.scootin.network.response

data class DirectOrderResponse(
    val addressDetails: AddressDetails,
    val expressDelivery: Boolean,
    val id: Long,
    val deliveryDetails: DeliveryDetail?,
    val media: Media,
    val orderDate: String,
    val order_status: String,
    val paymentDetails: PaymentDetails,
    val reviewDoneIndicator: Boolean,
    val shopManagement: ShopManagement,
    val userInfo: UserInfo,
    val orderDetails: OrderDetails,
    val amount: Int
) {

    data class PaymentDetails(
        val id: Int,
        val totalAmount: Double,
        val orderReference: String,
        val payment_mode: String,
        val payment_referer: String,
        val payment_status: String,
        val deliveryFreeAmount: Int
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