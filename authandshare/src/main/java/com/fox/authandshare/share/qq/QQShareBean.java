package com.fox.authandshare.share.qq;

/**
 * Created by fox.hu on 2018/9/4.
 */

public class QQShareBean {
    private static final String TAG = QQShareBean.class.getSimpleName();
    private String title;
    private String targetUrl;
    private String imgUrl;
    private String imgLocalUrl;
    private String summary;
    private String audioUrl;
    private String appName;

    public String getTitle() {
        return title;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getImgLocalUrl() {
        return imgLocalUrl;
    }

    public String getSummary() {
        return summary;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public String getAppName() {
        return appName;
    }

    public QQShareBean(Builder builder) {
        this.title = builder.title;
        this.targetUrl = builder.targetUrl;
        this.imgUrl = builder.imgUrl;
        this.imgLocalUrl = builder.imgLocalUrl;
        this.summary = builder.summary;
        this.audioUrl = builder.audioUrl;
        this.appName = builder.appName;
    }

    public static class Builder {
        private String title;
        private String targetUrl;
        private String imgUrl;
        private String imgLocalUrl;
        private String summary;
        private String audioUrl;
        private String appName;

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder targetUrl(String targetUrl) {
            this.targetUrl = targetUrl;
            return this;
        }

        public Builder imgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
            return this;
        }

        public Builder imgLocalUrl(String imgLocalUrl) {
            this.imgLocalUrl = imgLocalUrl;
            return this;
        }

        public Builder summary(String summary) {
            this.summary = summary;
            return this;
        }

        public Builder audioUrl(String audioUrl) {
            this.audioUrl = audioUrl;
            return this;
        }

        public Builder appName(String appName) {
            this.appName = appName;
            return this;
        }

        public QQShareBean build() {
            return new QQShareBean(this);
        }
    }
}
