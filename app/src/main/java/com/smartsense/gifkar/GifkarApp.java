package com.smartsense.gifkar;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.mpt.storage.SharedPreferenceUtil;
import com.smartsense.gifkar.imageutil.BitmapCache;
import com.smartsense.gifkar.imageutil.BitmapUtil;
import com.smartsense.gifkar.imageutil.DiskBitmapCache;
import com.smartsense.gifkar.textstyleutil.CooperHewittExtractor;
import com.smartsense.gifkar.textstyleutil.TypefaceManager;

import java.io.InputStream;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

public class GifkarApp extends Application {
    private static GifkarApp appInstance;
    public static final String TAG = GifkarApp.class.getSimpleName();
    private static char[] KEYSTORE_PASSWORD = "gifkar".toCharArray();
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    @Override
    public void onCreate() {
        super.onCreate();
        appInstance = this;
        TypefaceManager.addTextStyleExtractor(CooperHewittExtractor.getInstance());
//        Parse.initialize(this, "9yjUxrJuubYfJQvh8gONZuZqTEu3gcpqB1mdRkpH", "w3znN1nsItMDKIZaTJ8qzdRDPnfFKeW6uAI56C8Y");
//        ParseInstallation.getCurrentInstallation().saveInBackground();
        SharedPreferenceUtil.init(this);
    }

    public static synchronized GifkarApp getInstance() {
        return appInstance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
//            requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue = Volley.newRequestQueue(getApplicationContext(),new HurlStack(null, newSslSocketFactory()));
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

    private SSLSocketFactory newSslSocketFactory() {
        try {
            // Get an instance of the Bouncy Castle KeyStore format
            KeyStore trusted = KeyStore.getInstance("BKS");
            // Get the raw resource, which contains the keystore with
            // your trusted certificates (root and any intermediate certs)
            InputStream in = GifkarApp.getInstance().getResources().openRawResource(R.raw.gifkar);
            try {
                // Initialize the keystore with the provided trusted certificates
                // Provide the password of the keystore
                trusted.load(in, KEYSTORE_PASSWORD);
            } finally {
                in.close();
            }

            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(trusted);

            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);

            SSLSocketFactory sf = context.getSocketFactory();
            return sf;
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }
}