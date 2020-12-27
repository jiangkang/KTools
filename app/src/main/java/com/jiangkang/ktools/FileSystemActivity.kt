package com.jiangkang.ktools

import android.app.ProgressDialog
import android.content.res.AssetManager
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.jiangkang.ktools.databinding.ActivityFileSystemBinding
import com.jiangkang.tools.utils.FileUtils
import com.jiangkang.tools.utils.ToastUtils
import com.jiangkang.tools.widget.KDialog
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.util.*
import java.util.concurrent.Executors

/**
 * @author jiangkang
 */
class FileSystemActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFileSystemBinding

    var mProgressDialog: ProgressDialog? = null
    private var assetManager: AssetManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFileSystemBinding.inflate(layoutInflater)
        setContentView(binding.root)
        assetManager = assets
        mProgressDialog = ProgressDialog(this)

        handleClick()
    }

    private fun handleClick() {

        binding.btnGetGeneralFilePath.setOnClickListener {
            onBtnGetGeneralFilePathClicked()
        }


        binding.btnGetImgFromAssets.setOnClickListener {
            onBtnGetImgFromAssetsClicked()
        }

        binding.btnGetAudioFromAssets.setOnClickListener {
            onBtnGetAudioFromAssetsClicked()
        }

        binding.btnGetJsonFromAssets.setOnClickListener {
            onBtnGetJsonFromAssetsClicked()
        }

        binding.btnCreateFile.setOnClickListener {
            onBtnCreateFileClicked()
        }

        binding.btnGetFileSize.setOnClickListener {
            onBtnGetFileSizeClicked()
        }


        binding.btnGetDirSize.setOnClickListener {
            onBtnGetDirSizeClicked()
        }

        binding.btnDeleteFile.setOnClickListener {
            onBtnDeleteFileClicked()
        }

        binding.btnHideFile.setOnClickListener {
            onBtnHideFileClicked()
        }
    }

    private fun onBtnGetGeneralFilePathClicked() {
        val builder = StringBuilder()
        builder.append("""context.getExternalCacheDir():
  --${externalCacheDir!!.absolutePath}
""")
        builder.append("""context.getCacheDir():
  --${cacheDir.absolutePath}
""")
        builder.append("""context.getExternalFilesDir(Environment.DIRECTORY_PICTURES):
  --${getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!.absolutePath}
""")
        builder.append("""context.getFilesDir():
  --${filesDir.absolutePath}
""")
        KDialog.showMsgDialog(this, builder.toString())
    }

    private fun onBtnGetImgFromAssetsClicked() {
        try {
            val inputStream = assetManager!!.open("img/dog.jpg")
            val bitmap = BitmapFactory.decodeStream(inputStream)
            KDialog.showImgInDialog(this, bitmap)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun onBtnGetAudioFromAssetsClicked() {
        try {
            val descriptor = FileUtils.getAssetFileDescription("music/baiyemeng.mp3")
            val player = MediaPlayer()
            player.setDataSource(descriptor.fileDescriptor, descriptor.startOffset, descriptor.length)
            player.prepareAsync()
            player.setOnPreparedListener { mp -> showPlayerDialog(mp) }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun onBtnGetJsonFromAssetsClicked() {
        var inputStreamReader: InputStreamReader? = null
        try {
            inputStreamReader = InputStreamReader(assetManager!!.open("json/demo.json"))
            val reader = BufferedReader(inputStreamReader)
            val builder = StringBuilder()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                builder.append(line)
            }
            val jsonStr = builder.toString()
            val json = JSONObject(jsonStr)
            KDialog.showMsgDialog(this, json.toString(4))
        } catch (e: IOException) {
        } catch (e: JSONException) {
        } finally {
            if (inputStreamReader != null) {
                try {
                    inputStreamReader.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun onBtnCreateFileClicked() {
        val isSuccess = FileUtils.createFile("test.txt", Environment.getExternalStorageDirectory().absolutePath + File.separator + "ktools")
        ToastUtils.showShortToast(if (isSuccess) "test.txt在ktools文件夹下创建成功" else "创建失败")
    }

    private fun onBtnGetFileSizeClicked() {
        FileUtils.createFile("demo.txt", Environment.getExternalStorageDirectory().absolutePath + File.separator + "ktools")
        val file = File(Environment.getExternalStorageDirectory().absolutePath + File.separator + "ktools", "demo.txt")
        FileUtils.writeStringToFile("这只是一个测试文件，测试文件", file, false)
        if (file.exists() && file.isFile) {
            try {
                val fis = FileInputStream(file)
                val size = fis.available().toLong()
                ToastUtils.showShortToast("size = " + size + "B")
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else {
            ToastUtils.showShortToast("文件不存在")
        }
    }

    private fun onBtnGetDirSizeClicked() {
        Executors.newSingleThreadExecutor().execute {
            showProgressDialog()
            val size = FileUtils.getFolderSize(Environment.getExternalStorageDirectory().absolutePath)
            closeProgressDialog()
            KDialog.showMsgDialog(this@FileSystemActivity, "存储器根目录大小为" + (size / (1024 * 1024)).toString() + "M")
        }
    }

    private fun onBtnDeleteFileClicked() {
        val file = File(Environment.getExternalStorageDirectory().absolutePath + File.separator + "ktools",
                "test.txt")
        if (!file.exists()) {
            ToastUtils.showShortToast("要删除的文件不存在")
        } else {
            val isDone = file.delete()
            ToastUtils.showShortToast(if (isDone) "删除成功" else "删除失败")
        }
    }

    private fun showPlayerDialog(player: MediaPlayer) {
        val duration = player.duration
        val view = LayoutInflater.from(this).inflate(R.layout.layout_dialog_music_player, null)
        val seekBar = view.findViewById<SeekBar>(R.id.seek_bar_music)
        seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                //此处干扰正常播放，声音抖动
                val max = seekBar.max
                val destPosition = progress * duration / max
                player.seekTo(destPosition)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
        view.findViewById<View>(R.id.iv_play).setOnClickListener { player.start() }
        view.findViewById<View>(R.id.iv_pause).setOnClickListener { player.pause() }
        view.findViewById<View>(R.id.iv_stop).setOnClickListener { player.stop() }
        val timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    val currentPosition = player.currentPosition
                    val progress = currentPosition * 100 / duration
                    seekBar.progress = progress
                }
            }
        }, 0, 100)
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(false)
                .setTitle("播放器")
                .setView(view)
                .setNegativeButton("关闭") { dialog, which ->
                    if (player.isPlaying) {
                        player.stop()
                    }
                    dialog.dismiss()
                }.show()
    }

    private fun onBtnHideFileClicked() {
        //隐藏文件或者文件夹只需要将文件名前面加一个‘.’号
        val isSuccess = FileUtils.hideFile(Environment.getExternalStorageDirectory().absolutePath + File.separator + "ktools", "test.txt")
        ToastUtils.showShortToast(if (isSuccess) "隐藏成功" else "隐藏失败")
    }

    private fun showProgressDialog() {
        runOnUiThread {
            mProgressDialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
            mProgressDialog!!.setMessage("正在查询中，请稍等....")
            mProgressDialog!!.show()
        }
    }

    private fun closeProgressDialog() {
        runOnUiThread {
            if (mProgressDialog != null && mProgressDialog!!.isShowing) {
                mProgressDialog!!.hide()
            }
        }
    }

    companion object {
        private val TAG = FileSystemActivity::class.java.simpleName
    }
}