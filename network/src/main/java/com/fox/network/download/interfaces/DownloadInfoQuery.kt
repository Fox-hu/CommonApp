package com.job.network.interfaces

import com.fox.network.download.entity.DownloadInfo


/**

 * @Author fox.hu

 * @Date 2019/11/26 14:51

 */

interface DownloadInfoQuery {

    /**
     * 根据url找到目标的downloadinfo 如果没有则返回null
     **/
    fun getDownloadInfoByUrl(url: String): DownloadInfo?

    /**
     * 返回所有正在下载的任务
     **/
    fun getAllDownLoading(): List<DownloadInfo>

    /**
     * 返回所有已下载完成的任务
     **/
    fun getAllDownLoaded(): List<DownloadInfo>


    /**
     * @param orlUrl 原链接的url
     * @param downloadInfo 更新的item
     * 更新downloadinof的信息 为了防止盗链，url会定期失效
     **/
    fun updateDownloadInfo(orlUrl: String, downloadInfo: DownloadInfo)

}