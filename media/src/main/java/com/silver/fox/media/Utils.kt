package com.silver.fox.media

import android.media.MediaExtractor
import android.media.MediaFormat
import android.util.Log
import java.io.FileInputStream
import java.io.FileOutputStream
import kotlin.experimental.and
import kotlin.experimental.or

fun MediaExtractor.getTrackIndex(prefix: String): Int {
    for (i in 0..trackCount) {
        if (getTrackFormat(i).getString(MediaFormat.KEY_MIME)!!
                .startsWith(prefix)
        ) {
            return i
        }
    }
    return -1
}


