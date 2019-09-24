package vn.sunasterisk.music_70.service

import android.media.MediaPlayer

interface HandlerListenerPlayMusic: MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener,
    MediaPlayer.OnPreparedListener
