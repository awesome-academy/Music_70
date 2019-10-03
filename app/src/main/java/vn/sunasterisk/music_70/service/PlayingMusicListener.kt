package vn.sunasterisk.music_70.service

import vn.sunasterisk.music_70.data.model.Track
import vn.sunasterisk.music_70.util.StateType

interface PlayingMusicListener {
    fun onPlayingStateListener(@StateType state: Int)

    fun onTrackChangedListener(track: Track)
}
