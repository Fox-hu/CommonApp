package com.silver.fox.media

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.jaygoo.widget.RangeSeekBar
import com.silver.fox.ext.copyAssets
import com.silver.fox.ext.dp
import com.silver.fox.media.mix.MixProcess
import com.silver.fox.media.mix.MixProcess1
import com.silver.fox.media.rtmp.encoder.PermissionUtils
import java.io.File
import java.io.IOException

class AudioMixActivity : AppCompatActivity() {
    var videoView: VideoView? = null
    var rangeSeekBar: RangeSeekBar? = null
    var musicSeekBar: SeekBar? = null
    var voiceSeekBar: SeekBar? = null
    var musicVolume = 0
    var voiceVolume = 0
    var runnable: Runnable? = null
    var mDuration = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PermissionUtils().verifyStoragePermissions(this, null)
        setContentView(R.layout.media_activity_audio_mix)
        videoView = findViewById(R.id.videoView)
        rangeSeekBar = findViewById(R.id.rangeSeekBar)
        musicSeekBar = findViewById(R.id.musicSeekBar)
        voiceSeekBar = findViewById(R.id.voiceSeekBar)
        musicSeekBar?.max = 100
        voiceSeekBar?.max = 100
        musicSeekBar?.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                musicVolume = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
        voiceSeekBar?.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                voiceVolume = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
        val videoPath = File("${filesDir?.parentFile?.path}", "input.mp4").absolutePath
        try {
            copyAssets(
                assets, "music.mp3",
                File("${filesDir?.parentFile?.path}").absolutePath, false
            )
            copyAssets(
                assets, "input.mp4",
                File("${filesDir?.parentFile?.path}").absolutePath, false
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }
        startPlay(videoPath)
    }

    private fun startPlay(path: String) {
        videoView?.apply {
            val layoutParams = layoutParams
            layoutParams.height = 360.dp.toInt()
            layoutParams.width = 640.dp.toInt()
            this.layoutParams = layoutParams
            setVideoPath(path)
            start()
            setOnPreparedListener { mp ->
                mDuration = mp.duration / 1000
                mp.isLooping = true
                rangeSeekBar?.apply {
                    setRange(0f, mDuration.toFloat())
                    setValue(0f, mDuration.toFloat())
                    isEnabled = true
                    requestLayout()
                    setOnRangeChangedListener { _, min, _, _ ->
                        videoView?.seekTo(
                            min.toInt() * 1000
                        )
                    }
                    val handler = Handler()
                    runnable = Runnable {
                        if (videoView!!.currentPosition >= currentRange[1] * 1000) {
                            videoView!!.seekTo(currentRange[0].toInt() * 1000)
                        }
                        handler.postDelayed(runnable, 1000)
                    }
                    handler.postDelayed(runnable, 1000)
                }
            }
        }
    }

    fun clip(view: View) {
        //
        val videoFile = File("${filesDir?.parentFile?.path}", "input.mp4")
        val audioFile = File("${filesDir?.parentFile?.path}", "music.mp3")
        val outputFile = File("${filesDir?.parentFile?.path}", "output.mp4")
        Thread {
            MixProcess.mixAudioTrack(
                videoFile.absolutePath,
                audioFile.absolutePath,
                outputFile.absolutePath,
                (rangeSeekBar!!.currentRange[0] * 1000 * 1000).toInt(),
                (rangeSeekBar!!.currentRange[1] * 1000 * 1000).toInt(),
                voiceVolume,
                musicVolume
            )
// 用MixProcess1巨慢无比 原因待查
//            MixProcess1(videoFile.absolutePath,
//                audioFile.absolutePath,
//                outputFile.absolutePath).mixAudioTrack(
//                (rangeSeekBar!!.currentRange[0] * 1000 * 1000).toInt(),
//                (rangeSeekBar!!.currentRange[1] * 1000 * 1000).toInt(),
//                voiceVolume,
//                musicVolume
//            )
            runOnUiThread {
                startPlay(
                    outputFile.absolutePath
                )
                Toast.makeText(this, "剪辑完毕", Toast.LENGTH_SHORT).show()
            }
        }.start()
    }
}