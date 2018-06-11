package com.example.a14512.theone.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.a14512.theone.R

/**
 * @author 14512 on 2018/6/12
 */
object GlideUtil {

    fun glidePortrait(context: Context, url: String?, iv: ImageView) {
        val options = RequestOptions().error(R.mipmap.default_portrait)
                .placeholder(R.mipmap.default_portrait)
        Glide.with(context).load(url).apply(options).into(iv)
    }

    fun glideImg(context: Context, url: String?, iv: ImageView) {
        val options = RequestOptions().error(R.mipmap.iv_load_failure)
                .placeholder(R.mipmap.iv_load_failure)
        Glide.with(context).load(url).apply(options).into(iv)
    }

}