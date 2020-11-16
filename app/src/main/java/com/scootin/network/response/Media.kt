package com.scootin.network.response

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class Media(
    val id: Long,
    val thumb: String?,
    val type: String?,
    val url: String?,
    var colorTint: Int,
    var isWhite: Boolean
) : Parcelable