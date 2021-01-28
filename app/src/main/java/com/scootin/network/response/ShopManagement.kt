package com.scootin.network.response

data class ShopManagement(
    val address: AddressDetails,
    val amount: Double,
    val closeTime: String,
    val deleted: Boolean,
    val discountType: String,
    val id: Long,
    val latitude: Double,
    val longitude: Double,
    val name: String,
    val openTime: String,
    val status: Boolean
)