package vn.sunasterisk.music_70.service

import vn.sunasterisk.music_70.data.model.Track

interface MediaPlayerListener {
    fun createMediaPlayer(track: Track, listener: HandlerListenerPlayMusic)
    fun start()
    fun pause()
    fun stop()
    fun release()
    fun seek(position: Int)
    fun getDuration() : Int
    fun getCurrentPosition(): Int
}
