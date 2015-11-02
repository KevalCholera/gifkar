package com.smartsense.gifkar;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.mpt.storage.SharedPreferenceUtil;
import com.smartsense.gifkar.imageutil.BitmapCache;
import com.smartsense.gifkar.imageutil.BitmapUtil;
import com.smartsense.gifkar.imageutil.DiskBitmapCache;

public class GifkarApp extends Application {
    private static GifkarApp appInstance;
    public static final String TAG = GifkarApp.class.getSimpleName();
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    @Override
    public void onCreate() {
        super.onCreate();
        appInstance = this;
        SharedPreferenceUtil.init(this);
    }

    public static synchronized GifkarApp getInstance() {
        return appInstance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return requestQueue;
    }

    public ImageLoader getDiskImageLoader() {
        getRequestQueue();
        if (imageLoader == null) {
            imageLoader = new ImageLoader(this.requestQueue,
                    new DiskBitmapCache(getCacheDir(),
                            BitmapUtil.MAX_CATCH_SIZE));
        }
        return this.imageLoader;
    }

    public ImageLoader getCacheImageLoader() {
        getRequestQueue();
        if (imageLoader == null) {
            imageLoader = new ImageLoader(this.requestQueue, new BitmapCache(
                    BitmapUtil.MAX_CATCH_SIZE));
        }
        return this.imageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (requestQueue != null) {
            requestQueue.cancelAll(tag);
        }
    }
}