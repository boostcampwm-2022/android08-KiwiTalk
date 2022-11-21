package com.kiwi.kiwitalk.ui

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.kiwi.kiwitalk.R

@BindingAdapter("image")
fun setImage(imageView: ImageView, uri: String?) {
    Glide.with(imageView.context)
        .load(uri)
        .placeholder(R.drawable.ic_launcher_background)
        .error(R.drawable.ic_launcher_background)
        .fallback(R.drawable.ic_launcher_background)
        .into(imageView)
}