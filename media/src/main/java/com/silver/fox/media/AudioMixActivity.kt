package com.silver.fox.media

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.jaygoo.widget.RangeSeekBar
import com.silver.fox.ext.copyAssets
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
    }

    override fun onResume() {
        super.onResume()
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
            layoutParams.height = 480
            layoutParams.width = 640
            this.layoutParams = layoutParams
            setVideoPath(path)
            start()
            setOnPreparedListener { mp ->
                mDuration = mp.duration / 1000
                mp.isLooping = true
                rangeSeekBar?.apply {
                    setRange(0f, duration.toFloat())
                    setValue(0f, duration.toFloat())
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

    fun clip(view: View){

    }
}