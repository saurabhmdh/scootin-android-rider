package com.scootin.network.response

data class NormalOrderResponse(
    val orderDetails: OrderDetails,
    val orderInventoryDetailsList: List<OrderInventoryDetails>
) {
    data class OrderInventoryDetails(
        val amount: Double,
        val deliveryFreeAmount: Double,
        val id: Long,
        val inventoryDetails: InventoryDetails,
        val orderDetails: OrderDetails,
        val quantity: Int,
        val totalAmount: Double,
        val totalGSTAmount: Double
    ) {
        data class InventoryDetails(
            val description: String,
            val id: Long,
            val price: Double,
            val merchantPrice: Double,
            val productImage: Media,
            val quantity: Int,
            val taxedGST: String,
            val title: String,
            val shopManagement: ShopManagement
        )
    }
}