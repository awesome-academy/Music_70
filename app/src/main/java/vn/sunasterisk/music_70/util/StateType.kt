package vn.sunasterisk.music_70.util

import androidx.annotation.IntDef

@IntDef(StateType.PLAY, StateType.PAUSE, StateType.PREPARE)
annotation class StateType {
    companion object {
        const val PLAY = 0
        const val PAUSE = 1
        const val PREPARE = 2
    }
}
