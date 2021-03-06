package vn.sunasterisk.music_70.data.remote

import org.json.JSONObject
import vn.sunasterisk.music_70.base.BaseAsyncTask
import vn.sunasterisk.music_70.data.model.Track

class TrackAsyncTask(val callback: TrackDataSource.TrackCallback<List<Track>>) :
    BaseAsyncTask<Track>() {
    override fun convertJsonToObject(response: String): List<Track> {
        val tracks = arrayListOf<Track>()
        try {
            val objectRoot = JSONObject(response)
            val collections = objectRoot.getJSONArray(TrackAttributes.COLLECTION)
            for (i in 0 until collections.length()) {
                val trackList = collections.getJSONObject(i)
                val trackObject = trackList.getJSONObject(TrackAttributes.TRACK)
                val track = Track(trackObject)
                tracks.add(track)
            }
        } catch (e: Exception) {
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
