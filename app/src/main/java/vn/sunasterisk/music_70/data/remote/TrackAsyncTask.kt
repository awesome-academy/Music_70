package vn.sunasterisk.music_70.data.remote

import org.json.JSONObject
import vn.sunasterisk.music_70.base.BaseAsyncTask
import vn.sunasterisk.music_70.data.model.Track
import vn.sunasterisk.music_70.util.StringUtils

class TrackAsyncTask(val callback: TrackDataSource.TrackCallback<List<Track>>): BaseAsyncTask<Track>(){
    override fun convertJsonToObject(response: String): List<Track> {
        val tracks = arrayListOf<Track>()
        try {
            val objectRoot = JSONObject(response)
            val collections = objectRoot.getJSONArray(TrackAttributes.COLLECTION)

            for (i in 0 until collections.length()) {
                val trackList = collections.getJSONObject(i)
                val trackObject = trackList.getJSONObject(TrackAttributes.TRACK)
                val id = trackObject.getInt(TrackAttributes.ID)
                val title = trackObject.getString(TrackAttributes.TITLE)
                val	artworkUrl = trackObject.getString(TrackAttributes.ARTWORK_URL)
                val duration = trackObject.getInt(TrackAttributes.DURATION)
                val downloadable = trackObject.getBoolean(TrackAttributes.DOWNLOADABLE)
                val downloadUrl = trackObject.getString(TrackAttributes.DOWNLOAD_URL)
                val streamUrl = StringUtils.generateStreamUrl(id)
                val username = trackObject.getJSONObject(TrackAttributes.USER).getString(TrackAttributes.USERNAME)
                var artist = ""
                if (!trackObject.isNull(TrackAttributes.PUBLISHER_METADATA)
                    && !trackObject.getJSONObject(TrackAttributes.PUBLISHER_METADATA)
                        .isNull(TrackAttributes.ARTIST)) {
                    artist = trackObject.getJSONObject(TrackAttributes.PUBLISHER_METADATA)
                        .getString(TrackAttributes.ARTIST)
                } else {
                    artist = username
                }
                val likeCount= trackObject.getInt(TrackAttributes.LIKE_COUNT)
                val description = trackObject.getString(TrackAttributes.DESCRIPTION)
                val track =
                    Track(id, title, artworkUrl, duration, downloadable, downloadUrl, streamUrl, username, artist,likeCount,description)
                tracks.add(track)
            }
        }catch (e: Exception){
            callback.onLoadTracksFail(e)
        }
        return tracks
    }

    override fun onPostExecute(result: List<Track>?) {
        super.onPostExecute(result)
        if (result == null) callback.onLoadTracksFail(java.lang.Exception())
        else callback.onLoadTracksSuccess(result)
    }
}
