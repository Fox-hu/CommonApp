package com.fox.toutiao.ui

import android.os.Bundle
import android.os.Environment
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.arialyy.annotations.Download
import com.arialyy.aria.core.Aria
import com.arialyy.aria.core.task.DownloadTask
import com.fox.toutiao.R
import com.silver.fox.ext.toast
import kotlinx.android.synthetic.main.activity_download.*


class DownloadActivity : AppCompatActivity() {

    private var mTaskId = -1L

    companion object {
        const val DOWNLOAD_URL =
            "http://wdj-qn-apk.wdjcdn.com/e/b8/520c1a2208bf7724b96f538247233b8e.apk"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download)
        Aria.download(this).register()
    }


    fun onClick(view: View) {
        when (view.getId()) {
            R.id.start ->
                if (mTaskId == -1L) {
                    mTaskId = Aria.download(this)
                        .load(DOWNLOAD_URL)
                        .setFilePath(
                            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path + "/tttt.apk",
                            true
                        )
                        .create()
                } else {
                    Aria.download(this).load(mTaskId)
                        .resume()
                }


            R.id.stop -> if (Aria.download(this).load(mTaskId).isRunning) run {
                Aria.download(this).load(mTaskId).stop()
            }

            R.id.cancel -> Aria.download(this).load(mTaskId).cancel(true)
        }
    }

    @Download.onTaskStart
    fun taskStart(task: DownloadTask) {
        size.text = task.convertFileSize
    }

    @Download.onTaskRunning
    fun running(task: DownloadTask) {
        if (task.key == DOWNLOAD_URL) {
            var fileSize = task.fileSize
            if (fileSize == 0L) {
                progressBar.progress = 0
            } else {
                progressBar.progress = task.percent
            }
            speed.text = task.convertSpeed
        }
    }

    @Download.onTaskStop
    fun taskStop(task: DownloadTask) {
        if (task.key == DOWNLOAD_URL) {
            start.text = "resume"
            speed.text = ""
        }
    }

    @Download.onTaskCancel
    fun taskCancel(task: DownloadTask) {
        if (task.key == DOWNLOAD_URL) {
            progressBar.progress = 0
            start.text = "start"
            speed.text = ""
            mTaskId = -1
        }
    }

    @Download.onTaskComplete
    fun taskComplete(task: DownloadTask) {
        if (task.key == DOWNLOAD_URL) {
            progressBar.progress = 100
            toast("下载完成")
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        Aria.download(this).unRegister()
    }
}
