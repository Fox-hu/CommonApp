package com.fox.authandshare.share.wechat

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable

/**
 * Created by fox.hu on 2018/9/7.
 */

class WeChatShareBean : Parcelable {
    var text: String? = null
        private set
    var imgPath: String? = null
        private set
    var mp3Url: String? = null
        private set
    var mediaDescr: String? = null
        private set
    var mediaTitle: String? = null
        private set
    var thumbImgId: Int = 0
        private set
    private var videoUrl: String? = null

    constructor(builder: Builder) {
        this.text = builder.text
        this.imgPath = builder.imgPath
        this.mp3Url = builder.mp3Url
        this.mediaDescr = builder.mediaDescr
        this.mediaTitle = builder.mediaTitle
        this.thumbImgId = builder.thumbImgId
        this.videoUrl = builder.videoUrl
    }

    constructor() {}

    class Builder {
        var text: String? = null
        var imgPath: String? = null
        var mp3Url: String? = null
        var mediaDescr: String? = null
        var mediaTitle: String? = null
        var thumbImgId: Int = 0
        var videoUrl: String? = null

        fun text(text: String): Builder {
            this.text = text
            return this
        }

        fun imgPath(imgPath: String): Builder {
            this.imgPath = imgPath
            return this
        }

        fun mp3Url(mp3Url: String): Builder {
            this.mp3Url = mp3Url
            return this
        }

        fun mediaDescr(mediaDescr: String): Builder {
            this.mediaDescr = mediaDescr
            return this
        }

        fun mediaTitle(mediaTitle: String): Builder {
            this.mediaTitle = mediaTitle
            return this
        }

        fun videoUrl(videoUrl: String): Builder {
            this.videoUrl = videoUrl
            return this
        }

        fun thumbImgId(thumbImgId: Int): Builder {
            this.thumbImgId = thumbImgId
            return this
        }

        fun bulid(): WeChatShareBean {
            return WeChatShareBean(this)
        }
    }

    protected constructor(`in`: Parcel) {
        this.text = `in`.readString()
        this.imgPath = `in`.readString()
        this.mp3Url = `in`.readString()
        this.mediaDescr = `in`.readString()
        this.mediaTitle = `in`.readString()
        this.thumbImgId = `in`.readInt()
        this.videoUrl = `in`.readString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(this.text)
        dest.writeString(this.imgPath)
        dest.writeString(this.mp3Url)
        dest.writeString(this.mediaDescr)
        dest.writeString(this.mediaTitle)
        dest.writeInt(this.thumbImgId)
        dest.writeString(this.videoUrl)
    }

    companion object {

        @SuppressLint("ParcelCreator")
        val CREATOR: Parcelable.Creator<WeChatShareBean> =
            object : Parcelable.Creator<WeChatShareBean> {
                override fun createFromParcel(source: Parcel): WeChatShareBean {
                    return WeChatShareBean(source)
                }

                override fun newArray(size: Int): Array<WeChatShareBean?> {
                    return arrayOfNulls(size)
                }
            }
    }
}
