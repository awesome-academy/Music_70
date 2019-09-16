package vn.sunasterisk.music_70.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Genre(val id: String, val nameGenre: String, val image: String) : Parcelable
