package com.jiangkang.ktools

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.OnInitListener
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.jiangkang.annotations.Safe
import com.jiangkang.ktools.audio.VoiceBroadcastReceiver
import com.jiangkang.tools.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_audio.*
import java.util.*

/**
 * @author jiangkang
 */
class AudioActivity : AppCompatActivity() {

    private val etTextContent: EditText by lazy { findViewById<EditText>(R.id.et_text_content) }

    private var onInitListener: OnInitListener? = null
    private var speech: TextToSpeech? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio)
        btn_text_to_speech.setOnClickListener {
            onBtnTextToSpeechClicked()
        }

        btn_play_single_sound.setOnClickListener {
            onBtnPlaySingleSoundClicked()
        }

        btn_play_multi_sounds.setOnClickListener {
            onBtnPlayMultiSoundsClicked()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        if (speech != null) {
            speech!!.shutdown()
        }
    }

    private fun onBtnTextToSpeechClicked() {
        onInitListener = OnInitListener { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = speech!!.setLanguage(Locale.ENGLISH)
                if (result == TextToSpeech.LANG_MISSING_DATA
                        || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    ToastUtils.showShortToast("语言不支持")
                } else {
                    var content = "This is a default voice"
                    if (!TextUtils.isEmpty(etTextContent.text.toString())) {
                        content = etTextContent.text.toString()
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        speech!!.speak(content, TextToSpeech.QUEUE_FLUSH, null, null)
                    } else {
                        speech!!.speak(content, TextToSpeech.QUEUE_FLUSH, null)
                    }
                }
            }
        }
        speech = TextToSpeech(this, onInitListener)
    }

    @Safe
    fun onBtnPlaySingleSoundClicked() {
        val player = MediaPlayer.create(this, R.raw.tts_success)
        player.start()
    }

    private fun onBtnPlayMultiSoundsClicked() {
        sendBroadcast(Intent(this, VoiceBroadcastReceiver::class.java))
    }

    companion object {
        private const val TAG = "AudioActivity"
        fun launch(context: Context, bundle: Bundle?) {
            val intent = Intent(context, AudioActivity::class.java)
            if (bundle != null) {
                intent.putExtras(bundle)
            }
            context.startActivity(intent)
        }
    }
}

