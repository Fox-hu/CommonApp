package com.fox.network.download.downloader

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.fox.network.download.entity.DownloadInfo
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils


/**

 * @Author fox.hu

 * @Date 2019/11/27 13:43

 */

class DownloadInfoDBHelper(context: Context) : OrmLiteSqliteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    companion object {
        const val DB_NAME = "download_data.db"
        const val DB_VERSION = 3
    }

    override fun onCreate(database: SQLiteDatabase?, connectionSource: ConnectionSource?) {
        TableUtils.createTable(connectionSource, DownloadInfo::class.java)
    }

    override fun onUpgrade(database: SQLiteDatabase?, connectionSource: ConnectionSource?, oldVersion: Int, newVersion: Int) {
        TableUtils.dropTable<DownloadInfo, Any>(connectionSource, DownloadInfo::class.java, true)
    }
}