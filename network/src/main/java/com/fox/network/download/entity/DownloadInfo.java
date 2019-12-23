package com.fox.network.download.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.fox.network.download.interfaces.DownloadAble;

/**
 * @Author fox.hu
 * @Date 2019/11/27 13:38
 */
@DatabaseTable(tableName = "DownloadInfo")
public class DownloadInfo implements DownloadAble {

    public DownloadInfo() {

    }

    @DatabaseField(id = true)
    private long id;

    @DatabaseField
    private String name;

    @DatabaseField
    private String url;

    @DatabaseField
    private String path;

    @DatabaseField
    private String convertFileSize;

    @DatabaseField
    private int percent;

    @DatabaseField
    private String convertSpeed;

    @DatabaseField
    private long fileSize;

    @DatabaseField
    private long retryCount;

    @DatabaseField
    private int downloadState;

    @DatabaseField
    private String belong;

    @DatabaseField
    private String tag;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getConvertFileSize() {
        return convertFileSize;
    }

    public void setConvertFileSize(String convertFileSize) {
        this.convertFileSize = convertFileSize;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public String getConvertSpeed() {
        return convertSpeed;
    }

    public void setConvertSpeed(String convertSpeed) {
        this.convertSpeed = convertSpeed;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public int getDownloadState() {
        return downloadState;
    }

    public void setDownloadState(int downloadState) {
        this.downloadState = downloadState;
    }

    @Override
    public String getBelong() {
        return belong;
    }

    public void setBelong(String belong) {
        this.belong = belong;
    }

    @Override
    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public long getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(long retryCount) {
        this.retryCount = retryCount;
    }

    public static class Builder {

        private String name;

        private String url;

        private String path;

        private String convertFileSize;

        private int percent;

        private String convertSpeed;

        private long fileSize;

        private int downloadState;

        private String belong;

        private String tag;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public Builder tag(String tag) {
            this.tag = tag;
            return this;
        }

        public Builder belong(String belong) {
            this.belong = belong;
            return this;
        }

        public DownloadInfo build() {
            return new DownloadInfo(this);
        }
    }

    private DownloadInfo(Builder builder) {
        this.name = builder.name;
        this.belong = builder.belong;
        this.url = builder.url;
        this.path = builder.path;
        this.tag = builder.tag;
    }

    @Override
    public String toString() {
        return "DownloadInfo{" + "id=" + id + ", name='" + name + '\'' + ", url='" + url + '\'' +
               ", path='" + path + '\'' + ", convertFileSize='" + convertFileSize + '\'' +
               ", percent=" + percent + ", convertSpeed='" + convertSpeed + '\'' + ", fileSize=" +
               fileSize + ", retryCount=" + retryCount + ", downloadState=" + downloadState +
               ", belong='" + belong + '\'' + ", tag='" + tag + '\'' + '}';
    }
}
