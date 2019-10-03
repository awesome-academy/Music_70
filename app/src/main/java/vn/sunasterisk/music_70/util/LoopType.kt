package vn.sunasterisk.music_70.util

import androidx.annotation.IntDef

@IntDef(
    LoopType.NO,
    LoopType.ONE,
    LoopType.ALL
)
annotation class LoopType {
    companion object {
        const val NO = 0
        const val ONE = 1
        const val ALL = 2
    }
}
