package com.example.trootechpractical.utils

import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.trootechpractical.R

class BindingUtils {
    companion object {
        @BindingAdapter("itemImage")
        @JvmStatic
        fun loadImage(view: AppCompatImageView, imageUrl: String) {
            Glide.with(view.getContext())
                .load(imageUrl)
                .error(R.drawable.ic_item_placeholder)
                .placeholder(R.drawable.ic_item_placeholder)
                .into(view)
        }
    }
}