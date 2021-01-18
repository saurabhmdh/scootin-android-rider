package com.scootin.network.response

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