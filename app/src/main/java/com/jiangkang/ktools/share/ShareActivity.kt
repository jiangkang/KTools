package com.jiangkang.ktools.share

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.core.content.FileProvider
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import com.jiangkang.ktools.BuildConfig
import com.jiangkang.ktools.R
import com.jiangkang.tools.utils.FileUtils
import com.jiangkang.tools.utils.LogUtils
import com.jiangkang.tools.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_share.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.io.File


class ShareActivity : AppCompatActivity() {

    val TAG: String = "Share"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)

        receiveIntent()

        FileUtils.copyAssetsToFile("img/dog.jpg", "share0.jpg")
        FileUtils.copyAssetsToFile("img/demo.jpeg", "share1.jpeg")

        handleClick()
    }

    private fun receiveIntent() {

        val action = intent?.action
        val type = intent?.type


        type?.let {
            when (action) {
                Intent.ACTION_SEND -> {
                    if ("text/plain" == type) {
                        handleSendText(intent)
                    } else if (type.startsWith("image/")) {
                        handleSendImage(intent)
                        setResult(Activity.RESULT_OK)
                    }
                }

                Intent.ACTION_SEND_MULTIPLE -> {
                    if (type.startsWith("image/")) {
                        handleMultiImage(intent)
                        setResult(Activity.RESULT_OK)
                    }
                }

            }
        }


    }

    private fun handleSendText(intent: Intent) {
        val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)
        sharedText?.let {
            ToastUtils.showShortToast(it)
        }
    }

    private fun handleMultiImage(intent: Intent) {
        val imageUris: ArrayList<Uri> = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM)
        imageUris.let {
            imageUris.map {
                LogUtils.d(TAG, it.toString())
            }
        }
    }

    private fun handleSendImage(intent: Intent) {
        val imageUri: Uri = intent.getParcelableExtra(Intent.EXTRA_STREAM)
        imageUri.let {
            Log.d(TAG, it.toString())
            ToastUtils.showShortToast(it.toString())
        }
    }


    private fun handleClick() {
        btnShareText.onClick {
            shareText()
        }


        btnShareSingleImage.onClick {
            shareSingleImage()
        }

        btnShareMultiImages.onClick {
            shareMultiImages()
        }

        btnShareToQQ.onClick {
            val packageName = "com.tencent.mobileqq"
            shareToTarget(packageName)
        }

        btnShareToWeChat.onClick {
            val packageName = "com.tencent.mm"
            shareToTarget(packageName)
        }


        btnRouter.onClick {
            val intentString = et_intent.text.toString()
            intentString.let {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.addCategory(Intent.CATEGORY_DEFAULT)
                intent.data = Uri.parse(intentString)
                startActivity(intent)
            }
        }
    }


    private fun shareToTarget(packageName:String){
        val intent = Intent(Intent.ACTION_SEND)
        intent.apply {
            `package` = packageName
            intent.type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "来自于KTools")
        }
        if (intent.resolveActivity(packageManager) != null){
            startActivity(intent)
        }
    }

    private fun shareMultiImages() {
        doAsync {
            val file1 = File(Environment.getExternalStorageDirectory().absolutePath + File.separator + "ktools", "share0.jpg")
            val imageUri1 = FileProvider.getUriForFile(
                    this@ShareActivity,
                    BuildConfig.APPLICATION_ID,
                    file1
            )

            val file2 = File(Environment.getExternalStorageDirectory().absolutePath + File.separator + "ktools", "share1.jpeg")
            val imageUri2 = FileProvider.getUriForFile(
                    this@ShareActivity,
                    BuildConfig.APPLICATION_ID,
                    file2
            )
            var imageUris = ArrayList<Uri>()
            imageUris.add(imageUri1)
            imageUris.add(imageUri2)
            runOnUiThread {
                val shareIntent = Intent()
                shareIntent.action = Intent.ACTION_SEND_MULTIPLE
                shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris)
                shareIntent.type = "image/*"
                startActivity(Intent.createChooser(shareIntent, "分享多张图片到指定App"))
            }
        }
    }

    private fun shareSingleImage() {
        doAsync {
            val file = File(Environment.getExternalStorageDirectory().absolutePath + File.separator + "ktools", "share0.jpg")
            val uriToImage = FileProvider.getUriForFile(
                    this@ShareActivity,
                    BuildConfig.APPLICATION_ID,
                    file
            )
            runOnUiThread {
                val shareIntent = Intent()
                shareIntent.action = Intent.ACTION_SEND
                shareIntent.putExtra(Intent.EXTRA_STREAM, uriToImage)
                shareIntent.type = "image/jpeg"
                startActivity(Intent.createChooser(shareIntent, "分享单张图片到指定App"))
            }
        }

    }


    private fun shareText() {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.")
        sendIntent.type = "text/plain"
        startActivity(Intent.createChooser(sendIntent, "分享内容到指定App"))
    }




}
