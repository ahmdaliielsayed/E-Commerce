package com.ahmdalii.ecommerce.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.ahmdalii.ecommerce.R
import com.bumptech.glide.Glide

@BindingAdapter("app:bindImgUrl")
fun ImageView.setBindImage(url: String?) {
    if (url.isNullOrEmpty()) {
        this.setImageResource(R.drawable.image)
    } else {
        Glide
            .with(context)
            .load(url)
            .placeholder(R.drawable.no_category)
            .error(R.drawable.no_category)
            .into(this)
    }
}