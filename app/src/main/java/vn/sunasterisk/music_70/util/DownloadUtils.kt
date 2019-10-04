package vn.sunasterisk.music_70.util

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import vn.sunasterisk.music_70.R
import vn.sunasterisk.music_70.constant.Constant
import vn.sunasterisk.music_70.data.model.Track
import java.io.*
import java.net.URL

object DownloadUtils {
    @SuppressLint("StaticFieldLeak")
    private lateinit var notificationBuilder: NotificationCompat.Builder
    private lateinit var notificationManager: NotificationManager
    private const val MAX_PROGRES = 100
    private const val SIZE_BYTE = 1024
    private const val DOWNLOADING = "Downloading"
    private const val NOTIFICATION_CHANNEL_ID = "download_music"
    private const val NOTIFICATION_CHANNEL_NAME = "Donwload Music"
    private const val DOWNLOAD_COMPLETE = "Download Complete "
    private const val DOWNLOAD_ERROR = "Download Error "
    private const val DONWLOAD_PREPAIR = "Preparing download "
    fun createNotification(context: Context, track: Track) {
        notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = context.getString(R.string.app_name)
                setSound(null, null)
                enableLights(false)
                lightColor = Color.BLUE
                enableVibration(false)
            }
            notificationManager.createNotificationChannel(notificationChannel)
        }
        notificationBuilder =
            NotificationCompat.Builder(context.applicationContext, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(android.R.drawable.stat_sys_download)
                .setContentTitle(StringUtils.appendString(DONWLOAD_PREPAIR, track.title))
                .setDefaults(0)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setOngoing(true)
                .setOnlyAlertOnce(true)
                .setProgress(MAX_PROGRES, 0, true)
                .setAutoCancel(true)
        notificationManager.notify(0, notificationBuilder.build())
    }

    private fun updateNotification(currentProgress: Int, nameSong: String) {
        notificationBuilder.setProgress(MAX_PROGRES, currentProgress, false)
        notificationBuilder.setContentTitle(DOWNLOADING)
        notificationBuilder.setContentText("$nameSong: $currentProgress%")
        notificationManager.notify(0, notificationBuilder.build())
    }

    fun download(context: Context, urlString: String, track: Track): Boolean {
        try {
            val songDir = trackDirectory(context)
            if (!songDir!!.exists()) {
                songDir.mkdirs()
            }
            val url = URL(urlString)
            val connection = url.openConnection()
            val length = connection.contentLength
            val input: InputStream = BufferedInputStream(connection.getInputStream())
            val output: OutputStream = FileOutputStream(trackFile(context, track.id))
            val data = ByteArray(SIZE_BYTE)
            var count = input.read(data)
            var total = count
            while (count != -1) {
                val persente = total * MAX_PROGRES / length
                updateNotification(persente, track.title)
                output.write(data, 0, count)
                count = input.read(data)
                total += count
            }
            onDownloadComplete(true, track.title)
            output.flush()
            output.close()
            input.close()
            return true
        } catch (e: Exception) {
            onDownloadComplete(false, track.title)
            return false
        }
    }

    private fun onDownloadComplete(isDownloadAndSaveSuccess: Boolean, nameSong: String) {
        if (isDownloadAndSaveSuccess) {
            notificationBuilder.setContentText(
                StringUtils.appendString(
                    DOWNLOAD_COMPLETE,
                    nameSong
                )
            )
        } else {
            notificationBuilder.setContentText(StringUtils.appendString(DOWNLOAD_ERROR, nameSong))
        }
        notificationBuilder.setContentTitle(null)
        notificationManager.cancel(0)
        notificationBuilder.setProgress(0, 0, false)
        notificationBuilder.setOngoing(false)
        notificationManager.notify(0, notificationBuilder.build())
    }

    fun cancel() {
        if (::notificationManager.isInitialized) notificationManager.cancel(0)
    }

    private fun trackDirectory(context: Context) =
        context.applicationContext.getDir(Constant.TRACKS_DIRECTORY, Context.MODE_PRIVATE)

    fun trackFile(context: Context, id: Int) = File(trackDirectory(context), "$id")
}
