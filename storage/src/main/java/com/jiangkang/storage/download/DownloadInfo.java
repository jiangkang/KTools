package com.jiangkang.storage.download;

import java.io.Serializable;

/**
 * Created by jiangkang on 2017/12/6.
 * description：
 */

public class DownloadInfo implements Serializable{

    private String downloadUrl;

    private String storePath;

    private String filename;

    private int currentLength;

    private int totalLength;

    private int progress;

    private long date;

    private int taskCount;

    private String lastModified;

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public DownloadInfo setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
        return this;
    }

    public String getStorePath() {
        return storePath;
    }

    public DownloadInfo setStorePath(String storePath) {
        this.storePath = storePath;
        return this;
    }

    public String getFilename() {
        return filename;
    }

    public DownloadInfo setFilename(String filename) {
        this.filename = filename;
        return this;
    }

    public int getCurrentLength() {
        return currentLength;
    }

    public DownloadInfo setCurrentLength(int currentLength) {
        this.currentLength = currentLength;
        return this;
    }

    public int getTotalLength() {
        return totalLength;
    }

    public DownloadInfo setTotalLength(int totalLength) {
        this.totalLength = totalLength;
        return this;
    }

    public int getProgress() {
        return progress;
    }

    public DownloadInfo setProgress(int progress) {
        this.progress = progress;
        return this;
    }

    public long getDate() {
        return date;
    }

    public DownloadInfo setDate(long date) {
        this.date = date;
        return this;
    }

    public int getTaskCount() {
        return taskCount;
    }

    public DownloadInfo setTaskCount(int taskCount) {
        this.taskCount = taskCount;
        return this;
    }

    public String getLastModified() {
        return lastModified;
    }

    public DownloadInfo setLastModified(String lastModified) {
        this.lastModified = lastModified;
        return this;
    }
}
