package com.jiangkang.media.audio

import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.media.AudioManager.ACTION_AUDIO_BECOMING_NOISY
import android.os.Bundle
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import android.support.v4.media.MediaBrowserCompat
import androidx.media.MediaBrowserServiceCompat
import androidx.media.session.MediaButtonReceiver
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.jiangkang.media.R


private const val MEDIA_ROOT_ID = "media_root_id"
private const val EMPTY_MEDIA_ROOT_ID = "empty_root_id"

class MusicService : MediaBrowserServiceCompat() {

    private val TAG = "music_service"

    private val CHANNEL_ID = "music_service"

    private var mMediaSession: MediaSessionCompat? = null

    private lateinit var mStateBuilder: PlaybackStateCompat.Builder

    private val intentFilter = IntentFilter(ACTION_AUDIO_BECOMING_NOISY)

    private lateinit var afChangeListener:AudioManager.OnAudioFocusChangeListener

    private val myNoisyAudioStreamReceiver = BecomingNoisyReceiver()

    private val mMusicSessionCallback = object : MediaSessionCompat.Callback() {

        override fun onPlay() {
            super.onPlay()
        }

        override fun onPause() {
            super.onPause()
        }

        override fun onStop() {
            super.onStop()
        }

    }

    override fun onCreate() {
        super.onCreate()

        mMediaSession = MediaSessionCompat(baseContext, TAG).apply {

            mStateBuilder = PlaybackStateCompat.Builder()
                    .setActions(PlaybackStateCompat.ACTION_PLAY or PlaybackStateCompat.ACTION_PLAY_PAUSE)
            setPlaybackState(mStateBuilder.build())

            setCallback(mMusicSessionCallback)

            setSessionToken(sessionToken)
        }

        val controller = mMediaSession?.controller
        val mediaMetadata = controller?.metadata
        val description = mediaMetadata?.description

        val builder = NotificationCompat.Builder(baseContext, CHANNEL_ID).apply {

            //为当前播放曲目添加元数据
            setContentTitle(description?.title)
            setContentText(description?.subtitle)
            setSubText(description?.description)
            setLargeIcon(description?.iconBitmap)

            //允许点击notification启动player
            setContentIntent(controller?.sessionActivity)

            //notification被划掉的时候停止service
            setDeleteIntent(androidx.media.session.MediaButtonReceiver.buildMediaButtonPendingIntent(
                    baseContext,
                    PlaybackStateCompat.ACTION_STOP
            ))

            //锁屏的时候控制按钮可见
            setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

            //添加一个app icon，设置它的accent color
            setSmallIcon(R.drawable.ic_audio)
            color = ContextCompat.getColor(baseContext, R.color.colorPrimary)

            //添加一个暂停按钮
            addAction(NotificationCompat.Action(
                    R.drawable.ic_music_pause,
                    "Pause",
                    androidx.media.session.MediaButtonReceiver.buildMediaButtonPendingIntent(
                            baseContext,
                            PlaybackStateCompat.ACTION_PLAY_PAUSE
                    )
            ))

            //利用MediaStyle特性
            setStyle(
                    androidx.media.app.NotificationCompat.MediaStyle()
                            .setMediaSession(mMediaSession?.sessionToken)
                            .setShowActionsInCompactView(0)

                            //add a cancel button
                            .setShowCancelButton(true)
                            .setCancelButtonIntent(
                                    androidx.media.session.MediaButtonReceiver.buildMediaButtonPendingIntent(
                                            baseContext,
                                            PlaybackStateCompat.ACTION_STOP
                                    )
                            )

            )
        }

        startForeground(0, builder.build())
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


    override fun onLoadChildren(parentId: String, result: Result<MutableList<MediaBrowserCompat.MediaItem>>) {

        //不允许浏览
        if (EMPTY_MEDIA_ROOT_ID == parentId) {
            result.sendResult(null)
            return
        }

        val mediaItems = emptyList<MediaBrowserCompat.MediaItem>()
        if (MEDIA_ROOT_ID == parentId) {
            //根菜单,为顶层构建MediaItem对象，并放入到list中
        } else {
            //检查传递的parentID以查看我们所在的子菜单，然后将菜单的children放入到列表中
        }
        result.sendResult(mediaItems.toMutableList())

    }

    override fun onGetRoot(clientPackageName: String, clientUid: Int, rootHints: Bundle?): BrowserRoot {
        return if (allowBrowsing(clientPackageName, clientUid)) {
            MediaBrowserServiceCompat.BrowserRoot(MEDIA_ROOT_ID, null)
        } else {
            MediaBrowserServiceCompat.BrowserRoot(EMPTY_MEDIA_ROOT_ID, null)
        }
    }

    private fun allowBrowsing(clientPackageName: String, clientUid: Int): Boolean {
        return true
    }

}

class BecomingNoisyReceiver {

}
