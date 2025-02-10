package com.legalist.common.utils

import android.content.Context
import android.widget.ImageView
import androidx.annotation.DrawableRes
import coil.load
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso


object ImageDLoader {

    fun loadCircularImage(context: Context, imageUrl: String?, imageView: ImageView) {
        Glide.with(context)
            .load(imageUrl)
            .circleCrop()
            .into(imageView)
    }

    fun loadImage(
        imageView: ImageView,
        imageUrl: String?,
        placeholderResId: Int,
        errorResId: Int
    ) {
        imageUrl?.let {
            imageView.load(it) {
                placeholder(placeholderResId)
                error(errorResId)
            }
        }
    }
    fun loadImage2(
        imageView: ImageView,
        imageUrl: String?,
        @DrawableRes placeholder: Int = android.R.drawable.progress_indeterminate_horizontal, // Placeholder şəkli
        @DrawableRes errorImage: Int = android.R.drawable.stat_notify_error // Error şəkli
    ) {
        Picasso.get()
            .load(imageUrl)
            .placeholder(placeholder)
            .error(errorImage)
            .into(imageView)
    }

}