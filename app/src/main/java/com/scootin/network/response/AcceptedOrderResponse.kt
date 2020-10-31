package com.scootin.network.response

data class AcceptedOrderResponse (
    val addressDetails: Address,
    val directOrder: Boolean,
    val expressDelivery: Boolean,
    val id: Long,
    val orderDate: OrderDate,
    val orderStatus: String,
    val rejectReason: Any,
    val totalAmount: Double,
    val userInfo: UserInfo
)
data class Address(
    val addressLine1: String,
    val addressLine2: String,
    val address_type: String,
    val city: String,
    val deleted: Boolean,
    val hasDefault: Boolean,
    val id: Int,
    val pincode: String,
    val stateDetails: StateDetails
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
//data class UserInfo(
//    val active: Boolean,
//    val createdAt: Long,
//    val deleted: Boolean,
//    val email: Any,
//    val fcmId: String,
//    val firstName: Any,
//    val id: Int,
//    val lastName: Any,
//    val mobileNumber: String,
//    val modified: Long,
//    val otp: String,
//    val otpExpireTime: Long,
//    val password: Any,
//    val profilePicture: Any,
//    val walletInfoDetails: WalletInfoDetails
//){
//    data class WalletInfoDetails(
//        val balance: Long,
//        val currency: String,
//        val id: Int,
//        val lastUpdated: OrderDate,
//        val lastUpdatedBy: String
//    )
//}
data class OrderDate(
    val epochSecond: Int,
    val nano: Int
)
