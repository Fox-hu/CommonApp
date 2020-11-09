package com.fox.network.download.interfaces


import android.content.Context
import android.os.Environment
import com.fox.network.download.entity.DownloadInfo
import com.fox.network.download.entity.DownloadState

/**
 * @Author fox.hu
 * @Date 2019/11/26 14:51
 */

interface DownloadEvent {

    var listenerList: MutableList<Listener>

    fun registerListener(listener: Listener) {
        if (!listenerList.contains(listener)) {
            listenerList.add(listener)
        }
    }

    fun unRegisterListener(listener: Listener) {
        listenerList.remove(listener)
    }

    /**
     * 初始化
     *
     **/
    fun init(context: Context)

    /**
     * 开始下载
     **/

    fun download(downloadAble: DownloadAble): Boolean

    /**
     * 暂停下载
     **/
    fun stop(downloadAble: DownloadAble)

    /**
     * 继续下载
     **/
    fun resume(downloadAble: DownloadAble)

    /**
     * 取消任务 并删除记录
     **/
    fun cancel(downloadAble: DownloadAble)

    fun resumeAll()

    fun pauseAll()

    fun deleteAll()

    interface Listener {

        fun onStateChangeListener(downloadInfo: DownloadInfo, downloadState: DownloadState)

    }
}

interface DownloadAble {
    fun getName(): String
    fun getUrl(): String
    fun getPath(): String {
        val urlFileName = getUrl().split("/").last()
        val fileName = System.currentTimeMillis().toString() + urlFileName
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path + "/" + fileName
    }

    fun getBelong(): String? {
        return ""
    }

    fun getTag(): String? {
        return ""
    }
}