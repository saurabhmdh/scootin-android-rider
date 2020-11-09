package com.scootin.view.adapter

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.scootin.databinding.AdapterGalleryBinding
import com.scootin.network.AppExecutors
import com.scootin.network.response.Media
import com.scootin.util.constants.AppConstants
import com.scootin.view.customview.PhotoViewAttacher
import okhttp3.internal.toHexString
import timber.log.Timber

class GalleryAdapter(
    appExecutors: AppExecutors,
    private val imageGalleyCallbacks: ImageGalleryCallback
) : ListAdapter<Media, RecyclerView.ViewHolder>(
    AsyncDifferConfig.Builder(DiffCallback())
        .setBackgroundThreadExecutor(appExecutors.diskIO())
        .build()
) {
    class DiffCallback : DiffUtil.ItemCallback<Media>() {
        override fun areItemsTheSame(
            oldItem: Media,
            newItem: Media
        ): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(
            oldItem: Media,
            newItem: Media
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MediaImageHolder -> {
                val item = getItem(position)
                holder.bind(item, imageGalleyCallbacks)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE -> MediaImageHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
    }

    class MediaImageHolder private constructor(
        val binding: AdapterGalleryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun isWhiteColor(@ColorInt textColor: Int) = textColor.toHexString().substring(2) == AppConstants.COLOR_WHITE

        fun bind(item: Media, imageGalleyCallbacks: ImageGalleryCallback) {
            Glide.with(binding.imageViewData.context).asBitmap().load(item.url)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
                    ) {
                        Timber.i("Setting image : $resource")
                        binding.imageViewData.setImageBitmap(resource)
                        Palette.from(resource).generate { palette ->
                            palette?.lightVibrantSwatch?.let {
                                imageGalleyCallbacks.onImageChanged(it.rgb)
                                binding.galleryContainer.setBackgroundColor(it.rgb)
                                Palette.from(resource).generate { palette ->
                                    palette?.lightVibrantSwatch?.let {
                                        imageGalleyCallbacks.onImageChanged(it.rgb)
                                        item.colorTint = it.rgb
                                        item.isWhite = isWhiteColor(it.bodyTextColor)
                                        binding.galleryContainer.setBackgroundColor(it.rgb)
                                    }
                                }
                            }
                        }
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {}
                })

            binding.imageViewData.setCallback(object :
                PhotoViewAttacher.OnScaleChangedCallBack {
                override fun onNormalScale() {
                    imageGalleyCallbacks.onImageScaleChanged(true)
                }

                override fun onScaleDowned() {}

                override fun onScaleUpped() {
                    imageGalleyCallbacks.onImageScaleChanged(false)
                }
            })
        }

        companion object {
            fun from(parent: ViewGroup): MediaImageHolder {
                val binding = AdapterGalleryBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                return MediaImageHolder(binding)
            }
        }
    }

    interface ImageGalleryCallback {
        fun onImageChanged(@ColorInt color: Int)
        fun onImageScaleChanged(isNormal: Boolean)
    }

}