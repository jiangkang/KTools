package com.jiangkang.tools.service

import android.app.Service
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.IBinder
import android.provider.MediaStore

import com.jiangkang.tools.bean.Song
import com.jiangkang.tools.utils.LogUtils
import com.jiangkang.tools.utils.ToastUtils

import java.util.ArrayList

/**
 * Created by jiangkang on 2017/10/31.
 * description：扫描音乐的Service
 */

class ScanMusicService : Service() {

    override fun onCreate() {
        super.onCreate()

        //带限定符的this
        Thread(Runnable {
            val songs = scanSongs(this@ScanMusicService)
            LogUtils.d(songs)
            ToastUtils.showLongToast(songs.toString())
        }).start()

    }


    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }


    override fun onDestroy() {
        super.onDestroy()
    }


    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onUnbind(intent: Intent): Boolean {
        return super.onUnbind(intent)
    }


    private fun scanSongs(context: Context): ArrayList<Song> {
        val result = ArrayList<Song>()
        val resolver = context.contentResolver
        val cursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null)
        if (cursor != null && !cursor.isClosed && cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                val songName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
                val artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                val location = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))

                val song = Song()
                song.name = songName
                song.artist = artist
                song.location = location
                LogUtils.d(song.toString())
                result.add(song)
            }
        }
        return result
    }


}
