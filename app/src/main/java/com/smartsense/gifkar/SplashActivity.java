package com.smartsense.gifkar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.smartsense.gifkar.utill.CommonUtil;
import com.smartsense.gifkar.utill.LocationSettingsHelper;


public class SplashActivity extends AppCompatActivity {
    private LocationSettingsHelper mSettingsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
//        if (CommonUtil.isGPS(getApplicationContext())) {
        startApp();
//        } else {
//            mSettingsHelper = new LocationSettingsHelper(this);
//            mSettingsHelper.checkSettings();
//        }

    }

    public void startApp() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
//                    if (SharedPreferenceUtil.getBoolean(Constants.PrefKeys.PREF_ACCESS_TOKEN, false))
                    if (CommonUtil.isInternet(SplashActivity.this)) {
//                        startActivity(new Intent(getBaseContext(), GifkarActivity.class));
                        startActivity(new Intent(getBaseContext(), LoginActivity.class));
                    } else
                        startActivity(new Intent(getBaseContext(), NoInternetConnection.class));
                    finish();

                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case LocationSettingsHelper.REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
//				Log.i("TAG", "User agreed to make required location settings changes.");
                        startApp();
                        break;
                    case Activity.RESULT_CANCELED:
//				Log.i("TAG", "User chose not to make required location settings changes.");
                        finish();
                        break;
                    default:
                        finish();
                        break;
                }
                break;

        }
    }

}