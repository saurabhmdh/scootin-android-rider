package com.scootin.network.response

data class AllOrdersList (
    val order_id:String,
    val order_status:Boolean,
    val order_date:String,
    val id:Int
)