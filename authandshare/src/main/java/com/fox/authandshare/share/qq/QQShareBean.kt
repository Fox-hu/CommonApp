package com.fox.authandshare.share.qq

/**
 * Created by fox.hu on 2018/9/4.
 */

class QQShareBean(builder: Builder) {
    val title: String?
    val targetUrl: String?
    val imgUrl: String?
    val imgLocalUrl: String?
    val summary: String?
    val audioUrl: String?
    val appName: String?

    init {
        this.title = builder.title
        this.targetUrl = builder.targetUrl
        this.imgUrl = builder.imgUrl
        this.imgLocalUrl = builder.imgLocalUrl
        this.summary = builder.summary
        this.audioUrl = builder.audioUrl
        this.appName = builder.appName
    }

    class Builder {
        var title: String? = null
        var targetUrl: String? = null
        var imgUrl: String? = null
        var imgLocalUrl: String? = null
        var summary: String? = null
        var audioUrl: String? = null
        var appName: String? = null

        fun title(title: String): Builder {
            this.title = title
            return this
        }

        fun targetUrl(targetUrl: String): Builder {
            this.targetUrl = targetUrl
            return this
        }

        fun imgUrl(imgUrl: String): Builder {
            this.imgUrl = imgUrl
            return this
        }

        fun imgLocalUrl(imgLocalUrl: String): Builder {
            this.imgLocalUrl = imgLocalUrl
            return this
        }

        fun summary(summary: String): Builder {
            this.summary = summary
            return this
        }

        fun audioUrl(audioUrl: String): Builder {
            this.audioUrl = audioUrl
            return this
        }

        fun appName(appName: String): Builder {
            this.appName = appName
            return this
        }

        fun build(): QQShareBean {
            return QQShareBean(this)
        }
    }

    companion object {
        private val TAG = QQShareBean::class.java.simpleName
    }
}
