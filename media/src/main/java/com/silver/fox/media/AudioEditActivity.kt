package com.silver.fox.media

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.silver.fox.ext.copyAssets
import com.silver.fox.ext.showShortToast
import com.silver.fox.media.audio.PcmToWav
import com.silver.fox.media.audio.pcm.PcmEditor
import java.io.File

/**
 * 将一个音频裁减成一个新音频
 */
class AudioEditActivity : AppCompatActivity() {

    @Volatile
    private var isClip = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.media_activity_audio_edit)
    }

    fun clip(view: View?) {
        if (!isClip) {
            isClip = true
            Thread {
                copyAssets(
                    assets, "music.mp3",
                    File("${filesDir?.parentFile?.path}").absolutePath, true
                )
                val inputPath = File("${filesDir?.parentFile?.path}", "music.mp3").absolutePath
                val outPutPath =
                    File("${filesDir?.parentFile?.path}", "music_edit.mp3").absolutePath

                PcmEditor(inputPath).clip(10 * 1000 * 1000, 15 * 1000 * 1000) {
                    if (File(outPutPath).exists()) File(outPutPath).delete()
                    PcmToWav(44100).pcmToWav(it, outPutPath)
                    runOnUiThread {
                        "裁剪音频完成，path = $outPutPath".showShortToast()
                        isClip = false
                    }
                }
            }.start()
        } else {
            "正在裁剪中，请稍后".showShortToast()
        }
    }
}