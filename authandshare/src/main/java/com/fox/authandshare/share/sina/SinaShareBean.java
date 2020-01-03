package com.fox.authandshare.share.sina;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fox.hu on 2018/9/4.
 */

public class SinaShareBean implements Parcelable {
    private String text;
    private String title;
    private String actionUrl;

    private int resId;

    private String audioPath;

    public String getText() {
        return text;
    }

    public String getTitle() {
        return title;
    }

    public String getActionUrl() {
        return actionUrl;
    }

    public int getResId() {
        return resId;
    }

    public String getAudioPath() {
        return audioPath;
    }

    public SinaShareBean(Builder builder) {
        this.text = builder.text;
        this.title = builder.title;
        this.actionUrl = builder.actionUrl;
        this.resId = builder.resId;
        this.audioPath = builder.audioPath;
    }

    public static class Builder {
        private String text;
        private String title;
        private String actionUrl;

        private int resId;

        private String audioPath;

        public Builder text(String text) {
            this.text = text;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder actionUrl(String actionUrl) {
            this.actionUrl = actionUrl;
            return this;
        }

        public Builder resId(int resId) {
            this.resId = resId;
            return this;
        }

        public Builder audioPath(String audioPath) {
            this.audioPath = audioPath;
            return this;
        }

        public SinaShareBean bulid() {
            return new SinaShareBean(this);
        }
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.text);
        dest.writeString(this.title);
        dest.writeString(this.actionUrl);
        dest.writeInt(this.resId);
        dest.writeString(this.audioPath);
    }

    protected SinaShareBean(Parcel in) {
        this.text = in.readString();
        this.title = in.readString();
        this.actionUrl = in.readString();
        this.resId = in.readInt();
        this.audioPath = in.readString();
    }

    public static final Parcelable.Creator<SinaShareBean> CREATOR = new Parcelable.Creator<SinaShareBean>() {
        @Override
        public SinaShareBean createFromParcel(Parcel source) {return new SinaShareBean(source);}

        @Override
        public SinaShareBean[] newArray(int size) {return new SinaShareBean[size];}
    };
}
