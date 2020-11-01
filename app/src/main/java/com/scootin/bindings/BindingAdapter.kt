package com.scootin.bindings

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.scootin.R
import com.scootin.network.glide.GlideApp
import com.scootin.network.response.AddressDetails
import java.lang.StringBuilder
import java.math.BigDecimal
import java.text.Format
import java.text.NumberFormat
import java.util.*

@BindingAdapter("visibleGone")
fun View.visibleGone(show: Boolean) {
    this.visibility = if (show) View.VISIBLE else View.GONE
}

@BindingAdapter("setImage")
fun ImageView.setImage(url: String?) {
    GlideApp.with(this.context).setDefaultRequestOptions(getDefaultImage())
        .load(url)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}
@BindingAdapter("setToIntText")
fun TextView.setToIntText(value: Long) {
    text = value.toString()
}
@BindingAdapter("setToInt")
fun TextView.setToInt(value: Int) {
    text = value.toString()
}
@BindingAdapter("setPrice")
fun TextView.setPrice(value: Double) {
    val format: Format = NumberFormat.getCurrencyInstance(Locale("en", "in"))
    val finalValue =  format.format(BigDecimal(value))

    text = finalValue
}
@BindingAdapter("setOneLineAddress")
fun TextView.setOneLineAddress(address: AddressDetails?) {
    if(address == null) return
    val sb = StringBuilder().append(address.addressLine1).append(", ")
        .append(address.addressLine2).append(", ").append(address.city).append(", ")
        .append(address.pincode)

    text = sb.toString()
}

fun getDefaultImage() = RequestOptions().apply {
    placeholder(R.drawable.ic_placeholder)
    error(R.drawable.ic_placeholder)
}