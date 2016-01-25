package com.smartsense.gifkar.facebook;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.smartsense.gifkar.R;


public class FacebookLoginButtonProviderImpl implements FacebookProvider {

    private CallbackManager callbackManager;

    public void initSdk(Context context) {
        FacebookSdk.sdkInitialize(context);
    }

    public void addToContainer(ViewGroup container, FacebookCallback<LoginResult> facebookCallback) {
        callbackManager = CallbackManager.Factory.create();
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        LoginButton button = (LoginButton) inflater.inflate(R.layout.button_facebook_login, container, false);
        container.addView(button);
        button.registerCallback(callbackManager, facebookCallback);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}