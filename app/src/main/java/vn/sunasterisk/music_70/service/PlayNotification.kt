package vn.sunasterisk.music_70.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import vn.sunasterisk.music_70.R
import vn.sunasterisk.music_70.data.model.Track
import vn.sunasterisk.music_70.data.remote.TrackAttributes
import vn.sunasterisk.music_70.ui.nowplaying.NowPlayingActivity

class PlayNotification {
    private var notificationManager: NotificationManager? = null
    private lateinit var service: MediaService
    private var stopped: Boolean = false
    fun update(currentTrack: Track, isPlaying: Boolean) {
        val notificationLayout = RemoteViews(service.packageName, R.layout.notification_small)
        val notificationLayoutBig = RemoteViews(service.packageName, R.layout.notification_big)
        initDataNotification(notificationLayout, currentTrack, isPlaying)
        initDataNotification(notificationLayoutBig, currentTrack, isPlaying)
        setOnAction(notificationLayout, notificationLayoutBig)
        val deleteIntent = buildPendingIntent(service, MediaService.ACTION_QUIT, null)
        val builder = NotificationCompat.Builder(service, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_my_music)
            .setContentIntent(getPendingIntentOpenApp(currentTrack))
            .setDeleteIntent(deleteIntent)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setContent(notificationLayout)
            .setCustomBigContentView(notificationLayoutBig)
            .setShowWhen(true)
        val bigNotificationImageSize = service.resources.getDimensionPixelSize(R.dimen.dp_128)
        Glide.with(service)
            .asBitmap()
            .load(currentTrack.artworkUrl)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.ic_my_music)
                    .error(R.drawable.ic_my_music)
            )
            .into(object :
                SimpleTarget<Bitmap>(bigNotificationImageSize, bigNotificationImageSize) {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    notificationLayout.setImageViewBitmap(R.id.imageSong, resource)
                    notificationLayoutBig.setImageViewBitmap(R.id.imageSong, resource)
                    Palette.Builder(resource).generate {
                        it?.let { palette ->
                            val dominantColor =
                                palette.getDominantColor(
                                    ContextCompat.getColor(
                                        service,
                                        R.color.color_accent
                                    )
                                )
                            builder.color = dominantColor
                        }
                    }
                }
            })

        val notification = builder.build()
        notificationManager?.notify(NOTIFICATION_ID, notification)
        service.startForeground(NOTIFICATION_ID, notification)

    }

    private fun getPendingIntentOpenApp(track: Track): PendingIntent {
        val intent = Intent(service, NowPlayingActivity::class.java).apply {
            putExtra(TrackAttributes.TRACK, track)
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        return TaskStackBuilder.create(service).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }
    }

    @Synchronized
    fun init(service: MediaService) {
        this.service = service
        notificationManager =
            service.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }
    }

    @Synchronized
    fun stop() {
        stopped = true
        if (::service.isInitialized) {
            service.stopForeground(true)
            notificationManager?.cancel(NOTIFICATION_ID)
        }
    }

    private fun initDataNotification(remoteViews: RemoteViews, track: Track, isPlaying: Boolean) {
        remoteViews.apply {
            setTextViewText(R.id.textSongName, track.title)
            setTextViewText(R.id.textArtist, track.artist)
            setImageViewResource(
                R.id.imagePlay,
                if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play
            )
        }
    }

    private fun setOnAction(notificationLayout: RemoteViews, notificationLayoutBig: RemoteViews) {
        var pendingIntent: PendingIntent
        val serviceName = ComponentName(service, MediaService::class.java)

        pendingIntent = buildPendingIntent(service, MediaService.ACTION_PREVIOUS, serviceName)
        notificationLayout.setOnClickPendingIntent(R.id.imagePrevious, pendingIntent)
        notificationLayoutBig.setOnClickPendingIntent(R.id.imagePrevious, pendingIntent)

        pendingIntent = buildPendingIntent(service, MediaService.ACTION_PLAY, serviceName)
        notificationLayout.setOnClickPendingIntent(R.id.imagePlay, pendingIntent)
        notificationLayoutBig.setOnClickPendingIntent(R.id.imagePlay, pendingIntent)

        pendingIntent = buildPendingIntent(service, MediaService.ACTION_NEXT, serviceName)
        notificationLayout.setOnClickPendingIntent(R.id.imageNext, pendingIntent)
        notificationLayoutBig.setOnClickPendingIntent(R.id.imageNext, pendingIntent)

        pendingIntent = buildPendingIntent(service, MediaService.ACTION_QUIT, serviceName)
        notificationLayoutBig.setOnClickPendingIntent(R.id.imageClear, pendingIntent)
    }

    private fun buildPendingIntent(
        context: Context,
        action: String,
        serviceName: ComponentName?
    ): PendingIntent {
        val intent = Intent(action)
        intent.component = serviceName
        return PendingIntent.getService(context, 0, intent, 0)
    }

    @RequiresApi(26)
    private fun createNotificationChannel() {
        var notificationChannel: NotificationChannel? =
            notificationManager?.getNotificationChannel(NOTIFICATION_CHANNEL_ID)
        if (notificationChannel == null) {
            notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                service.getString(R.string.string_notification_name),
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = service.getString(R.string.string_notification_description)
                enableLights(false)
                enableVibration(false)
            }
            notificationManager?.createNotificationChannel(notificationChannel)
        }
    }

    companion object {
        const val NOTIFICATION_ID = 1
        const val NOTIFICATION_CHANNEL_ID = "playing_notification"
    }
}
