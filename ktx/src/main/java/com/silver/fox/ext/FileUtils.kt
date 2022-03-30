package com.silver.fox.ext

import android.content.Context
import android.content.res.AssetManager
import java.io.*
import java.nio.ByteBuffer
import java.text.DecimalFormat

/**
 * Return the file size, include all sub files
 */
fun getFolderSize(file: File): Long {
    var total = 0L
    for (subFile in file.listFiles()) {
        total += if (subFile.isFile) subFile.length()
        else getFolderSize(subFile)
    }
    return total
}

/**
 * Return the formatted file size, like "4.78 GB"
 * @param unit 1000 or 1024, default to 1000
 */
fun getFormatFileSize(size: Long, unit: Int = 1000): String {
    val formatter = DecimalFormat("####.00")
    return when {
        size < 0 -> "0 B"
        size < unit -> "$size B"
        size < unit * unit -> "${formatter.format(size.toDouble() / unit)} KB"
        size < unit * unit * unit -> "${formatter.format(size.toDouble() / unit / unit)} MB"
        else -> "${formatter.format(size.toDouble() / unit / unit / unit)} GB"
    }
}

/**
 * Return all subFile in the folder
 */
fun getAllSubFile(folder: File): Array<File> {
    var fileList: Array<File> = arrayOf()
    if (!folder.canListFiles) return fileList
    for (subFile in folder.listFiles())
        fileList = if (subFile.isFile) fileList.plus(subFile)
        else fileList.plus(getAllSubFile(subFile))
    return fileList
}

/**
 * copy the [sourceFile] to the [destFile], only for file, not for folder
 * @param overwrite if the destFile is exist, whether to overwrite it
 */
fun copyFile(
    sourceFile: File,
    destFile: File,
    overwrite: Boolean,
    func: ((file: File, i: Int) -> Unit)? = null
) {

    if (!sourceFile.exists()) return

    if (destFile.exists()) {
        val stillExists = if (!overwrite) true else !destFile.delete()

        if (stillExists) {
            return
        }
    }

    if (!destFile.exists()) destFile.createNewFile()

    val inputStream = FileInputStream(sourceFile)
    val outputStream = FileOutputStream(destFile)
    val iChannel = inputStream.channel
    val oChannel = outputStream.channel


    val totalSize = sourceFile.length()
    val buffer = ByteBuffer.allocate(1024)
    var hasRead = 0f
    var progress = -1
    while (true) {
        buffer.clear()
        val read = iChannel.read(buffer)
        if (read == -1)
            break
        buffer.limit(buffer.position())
        buffer.position(0)
        oChannel.write(buffer)
        hasRead += read

        func?.let {
            val newProgress = ((hasRead / totalSize) * 100).toInt()
            if (progress != newProgress) {
                progress = newProgress
                it(sourceFile, progress)
            }
        }
    }

    inputStream.close()
    outputStream.close()
}

/**
 * copy the [sourceFolder] to the [destFolder]
 * @param overwrite if the destFile is exist, whether to overwrite it
 */
fun copyFolder(
    sourceFolder: File,
    destFolder: File,
    overwrite: Boolean,
    func: ((file: File, i: Int) -> Unit)? = null
) {
    if (!sourceFolder.exists()) return

    if (!destFolder.exists()) {
        val result = destFolder.mkdirs()
        if (!result) return
    }

    for (subFile in sourceFolder.listFiles()) {
        if (subFile.isDirectory) {
            copyFolder(
                subFile,
                File("${destFolder.path}${File.separator}${subFile.name}"),
                overwrite,
                func
            )
        } else {
            copyFile(subFile, File(destFolder, subFile.name), overwrite, func)
        }
    }
}

fun getExternalDir(context: Context, folder: String): String {
    return context.getExternalFilesDir(folder)?.path + File.separator
}

/**
 * 递归拷贝Asset目录中的文件到rootDir中
 * Recursively copy the files in the Asset directory to rootDir
 * @param assets
 * @param path
 * @param rootDir
 * @throws IOException
 */

fun copyAssets(
    assets: AssetManager,
    path: String,
    rootDir: String,
    deleteIfExist: Boolean = false
) {
    if (isAssetsDir(assets, path)) {
        val dir = File(rootDir, path)
        if (deleteIfExist && dir.exists()) {
            deleteDirectory(dir)
        }
        check(!(!dir.exists() && !dir.mkdirs())) { "mkdir failed" }
        for (s in assets.list(path)!!) {
            copyAssets(assets, "$path/$s", rootDir, deleteIfExist)
        }
    } else {
        val input = assets.open(path)
        val dest = File(rootDir, path)
        copyToFileOrThrow(input, dest)
    }
}

fun isAssetsDir(assets: AssetManager, path: String?): Boolean {
    if (path == null) return false
    try {
        val files = assets.list(path)
        return files != null && files.isNotEmpty()
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return false
}

fun copyToFileOrThrow(inputStream: InputStream, destFile: File) {
    var buffer = ByteArray(64 * 1024)
    if (destFile.exists()) {
        return
    }
    val file = destFile.parentFile
    if (file != null && !file.exists()) {
        file.mkdirs()
    }
    val out = FileOutputStream(destFile)
    try {
        if (buffer == null || buffer.isEmpty()) {
            buffer = ByteArray(64 * 1024)
        }
        var bytesRead: Int
        while (inputStream.read(buffer).also { bytesRead = it } >= 0) {
            out.write(buffer, 0, bytesRead)
        }
    } finally {
        out.flush()
        try {
            out.fd.sync()
        } catch (e: IOException) {
        }
        out.close()
    }
}

fun deleteDirectory(fileOrDirectory: File) {
    val children = fileOrDirectory.listFiles()
    if (fileOrDirectory.isDirectory && children != null) {
        for (child in children) {
            deleteDirectory(child)
        }
    }
    fileOrDirectory.delete()
}