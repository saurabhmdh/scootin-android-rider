package com.scootin.network.response

data class DirectOrderResponse(
    val addressDetails: AddressDetails,
    val expressDelivery: Boolean,
    val id: Long,
    val deliveryDetails: DeliveryDetail?,
    val media: Media,
    val orderDate: String,
    val extraData: String,
    val orderStatus: String,
    val paymentDetails: PaymentDetails,
    val reviewDoneIndicator: Boolean,
    val shopManagement: ShopManagement,
    val userInfo: UserInfo,
    val orderDetails: OrderDetails,
    val amount: Double
)