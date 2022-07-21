package com.silver.fox.ext

import java.io.*
import java.nio.charset.Charset
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipException
import java.util.zip.ZipFile
import java.util.zip.ZipInputStream

val File.canListFiles: Boolean
    get() = canRead() and isDirectory

/**
 * Total size (include all subFile)
 */
val File.totalSize: Long
    get() = if (isFile) length() else getFolderSize(this)

/**
 * Formatted total size (include all subFile)
 */
val File.formatSize: String
    get() = getFormatFileSize(totalSize)

/**
 * Return file's mimeType, such as "png"
 */
val File.mimeType: String
    get() = getMimeType(extension, isDirectory)

/**
 * List sub files
 * @param isRecursive whether to list recursively
 * @param filter exclude some files
 */
fun File.listFiles(
    isRecursive: Boolean = false,
    filter: ((file: File) -> Boolean)? = null
): Array<out File> {
    val fileList = if (!isRecursive) listFiles() else getAllSubFile(this)
    var result: Array<File> = arrayOf()
    return if (filter == null) fileList
    else {
        for (file in fileList) {
            if (filter(file)) result = result.plus(file)
        }
        result
    }
}

/**
 * write some text to file
 * @param append whether to append or overwrite
 * @param charset default charset is utf-8
 */
fun File.writeText(append: Boolean = false, text: String, charset: Charset = Charsets.UTF_8) {
    if (append) appendText(text, charset) else writeText(text, charset)
}

/**
 * write some bytes to file
 * @param append whether to append or overwrite
 */
fun File.writeBytes(append: Boolean = false, bytes: ByteArray) {
    if (append) appendBytes(bytes) else writeBytes(bytes)
}

/**
 *  copy file
 *  @param destFile dest file/folder
 *  @param overwrite whether to override dest file/folder if exist
 *  @param reserve Whether to reserve source file/folder
 */
fun File.moveTo(destFile: File, overwrite: Boolean = true, reserve: Boolean = true): Boolean {
    val dest = copyRecursively(destFile, overwrite)
    if (!reserve) deleteRecursively()
    return dest
}

/**
 * copy file with progress callback
 * @param destFolder dest folder
 * @param overwrite whether to override dest file/folder if exist
 * @param func progress callback (from 0 to 100)
 */
fun File.moveToWithProgress(
    destFolder: File,
    overwrite: Boolean = true,
    reserve: Boolean = true,
    func: ((file: File, i: Int) -> Unit)? = null
) {

    if (isDirectory) copyFolder(this, File(destFolder, name), overwrite, func)
    else copyFile(this, File(destFolder, name), overwrite, func)

    if (!reserve) deleteRecursively()
}

/** Rename to newName */
fun File.rename(newName: String) =
    rename(File("$parent${File.separator}$newName"))

/** Rename to newFile's name */
fun File.rename(newFile: File) =
    if (newFile.exists()) false else renameTo(newFile)

/**
 * turn target File to bytearray
 */
fun File.getBytes(): ByteArray? {
    val inputStream: InputStream = DataInputStream(FileInputStream(this))
    var len: Int
    val size = 1024
    val bos = ByteArrayOutputStream()
    var buf = ByteArray(size)
    while (inputStream.read(buf, 0, size).also { len = it } != -1) bos.write(buf, 0, len)
    buf = bos.toByteArray()
    return buf
}

fun File.unzip( targetDirectory: File?) {
    val zis = ZipInputStream(
        BufferedInputStream(FileInputStream(this))
    )
    zis.use { it ->
        var ze: ZipEntry
        var count: Int
        val buffer = ByteArray(8192)
        while (it.nextEntry.also { ze = it } != null) {
            val file = File(targetDirectory, ze.name)
            val dir = if (ze.isDirectory) file else file.parentFile
            if (!dir.isDirectory && !dir.mkdirs()) throw FileNotFoundException(
                "Failed to ensure directory: " +
                        dir.absolutePath
            )
            if (ze.isDirectory) continue
            val fout = FileOutputStream(file)
            fout.use { f ->
                while (it.read(buffer).also { count = it } != -1) f.write(buffer, 0, count)
            }
            /* if time should be restored as well
            long time = ze.getTime();
            if (time > 0)
                file.setLastModified(time);
            */
        }
    }
}

/**
 * 解压缩功能.
 * 将zipFile文件解压到folderPath目录下.
 *
 * @throws Exception
 */
fun File.upZipFile(folderPath: String) {
    val zfile = ZipFile(this)
    val zList: Enumeration<*> = zfile.entries()
    var ze: ZipEntry? = null
    val size = 1024 * 64
    val buf = ByteArray(size)
    while (zList.hasMoreElements()) {
        ze = zList.nextElement() as ZipEntry
        val destFile = File(File(folderPath), ze.name)
        if (ze.isDirectory) {
            if (destFile.isFile) {
                destFile.delete()
            }
            destFile.mkdirs()
            continue
        }
        val parent = destFile.parentFile
        if (!parent.exists()) {
            parent.mkdirs()
        }
        val os: OutputStream = BufferedOutputStream(FileOutputStream(destFile))
        val inputStream: InputStream = BufferedInputStream(zfile.getInputStream(ze))
        var readLen = 0
        while (inputStream.read(buf, 0, size).also { readLen = it } != -1) {
            os.write(buf, 0, readLen)
        }
        inputStream.close()
        os.close()
    }
    zfile.close()
}
