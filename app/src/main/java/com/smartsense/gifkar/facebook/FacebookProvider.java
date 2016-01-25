package com.smartsense.gifkar.facebook;

import android.content.Context;
import android.content.Intent;
import android.view.ViewGroup;

import com.facebook.FacebookCallback;
import com.facebook.login.LoginResult;

/**
 * Interface for linking a 'textStyle' attribute with the actual font asset name.
 *
 * Created by evelina on 16/01/2014.
 */
public interface FacebookProvider {
    void initSdk(Context context);

    void addToContainer(ViewGroup container, FacebookCallback<LoginResult> facebookCallback);

    void onActivityResult(int requestCode, int resultCode, Intent data);
}
