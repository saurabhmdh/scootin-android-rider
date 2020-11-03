package com.scootin.network.request


data class RequestOrderAcceptedByRider(
    val type: String,
    val accepted: Boolean
)