package vn.sunasterisk.music_70.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.json.JSONObject
import vn.sunasterisk.music_70.data.remote.TrackAttributes
import vn.sunasterisk.music_70.util.StringUtils

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
    val description: String,
    val isOnline: Boolean = true
) : Parcelable {
    constructor(jsonObject: JSONObject) : this(
        jsonObject.getInt(TrackAttributes.ID),
        jsonObject.getString(TrackAttributes.TITLE),
        jsonObject.getString(TrackAttributes.ARTWORK_URL),
        jsonObject.getInt(TrackAttributes.DURATION),
        jsonObject.getBoolean(TrackAttributes.DOWNLOADABLE),
        jsonObject.getString(TrackAttributes.DOWNLOAD_URL),
        StringUtils.generateStreamUrl(jsonObject.getInt(TrackAttributes.ID)),
        jsonObject.getJSONObject(TrackAttributes.USER).getString(TrackAttributes.USERNAME),
        if (!jsonObject.isNull(TrackAttributes.PUBLISHER_METADATA)
            && !jsonObject.getJSONObject(TrackAttributes.PUBLISHER_METADATA)
                .isNull(TrackAttributes.ARTIST)
        ) {
            jsonObject.getJSONObject(TrackAttributes.PUBLISHER_METADATA)
                .getString(TrackAttributes.ARTIST)
        } else {
            jsonObject.getJSONObject(TrackAttributes.USER).getString(TrackAttributes.USERNAME)
        },
        jsonObject.getInt(TrackAttributes.LIKE_COUNT),
        jsonObject.getString(TrackAttributes.DESCRIPTION)
    )
}
