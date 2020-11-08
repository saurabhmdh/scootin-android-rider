package com.scootin.util.constants

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat



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


    fun moveToMapWithDirection(context: Context, address: String) {
        val gmmIntentUri = Uri.parse("google.navigation:q=${address}&mode=l")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        ContextCompat.startActivity(context, mapIntent, null)
    }

    fun makeCall(context: Context, mobileNumber: String) {
        val intent = Intent()
        intent.action = Intent.ACTION_DIAL // Action for what intent called for
        intent.data = Uri.parse("tel: $mobileNumber") // Data with intent respective action on intent
        ContextCompat.startActivity(context, intent, null)
    }
}