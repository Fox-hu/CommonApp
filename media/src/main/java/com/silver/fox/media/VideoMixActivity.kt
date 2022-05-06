package com.silver.fox.media

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.silver.fox.ext.copyAssets
import com.silver.fox.ext.showShortToast
import com.silver.fox.media.video.VideoMixProcess
import java.io.File


/**
 *
 */
class VideoMixActivity : AppCompatActivity() {
    @Volatile
    private var isClip = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.media_activity_video_mix)
    }

    fun mix(view: View?) {
        if (!isClip) {
            isClip = true
            Thread {
                copyAssets(
                    assets, "input.mp4",
                    File("${filesDir?.parentFile?.path}").absolutePath, true
                )
                copyAssets(
                    assets, "input1.mp4",
                    File("${filesDir?.parentFile?.path}").absolutePath, true
                )
                val inputPath1 = File("${filesDir?.parentFile?.path}", "input.mp4").absolutePath
                val inputPath2 = File("${filesDir?.parentFile?.path}", "input1.mp4").absolutePath
                val outPutPath = File("${filesDir?.parentFile?.path}", "mix_video.mp4").absolutePath
                VideoMixProcess.appendVideo(inputPath1, inputPath2, outPutPath)
                runOnUiThread {
                    "视频合并完成，path = $outPutPath".showShortToast()
                    isClip = false
                }
            }.start()
        } else {
            "正在裁剪中，请稍后".showShortToast()
        }
    }
}