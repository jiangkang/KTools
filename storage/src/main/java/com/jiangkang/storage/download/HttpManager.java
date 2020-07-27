package com.jiangkang.storage.download;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by jiangkang on 2017/12/6.
 * description：
 */

public class HttpManager {

    private static class Holder{
        private static final HttpManager sInstance = new HttpManager();
    }

    private OkHttpClient.Builder mBuilder;

    private HttpManager(){
        mBuilder = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS)
                .readTimeout(10,TimeUnit.SECONDS);
    }


    public static HttpManager getInstance(){
        return Holder.sInstance;
    }


    public Response initRequstSync(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .header("Range","bytes=0-")
                .build();
        return mBuilder.build().newCall(request).execute();
    }


    public Call initRequestAsync(String url, long start, long end, Callback callback){
        Request request = new Request.Builder()
                .url(url)
                .header("Range",String.format("bytes=%s-%s",start,end))
                .build();

        Call call = mBuilder.build().newCall(request);

        call.enqueue(callback);

        return call;
    }


    public Response initRequestSync(String url, String lastModified) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .header("Range", "bytes=0-")
                .header("If-Range", lastModified)
                .build();
        return mBuilder.build().newCall(request).execute();
    }





}
