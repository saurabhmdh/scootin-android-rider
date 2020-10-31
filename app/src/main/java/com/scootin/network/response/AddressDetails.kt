package com.scootin.network.response

data class AddressDetails(
    val addressLine1: String,
    val addressLine2: String?,
    val addressType: String?,
    val city: String?,
    val id: Long,
    val pincode: String,
    val stateDetails: StateDetails,
    val userInfo: UserInfo
) {
    data class StateDetails(
        val countryDetails: CountryDetails,
        val id: Int,
        val name: String
    ) {
        data class CountryDetails(
            val id: Int,
            val name: String
        )
    }
}