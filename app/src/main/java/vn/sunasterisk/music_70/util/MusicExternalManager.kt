package vn.sunasterisk.music_70.util

import android.content.Context
import android.provider.BaseColumns
import android.provider.MediaStore
import vn.sunasterisk.music_70.data.model.Track

class MusicExternalManager(context: Context) {
    val externals by lazy { mutableListOf<Track>() }

    init { queyAllMusic(context) }

    private fun queyAllMusic(context: Context) {
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            BaseColumns._ID,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.ARTIST_ID,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DATE_MODIFIED
        )
        val cursor = context.contentResolver
            .query(uri, projection, null, null, null) ?: return
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            externals.add(Track(cursor))
            cursor.moveToNext()
        }
        cursor.close()
    }
}
