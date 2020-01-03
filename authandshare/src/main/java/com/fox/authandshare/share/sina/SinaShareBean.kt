package com.fox.authandshare.share.sina

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable

/**
 * Created by fox.hu on 2018/9/4.
 */

class SinaShareBean : Parcelable {
    var text: String? = null
        private set
    var title: String? = null
        private set
    var actionUrl: String? = null
        private set

    var resId: Int = 0
        private set

    var audioPath: String? = null
        private set

    constructor(builder: Builder) {
        this.text = builder.text
        this.title = builder.title
        this.actionUrl = builder.actionUrl
        this.resId = builder.resId
        this.audioPath = builder.audioPath
    }

    class Builder {
        var text: String? = null
        var title: String? = null
        var actionUrl: String? = null

        var resId: Int = 0

        var audioPath: String? = null

        fun text(text: String): Builder {
            this.text = text
            return this
        }

        fun title(title: String): Builder {
            this.title = title
            return this
        }

        fun actionUrl(actionUrl: String): Builder {
            this.actionUrl = actionUrl
            return this
        }

        fun resId(resId: Int): Builder {
            this.resId = resId
            return this
        }

        fun audioPath(audioPath: String): Builder {
            this.audioPath = audioPath
            return this
        }

        fun bulid(): SinaShareBean {
            return SinaShareBean(this)
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(this.text)
        dest.writeString(this.title)
        dest.writeString(this.actionUrl)
        dest.writeInt(this.resId)
        dest.writeString(this.audioPath)
    }

    protected constructor(`in`: Parcel) {
        this.text = `in`.readString()
        this.title = `in`.readString()
        this.actionUrl = `in`.readString()
        this.resId = `in`.readInt()
        this.audioPath = `in`.readString()
    }

    companion object {

        @SuppressLint("ParcelCreator")
        val CREATOR: Parcelable.Creator<SinaShareBean> =
            object : Parcelable.Creator<SinaShareBean> {
                override fun createFromParcel(source: Parcel): SinaShareBean {
                    return SinaShareBean(source)
                }

                override fun newArray(size: Int): Array<SinaShareBean?> {
                    return arrayOfNulls(size)
                }
            }
    }
}
