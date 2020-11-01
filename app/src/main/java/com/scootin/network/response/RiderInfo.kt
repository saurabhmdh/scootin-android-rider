package com.scootin.network.response

 data class RiderInfo(
    val active: Boolean,
    val address: AddressDetails,
    val deleted: Boolean,
    val email: String,
    val fcm_id: String,
    val first_name: String,
    val gender: String,
    val id: Int,
    val last_name: String,
    val mobile_number: String,
    val password: String,
    val profilePictureReference: ProfilePictureReference,
    val riderActiveForOrders: Boolean,
    val serviceID: ServiceID
) {
    data class ProfilePictureReference(
        val deleted: Boolean,
        val filename: String,
        val id: Int,
        val type: String,
        val url: String
    )

    data class ServiceID(
        val deleted: Boolean,
        val id: Int,
        val latitude: Double,
        val longitude: Double,
        val name: String,
        val serviceRadius: Int
    )
}