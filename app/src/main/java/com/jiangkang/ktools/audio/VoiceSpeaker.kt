package com.jiangkang.ktools.audio

import android.content.res.AssetFileDescriptor
import android.media.MediaPlayer
import com.jiangkang.tools.utils.FileUtils
import java.io.IOException
import java.util.concurrent.CountDownLatch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Created by jiangkang on 2017/10/17.
 */
object VoiceSpeaker {

    private val service: ExecutorService = Executors.newCachedThreadPool()

    fun speak(list: List<String?>?) {
        service.execute { start(list) }
    }

    private fun start(list: List<String?>?) {
        synchronized(this) {
            val latch = CountDownLatch(1)
            val player = MediaPlayer()
            if (list != null && list.isNotEmpty()) {
                val counter = intArrayOf(0)
                val path = String.format("sound/tts_%s.mp3", list[counter[0]])
                var fd: AssetFileDescriptor? = null
                try {
                    fd = FileUtils.getAssetFileDescription(path)
                    player.setDataSource(fd.fileDescriptor, fd.startOffset,
                            fd.length)
                    player.prepareAsync()
                    player.setOnPreparedListener { mp -> mp.start() }
                    player.setOnCompletionListener { mp ->
                        mp.reset()
                        counter[0]++
                        if (counter[0] < list.size) {
                            try {
                                val fileDescriptor = FileUtils.getAssetFileDescription(String.format("sound/tts_%s.mp3", list[counter[0]]))
                                mp.setDataSource(fileDescriptor.fileDescriptor, fileDescriptor.startOffset, fileDescriptor.length)
                                mp.prepare()
                            } catch (e: IOException) {
                                e.printStackTrace()
                                latch.countDown()
                            }
                        } else {
                            mp.release()
                            latch.countDown()
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                    latch.countDown()
                } finally {
                    if (fd != null) {
                        try {
                            fd.close()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }
            }
            try {
                latch.await()
                (this as Object).notifyAll()
            } catch (t: Throwable) {
                t.printStackTrace()
            }
        }
    }

}