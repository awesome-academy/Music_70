package vn.sunasterisk.music_70.util

import androidx.annotation.IntDef

@IntDef(
		ShuffleType.YES,
		ShuffleType.NO
)
annotation class ShuffleType {
	companion object {
		const val YES = 1
		const val NO = 0
	}
}
