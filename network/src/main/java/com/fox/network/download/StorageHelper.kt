package com.fox.network.download

import android.os.Environment
import android.os.StatFs
import com.arialyy.aria.util.FileUtil
import java.util.*

/**
 * @Author fox.hu
 * @Date 2019/12/10 10:14
 */
class StorageHelper {

    companion object {

        private val units = arrayOf("B", "KB", "MB", "GB", "TB")

        @JvmStatic
        fun getTotalSize(): String {
            val statFs = StatFs(Environment.getExternalStorageDirectory().path)
            val totalSize = statFs.totalBytes
            return getUnit(totalSize.toFloat())
        }

        @JvmStatic
        fun getAvailableSize(): String {
            val statFs = StatFs(Environment.getExternalStorageDirectory().path)
            val availableSize = statFs.availableBytes
            return getUnit(availableSize.toFloat())
        }

        /**
         * 单位转换
         */
        @JvmStatic
        fun getUnit(size: Float): String {
            var tSize = size
            var index = 0
            while (tSize > 1024 && index < 4) {
                tSize /= 1024
                index++
            }
            return String.format(Locale.getDefault(), " %.2f%s", tSize, units[index])
        }

        @JvmStatic
        fun checkSDMemorySpace(filePath: String, fileSize: Long): Boolean {
            return FileUtil.checkSDMemorySpace(filePath, fileSize)
        }
    }
}
