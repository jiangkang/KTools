package com.jiangkang.ktools

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.EditText
import android.widget.VideoView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.jiangkang.hybrid.Khybrid
import com.jiangkang.ktools.databinding.ActivityImageBinding
import com.jiangkang.tools.utils.FileUtils
import com.jiangkang.tools.utils.ImageUtils
import com.jiangkang.tools.utils.LogUtils
import com.jiangkang.tools.utils.ToastUtils
import com.jiangkang.tools.widget.KDialog
import java.io.File
import java.io.IOException

class ImageActivity : AppCompatActivity() {

    private lateinit var binding:ActivityImageBinding

    private val etMaxWidth: EditText by lazy { findViewById<EditText>(R.id.et_max_width) }
    private val etMaxHeight: EditText by lazy { findViewById<EditText>(R.id.et_max_height) }

    private var outputImageFile: File? = null
    private var outputVideoFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "Image"

        binding.btnChoosePictureFromAlbum.setOnClickListener {
            openAlbum()
        }

        binding.btnTakePicture.setOnClickListener {
            onBtnTakePictureClicked()
        }

        binding.btnTakeVideo.setOnClickListener {
            onBtnTakeVideoClicked()
        }


        binding.btnScreenCapture.setOnClickListener {
            onBtnScreenCaptureClicked()
        }

        binding.btnTakePictureWithoutCompress.setOnClickListener {
            onBtnTakePictureWithoutCompressClicked()
        }

        binding.btnShowBase64ImgInWeb.setOnClickListener {
            onBtnShowBase64ImgInWebClicked()
        }

        binding.btnScaleImageByMaxWidthAndHeight.setOnClickListener {
            onBtnScaleImageByMaxWidthAndHeightClicked()
        }

        binding.btnPrintBitmap.setOnClickListener {
            onBtnPrintBitmapClicked()
        }
    }

    private fun openAlbum() {
        val albumIntent = Intent(Intent.ACTION_PICK)
        albumIntent.type = "image/*"
        startActivityForResult(albumIntent, REQUEST_OPEN_ALBUM)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_OPEN_ALBUM -> if (resultCode == Activity.RESULT_OK) {
                handleAlbumData(data)
            }
            REQUEST_IMAGE_CAPTURE -> if (resultCode == Activity.RESULT_OK) {
                handleImageCaptureData(data)
            }
            REQUEST_CODE_CAPTURE_IMAGE_WITHOUT_COMPRESS -> if (resultCode == Activity.RESULT_OK) {
                handleImageCaptureWithoutCompress(data)
            }
            REQUEST_CODE_TAKE_VIDEO -> if (resultCode == Activity.RESULT_OK) {
                handleVideoData(data)
            }
            else -> {
            }
        }
    }

    private fun handleVideoData(data: Intent?) {
        showVideoInDialog(outputVideoFile)
    }

    private fun showVideoInDialog(file: File?) {
        val videoView = VideoView(this)
        videoView.setVideoPath(file!!.absolutePath)
        val dialog = AlertDialog.Builder(this)
                .setView(videoView)
                .setPositiveButton("播放", null)
                .setNeutralButton("暂停", null)
                .setNegativeButton("关闭") { dialog, which -> dialog.dismiss() }
                .setCancelable(false)
                .show()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setOnClickListener { videoView.start() }
        dialog.getButton(AlertDialog.BUTTON_NEUTRAL)
                .setOnClickListener { videoView.pause() }
    }

    private fun handleImageCaptureWithoutCompress(data: Intent?) {
        val bitmap = BitmapFactory.decodeFile(outputImageFile!!.absolutePath)

        //让媒体扫描器扫描
        galleryAddPic(outputImageFile)
        KDialog.showImgInDialog(this, bitmap)
    }

    private fun galleryAddPic(file: File?) {
        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val imgUri = Uri.fromFile(file)
        mediaScanIntent.data = imgUri
        this.sendBroadcast(mediaScanIntent)
    }

    private fun handleImageCaptureData(data: Intent?) {
        //这张图是thumbnail,清晰度低，当做Icon还可以，但是作为图片显示并不合适
        val bitmap = data!!.extras!!["data"] as Bitmap?
        KDialog.showImgInDialog(this, bitmap)
    }

    private fun handleAlbumData(data: Intent?) {
        val uri = data!!.data
        val projection = arrayOf(
                MediaStore.Images.Media.DATA
        )
        val cursor = contentResolver.query(
                uri!!,
                projection,
                null,
                null,
                null
        )
        if (cursor != null && cursor.moveToFirst()) {
            val dataIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
            val imagePath = cursor.getString(dataIndex)
            cursor.close()
            showBitmap(imagePath)
        }
    }

    private fun showBitmap(imagePath: String) {
        ToastUtils.showShortToast(imagePath)
        val bitmap = BitmapFactory.decodeFile(imagePath)
        KDialog.showImgInDialog(this, bitmap)
    }

    private fun onBtnTakePictureClicked() {
        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            openCamera()
        } else {
            requestPermissions(arrayOf(Manifest.permission.CAMERA),1111)
        }
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        //返回第一个可以处理该Intent的组件
        if (cameraIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    private fun onBtnTakeVideoClicked() {
        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
           takeVideo()
        } else {
            requestPermissions(arrayOf(Manifest.permission.CAMERA),1111)
        }
    }

    private fun takeVideo() {
        val dirVideo = File(Environment.getExternalStorageDirectory(), "ktools/videos/")
        if (!dirVideo.exists()) {
            dirVideo.mkdirs()
        }
        outputVideoFile = File(dirVideo.absolutePath + System.currentTimeMillis() + ".mp4")
        if (!outputVideoFile!!.exists()) {
            try {
                outputVideoFile!!.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        //兼容7.0
        val videoUri = FileProvider.getUriForFile(
                this,
                BuildConfig.APPLICATION_ID,
                outputVideoFile!!
        )
        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        //指定输出
        intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri)
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1)
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 3000)
        startActivityForResult(intent, REQUEST_CODE_TAKE_VIDEO)
    }

    private fun onBtnScreenCaptureClicked() {
        val decorView = window.decorView
        decorView.isDrawingCacheEnabled = true
        decorView.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
        decorView.buildDrawingCache()
        val screen = Bitmap.createBitmap(decorView.drawingCache)
        KDialog.showImgInDialog(this, screen)
    }

    private fun onBtnTakePictureWithoutCompressClicked() {
        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            openCameraWithOutput()
        } else {
            requestPermissions(arrayOf(Manifest.permission.CAMERA),1111)
        }
    }

    private fun openCameraWithOutput() {
        val path = File(Environment.getExternalStorageDirectory(), "ktools").absolutePath
        if (!File(path).exists()) {
            File(path).mkdirs()
        }
        outputImageFile = File(path, System.currentTimeMillis().toString() + ".png")
        if (!outputImageFile!!.exists()) {
            try {
                outputImageFile!!.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        //兼容7.0
        val contentUri = FileProvider.getUriForFile(
                this,
                BuildConfig.APPLICATION_ID,
                outputImageFile!!
        )
        LogUtils.d(TAG, "openCameraWithOutput: uri = $contentUri")
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(Intent.EXTRA_MIME_TYPES, MimeTypeMap.getSingleton().getMimeTypeFromExtension("png"))
        //指定输出路径
        intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri)
        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, REQUEST_CODE_CAPTURE_IMAGE_WITHOUT_COMPRESS)
        }
    }

    private fun onBtnShowBase64ImgInWebClicked() {
        Khybrid().loadUrl(this, FileUtils.getAssetsPath("web/demo_img.html"))
    }

    private fun onBtnScaleImageByMaxWidthAndHeightClicked() {
        val srcBitmap = BitmapFactory.decodeResource(this.resources, R.drawable.demo)
        var maxWidth = srcBitmap.width
        var maxHeight = srcBitmap.height
        if (!TextUtils.isEmpty(etMaxWidth!!.text.toString()) && TextUtils.isDigitsOnly(etMaxWidth!!.text.toString())) {
            maxWidth = etMaxWidth!!.text.toString().toInt()
        }
        if (!TextUtils.isEmpty(etMaxHeight!!.text.toString()) && TextUtils.isDigitsOnly(etMaxHeight!!.text.toString())) {
            maxHeight = etMaxHeight!!.text.toString().toInt()
        }
        val scaledBitmap = ImageUtils.scaleBitmap(srcBitmap, maxWidth, maxHeight)
        if (scaledBitmap != null) {
            KDialog.showImgInDialog(this, scaledBitmap)
        }
    }

    private fun onBtnPrintBitmapClicked() {
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.demo)
        ImageUtils.printBitmap(this, bitmap)
    }

    companion object {
        private val TAG = ImageActivity::class.java.simpleName
        private const val REQUEST_OPEN_ALBUM = 1000
        private const val REQUEST_IMAGE_CAPTURE = 1001
        private const val REQUEST_CODE_CAPTURE_IMAGE_WITHOUT_COMPRESS = 1002
        private const val REQUEST_CODE_TAKE_VIDEO = 1003
    }
}