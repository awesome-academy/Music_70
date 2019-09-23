package vn.sunasterisk.music_70.util

import android.annotation.SuppressLint
import android.widget.ImageView
import androidx.annotation.IdRes
import com.bumptech.glide.Glide
import vn.sunasterisk.music_70.R

object LoadImage {
    fun loadImage(image: ImageView, link:String){
        Glide.with(image.context)
            .load(StringUtils.reformatImageUrl(link))
            .fallback(R.drawable.bg_default_image)
            .error(R.drawable.bg_default_image)
            .into(image)
    }
    @SuppressLint("ResourceType")
    fun loadImageFromDrawable(image: ImageView, @IdRes resourceId :Int?){
        Glide.with(image.context)
            .load(resourceId)
            .fallback(R.drawable.bg_default_image)
            .error(R.drawable.bg_default_image)
            .into(image)

    }
}
