package vn.sunasterisk.music_70.service

import android.app.IntentService
import android.content.Context
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import vn.sunasterisk.music_70.data.model.Track
import vn.sunasterisk.music_70.util.DownloadUtils
import vn.sunasterisk.music_70.util.StringUtils

class DownloadIntentService : IntentService("DownloadIntentService") {

    companion object {

        private const val ACTION_DOWNLOAD = "ACTION_DOWNLOAD"

        const val EXTRA_TRACK = "EXTRA_TRACK"

        const val DOWNLOAD_COMPLETE = "DOWNLOAD_COMPLETE"

        fun startActionDownload(context: Context, track: Track) {
            val intent = Intent(context, DownloadIntentService::class.java).apply {
                action = ACTION_DOWNLOAD
                putExtra(EXTRA_TRACK, track)
            }
            context.startService(intent)
        }
    }

    override fun onHandleIntent(intent: Intent?) {
        when (intent?.action) {
            ACTION_DOWNLOAD -> {
                val track = intent.getParcelableExtra(EXTRA_TRACK) as Track
                DownloadUtils.createNotification(applicationContext, track)
                handleActionDownload(track)
            }
        }
    }

    private fun handleActionDownload(track: Track) {
        if (DownloadUtils.download(
                applicationContext,
                StringUtils.generateStreamUrl(track.id),
                track
            )
        ) {
            broadcastDownloadComplete(track)
        }
    }

    private fun broadcastDownloadComplete(track: Track) {
        val intent = Intent(DOWNLOAD_COMPLETE)
        intent.putExtra(EXTRA_TRACK, track)
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        DownloadUtils.cancel()
    }
}
