package vn.sunasterisk.music_70.data.remote

import org.json.JSONArray
import vn.sunasterisk.music_70.base.BaseAsyncTask
import vn.sunasterisk.music_70.data.model.Track
import java.io.IOException

class SearchAsyncTask(private val callback: TrackDataSource.TrackCallback<List<Track>>) :
    BaseAsyncTask<Track>() {
    override fun convertJsonToObject(response: String): List<Track> {
        val tracks = arrayListOf<Track>()
        try {
            val collections = JSONArray(response)
            for (i in 0 until collections.length()) {
                val trackObject = collections.getJSONObject(i)
                val track = Track(trackObject)
                tracks.add(track)
            }
        } catch (e: IOException) {
            callback.onLoadTracksFail(e)
        }
        return tracks
    }

    override fun onPostExecute(result: List<Track>?) {
        super.onPostExecute(result)
        if (result == null) callback.onLoadTracksFail(Exception())
        else callback.onLoadTracksSuccess(result)
    }
}
