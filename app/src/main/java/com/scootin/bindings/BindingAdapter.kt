package com.scootin.bindings

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.scootin.R
import com.scootin.network.glide.GlideApp

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

fun getDefaultImage() = RequestOptions().apply {
    placeholder(R.drawable.ic_placeholder)
    error(R.drawable.ic_placeholder)
}