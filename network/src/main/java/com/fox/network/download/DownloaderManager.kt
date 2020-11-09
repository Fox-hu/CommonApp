package com.fox.network.download

import com.fox.network.download.downloader.AriaDownloader
import com.fox.network.download.interfaces.DownloadEvent
import com.job.network.interfaces.DownloadInfoQuery
import com.fox.network.download.interfaces.DownloadLifeCycle

/**

 * @Author fox.hu

 * @Date 2019/11/26 14:51

 */
interface Downloader : DownloadEvent, DownloadInfoQuery, DownloadLifeCycle

object DownloadManager : Downloader by AriaDownloader()

