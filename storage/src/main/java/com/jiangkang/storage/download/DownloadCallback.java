package com.jiangkang.storage.download;

/**
 * Created by jiangkang on 2017/12/6.
 * description：
 */

public interface DownloadCallback {

    void onStart(long currentSize, long totalSize, int progress);

    void onDownloading(long currentSize, long totalSize, int progress);

    void onPause();

    void onCancel();

    void onWaiting();

    void onFinished();

    void onError(String errorMsg);

}
