package com.fox.network.download.downloader

import android.content.Context
import com.arialyy.annotations.Download
import com.arialyy.aria.core.Aria
import com.arialyy.aria.core.download.DownloadEntity
import com.arialyy.aria.core.task.DownloadTask
import com.fox.network.download.Downloader
import com.fox.network.download.entity.DownloadInfo
import com.fox.network.download.interfaces.DownloadAble
import com.fox.network.download.interfaces.DownloadEvent
import com.j256.ormlite.dao.Dao
import com.fox.network.download.entity.DownloadState
import com.silver.fox.ext.isNotNullAndEmpty
import com.silver.fox.ext.logi
import java.util.concurrent.CopyOnWriteArrayList

/**

 * @Author fox.hu

 * @Date 2019/11/26 17:01

 */

class AriaDownloader : Downloader {

    private val TAG = AriaDownloader::class.java.simpleName

    override var listenerList: MutableList<DownloadEvent.Listener> = CopyOnWriteArrayList()

    private lateinit var downloadInfoDao: Dao<DownloadInfo, String>

    override fun init(context: Context) {
        Aria.init(context)
        onAttach()
        downloadInfoDao = DownloadInfoDBHelper(context).getDao(DownloadInfo::class.java)
    }

    override fun download(downloadAble: DownloadAble): Boolean {
        val downloadEntity = Aria.download(this).getFirstDownloadEntity(downloadAble.getUrl())
        return if (downloadEntity != null) {
            val items = downloadInfoDao.queryBuilder().where().eq("url", downloadEntity.url).query()
            if (items.isNotNullAndEmpty()) {
                val downloadInfo = items[0]
                ("has Downloaded $downloadInfo").logi(TAG)
                Aria.download(this).load(downloadInfo.getUrl()).setFilePath(downloadInfo.getPath()).create()
            }
            true
        } else {
            val path = downloadAble.getPath()
            val taskId = Aria.download(this).load(downloadAble.getUrl()).setFilePath(path).create()
            val downloadInfo = downloadAble.run {
                DownloadInfo.Builder().url(getUrl()).path(path).name(getName()).belong(getBelong()).tag(getTag()).build()
            }
            ("has not Download $downloadInfo").logi(TAG)
            if (taskId != -1L) {
                downloadInfo.id = taskId
                downloadInfoDao.create(downloadInfo)
                ("taskId error").logi(TAG)
                return false
            }
            true
        }
    }

    override fun stop(downloadAble: DownloadAble) {
        val id = getIdByUrl(downloadAble)
        Aria.download(this).load(id).stop()
    }

    private fun getIdByUrl(downloadAble: DownloadAble): Long {
        val entity = Aria.download(this).getFirstDownloadEntity(downloadAble.getUrl())
        return entity?.id ?: 0
    }

    override fun resume(downloadAble: DownloadAble) {
        val id = getIdByUrl(downloadAble)
        Aria.download(this).load(id).resume()
    }

    override fun cancel(downloadAble: DownloadAble) {
        val id = getIdByUrl(downloadAble)
        Aria.download(this).load(id).cancel(true)
    }

    override fun resumeAll() {
        Aria.download(this).resumeAllTask()
    }

    override fun pauseAll() {
        Aria.download(this).stopAllTask()
    }

    override fun deleteAll() {
        Aria.download(this).removeAllTask(true)
    }

    override fun getDownloadInfoByUrl(url: String): DownloadInfo? {
        val downloadEntity = Aria.download(this).getFirstDownloadEntity(url)
        return downloadEntity?.let {
            convert2DownloadInfo(it)
        }
    }

    override fun getAllDownLoading(): List<DownloadInfo> {
        return convert2DownloadInfoList(Aria.download(this).allNotCompleteTask)
    }

    override fun getAllDownLoaded(): List<DownloadInfo> {
        return convert2DownloadInfoList(Aria.download(this).allCompleteTask)
    }


    private fun convert2DownloadInfoList(ariaList: MutableList<DownloadEntity>?): List<DownloadInfo> {
        return if (ariaList.isNullOrEmpty()) {
            emptyList()
        } else {
            ariaList!!.map {
                convert2DownloadInfo(it) ?: return emptyList()
            }.toList()
        }
    }

    private fun convert2DownloadInfo(downloadEntity: DownloadEntity): DownloadInfo? {
        val query = downloadInfoDao.queryBuilder().where().eq("url", downloadEntity.url).query()
        return if (query.isNotNullAndEmpty()) {
            query[0]
        } else {
            null
        }
    }

    override fun updateDownloadInfo(orlUrl: String, downloadInfo: DownloadInfo) {
        val downloadEntity = Aria.download(this).getFirstDownloadEntity(orlUrl)
        downloadEntity?.apply {
            url = downloadInfo.getUrl()
            filePath = downloadInfo.getPath()
            update()
            downloadInfoDao.update(downloadInfo)
        }
    }

    override fun onAttach() {
        Aria.download(this).register()
    }

    override fun onDetach() {
        Aria.download(this).unRegister()
    }

    private fun findDownLoadInfoThenCallBack(task: DownloadTask?, downloadState: DownloadState, callback: (DownloadInfo) -> Unit) {
        task?.let { t ->
            val retList = downloadInfoDao.queryBuilder().where().eq("url", t.key).query()
            retList?.let {
                if (it.isNotEmpty()) {
                    val downloadInfo = it[0]
                    (downloadInfo.getName() + " " + downloadState.name).logi(TAG)
                    convertTask2Info(t, downloadInfo, downloadState)
                    downloadInfoDao.update(downloadInfo)
                    callback(downloadInfo)
                }
            }
        }
    }

    private fun convertTask2Info(task: DownloadTask, downloadInfo: DownloadInfo, downloadState: DownloadState) {
        downloadInfo.apply {
            convertFileSize = task.convertFileSize
            convertSpeed = task.convertSpeed
            percent = task.percent
            fileSize = task.fileSize
            this.downloadState = downloadState.STATE
        }
    }

    @Download.onTaskStart
    fun onDownloadStart(task: DownloadTask?) {
        findDownLoadInfoThenCallBack(task, DownloadState.STATE_START) {
            listenerList.forEach { listener -> listener.onStateChangeListener(it, DownloadState.STATE_START) }
        }
    }

    @Download.onWait
    fun onDownloadWait(task: DownloadTask?) {
        findDownLoadInfoThenCallBack(task, DownloadState.STATE_WAIT) {
            listenerList.forEach { listener -> listener.onStateChangeListener(it, DownloadState.STATE_WAIT) }
        }
    }

    @Download.onTaskStop
    fun onDownloadStop(task: DownloadTask?) {
        findDownLoadInfoThenCallBack(task, DownloadState.STATE_STOP) {
            listenerList.forEach { listener -> listener.onStateChangeListener(it, DownloadState.STATE_STOP) }
        }
    }

    @Download.onTaskResume
    fun onDownloadResume(task: DownloadTask?) {
        findDownLoadInfoThenCallBack(task, DownloadState.STATE_RESUME) {
            listenerList.forEach { listener -> listener.onStateChangeListener(it, DownloadState.STATE_RESUME) }
        }
    }

    @Download.onTaskComplete
    fun onDownloadComplete(task: DownloadTask?) {
        findDownLoadInfoThenCallBack(task, DownloadState.STATE_COMPLETE) {
            listenerList.forEach { listener -> listener.onStateChangeListener(it, DownloadState.STATE_COMPLETE) }
        }
    }

    @Download.onTaskFail
    fun onDownloadFailed(task: DownloadTask?) {
        findDownLoadInfoThenCallBack(task, DownloadState.STATE_FAILED) {
            listenerList.forEach { listener -> listener.onStateChangeListener(it, DownloadState.STATE_FAILED) }
        }
    }

    @Download.onTaskRunning
    fun onDownloadRunning(task: DownloadTask?) {
        findDownLoadInfoThenCallBack(task, DownloadState.STATE_RUNNING) {
            listenerList.forEach { listener -> listener.onStateChangeListener(it, DownloadState.STATE_RUNNING) }
        }
    }

    @Download.onTaskCancel
    fun onDownloadCancel(task: DownloadTask?) {
        findDownLoadInfoThenCallBack(task, DownloadState.STATE_CANCEL) {
            downloadInfoDao.delete(it)
            listenerList.forEach { listener -> listener.onStateChangeListener(it, DownloadState.STATE_CANCEL) }
        }
    }
}