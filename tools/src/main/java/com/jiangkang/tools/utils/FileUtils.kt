package com.jiangkang.tools.utils

import android.content.res.AssetFileDescriptor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import com.jiangkang.tools.King
import okhttp3.internal.closeQuietly
import java.io.*
import java.nio.channels.FileChannel
import java.util.concurrent.Executors
import kotlin.concurrent.thread

/**
 * Created by jiangkang on 2017/9/20.
 */
object FileUtils {
    fun getAssetsPath(filename: String): String {
        return "file:///android_asset/$filename"
    }

    @Throws(IOException::class)
    fun getInputStreamFromAssets(filename: String?): InputStream {
        val manager = King.applicationContext.assets
        return manager.open((filename)!!)
    }

    @Throws(IOException::class)
    fun listFilesFromPath(path: String?): Array<String>? {
        val manager = King.applicationContext.assets
        return manager.list((path)!!)
    }

    @Throws(IOException::class)
    fun getAssetFileDescription(filename: String?): AssetFileDescriptor {
        val manager = King.applicationContext.assets
        return manager.openFd((filename)!!)
    }

    fun writeStringToFile(string: String?, file: File?, isAppending: Boolean) {
        var writer: FileWriter? = null
        var bufferedWriter: BufferedWriter? = null
        try {
            writer = FileWriter(file)
            bufferedWriter = BufferedWriter(writer)
            if (isAppending) {
                bufferedWriter.append(string)
            } else {
                bufferedWriter.write(string)
            }
            bufferedWriter.flush()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                writer?.close()
                bufferedWriter?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun writeStringToFile(string: String?, filePath: String?, isAppending: Boolean) {
        writeStringToFile(string, File(filePath), isAppending)
    }

    @Throws(IOException::class)
    fun getBitmapFromAssets(filename: String?): Bitmap {
        val manager = King.applicationContext.assets
        val inputStream = manager.open((filename)!!)
        return BitmapFactory.decodeStream(inputStream)
    }

    fun copyAssetsToFile(assetFilename: String?, dstName: String?) {
        Executors.newCachedThreadPool().execute {
            var fos: FileOutputStream? = null
            try {
                val dstFile = File(Environment.getExternalStorageDirectory().absolutePath + File.separator + "ktools", dstName)
                fos = FileOutputStream(dstFile)
                val fileInputStream = getInputStreamFromAssets(assetFilename)
                val buffer = ByteArray(1024 * 2)
                var byteCount: Int
                while ((fileInputStream.read(buffer).also { byteCount = it }) != -1) {
                    fos.write(buffer, 0, byteCount)
                }
                fos.flush()
            } catch (e: IOException) {
            } finally {
                if (fos != null) {
                    try {
                        fos.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    /**
     * @param filename  filename you will create
     * @param directory directory where the file exists
     * @return true if the file created successfully, or return false
     */
    fun createFile(filename: String?, directory: String?): Boolean {
        var isSuccess = false
        val file = File(directory, filename)
        if (!file.exists()) {
            try {
                isSuccess = file.createNewFile()
            } catch (e: IOException) {
            }
        } else {
            file.delete()
            try {
                isSuccess = file.createNewFile()
            } catch (e: IOException) {
            }
        }
        return isSuccess
    }

    fun hideFile(directory: String?, filename: String): Boolean {
        val isSuccess: Boolean
        val file = File(directory, filename)
        isSuccess = file.renameTo(File(directory, ".$filename"))
        if (isSuccess) {
            file.delete()
        }
        return isSuccess
    }

    fun getFolderSize(folderPath: String?): Long {
        var size: Long = 0
        val directory = File(folderPath)
        if (directory.exists() && directory.isDirectory) {
            for (file: File in directory.listFiles()) {
                if (file.isDirectory) {
                    size += getFolderSize(file.absolutePath)
                } else {
                    size += file.length()
                }
            }
        }
        return size
    }

    fun readFromFile(filename: String?): String? {
        try {
            val bufferedReader = BufferedReader(FileReader(getAssetFileDescription(filename).fileDescriptor))
            val stringBuilder = StringBuilder()
            var content: String?
            while ((bufferedReader.readLine().also { content = it }) != null) {
                stringBuilder.append(content)
            }
            return stringBuilder.toString()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            return null
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
    }

    @Throws(IOException::class)
    fun getJsonStringFromAssets(filename: String?): String {
        val inputStreamReader = InputStreamReader(getInputStreamFromAssets(filename))
        val reader = BufferedReader(inputStreamReader)
        val builder = StringBuilder()
        var line: String?
        while ((reader.readLine().also { line = it }) != null) {
            builder.append(line)
        }
        return builder.toString()
    }

    /**
     * 使用FileChannel完成文件复制
     */
    fun copyFile(source:File,dest:File){
        thread { val fisChannel = FileInputStream(source).channel
            val fosChannel = FileOutputStream(dest).channel
            fosChannel.transferFrom(fisChannel,0,fisChannel.size())
            fosChannel.closeQuietly()
            fisChannel.closeQuietly()
        }
    }
}