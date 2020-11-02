package com.scootin.util.constants

import android.net.Uri

object IntentConstants {


    private const val HTTPS = "https"
    private const val PROJECT_AUTH = "rider.scootin.com"

    fun openOrderDetail(orderId: String) = Uri.Builder().scheme(HTTPS)
        .authority(PROJECT_AUTH)
        .appendPath("order-detail")
        .appendPath(orderId)
        .build()

    fun openDirectOrderDetail(orderId: String) = Uri.Builder().scheme(HTTPS)
        .authority(PROJECT_AUTH)
        .appendPath("order-detail-direct")
        .appendPath(orderId)
        .build()

    fun openOrders() = Uri.Builder().scheme(HTTPS)
        .authority(PROJECT_AUTH)
        .appendPath("orders")
        .build()
}