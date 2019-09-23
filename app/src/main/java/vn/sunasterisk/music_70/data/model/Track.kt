package vn.sunasterisk.music_70.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Track(
    val id: Int,
    val title: String,
    val artworkUrl: String?,
    val duration: Int,
    val isDownload: Boolean? = null,
    val downloadUrl: String?,
    val streamUrl: String?,
    val username: String?,
    val artist: String?,
    val likesCount: Int = 0,
    val description: String
) : Parcelable
