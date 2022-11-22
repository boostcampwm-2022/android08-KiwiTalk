package com.kiwi.kiwitalk.ui

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.kiwi.kiwitalk.R

@BindingAdapter("loadImageByUri")
fun setImage(imageView: ImageView, uri: String?) {
    Glide.with(imageView.context)
        .load(uri)
        .placeholder(R.drawable.ic_launcher_background)
        .error(R.drawable.logo_splash_transparent)
        .fallback(R.drawable.ic_launcher_background)
        .fitCenter()
        .into(imageView)
}