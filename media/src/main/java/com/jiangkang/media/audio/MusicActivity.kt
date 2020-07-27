package com.jiangkang.media.audio

import android.content.ComponentName
import android.content.Context
import android.media.AudioManager
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import com.jiangkang.media.R

class MusicActivity : AppCompatActivity() {

    private lateinit var mMediaBrowser: MediaBrowserCompat

    private lateinit var mMusicControllerCallback: MusicControllerCallback

    private lateinit var mMusicConnectionCallback: MusicConnectionCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music)

        mMusicControllerCallback = MusicControllerCallback()
        mMusicConnectionCallback = MusicConnectionCallback(this, mMediaBrowser, mMusicControllerCallback)

        mMediaBrowser = MediaBrowserCompat(
                this,
                ComponentName(this, MusicService::class.java),
                mMusicConnectionCallback,
                null
        )
    }

    override fun onStart() {
        super.onStart()
        mMediaBrowser.connect()
    }

    override fun onResume() {
        super.onResume()
        volumeControlStream = AudioManager.STREAM_MUSIC
    }

    override fun onStop() {
        super.onStop()
        MediaControllerCompat
                .getMediaController(this)
                ?.unregisterCallback(mMusicControllerCallback)
        mMediaBrowser.disconnect()
    }

}


class MusicConnectionCallback(var context: Context, var browser: MediaBrowserCompat, private val musicControllerCallback: MusicControllerCallback) : MediaBrowserCompat.ConnectionCallback() {

    override fun onConnected() {
        browser.sessionToken.also { token ->
            val mediaController = MediaControllerCompat(context, token)
            MediaControllerCompat.setMediaController(context as MusicActivity, mediaController)
        }
        buildTransportControls()
    }

    private fun buildTransportControls() {
        val activity = context as MusicActivity
        val mediaController = MediaControllerCompat.getMediaController(activity)

        val mBtnPlayPause = activity.findViewById<Button>(R.id.btn_play_pause)
        mBtnPlayPause.setOnClickListener {
            val pbState = mediaController.playbackState.state
            if (pbState == PlaybackStateCompat.STATE_PLAYING) {
                mediaController.transportControls.pause()
            } else {
                mediaController.transportControls.play()
            }
        }

        //display a init state
        val metadata = mediaController.metadata
        val pbState = mediaController.playbackState

        mediaController.registerCallback(musicControllerCallback)
    }

}


class MusicControllerCallback : MediaControllerCompat.Callback() {

    override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
        super.onMetadataChanged(metadata)
    }

    override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
        super.onPlaybackStateChanged(state)
    }
    
}