package com.baiganov.devlife.bindingadapters

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.baiganov.devlife.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

object PostItemBinding {


    @BindingAdapter("loadImageFromUrl")
    @JvmStatic
    fun loadImageFromUrl(imageView: ImageView, imageUrl: String) {
        Glide.with(imageView.context).asGif()
            .load(imageUrl)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .centerCrop()
            .placeholder(R.drawable.ic_place_holder)
            .into(imageView)
    }
}