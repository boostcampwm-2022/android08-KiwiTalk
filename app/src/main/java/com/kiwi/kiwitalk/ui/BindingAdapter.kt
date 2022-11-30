package com.kiwi.kiwitalk.ui

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.kiwi.kiwitalk.R

@BindingAdapter("loadImageByUri")
fun setImage(imageView: ImageView, uri: String?) {
    Glide.with(imageView.context)
        .load(uri)
        .placeholder(R.drawable.ic_baseline_cloud_sync_24)
        .error(R.drawable.logo_splash_transparent)
        .fallback(R.drawable.ic_baseline_cloud_sync_24)
        .fitCenter()
        .into(imageView)
}