package com.smartsense.gifkar;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.smartsense.gifkar.utill.CommonUtil;
import com.smartsense.gifkar.utill.LocationSettingsHelper;


public class SplashActivity extends AppCompatActivity implements AnimationListener {
    private LocationSettingsHelper mSettingsHelper;
    ImageView img1, img2, img3, img4, img5;
    private Animation an1, an2, an3, an4, an5, an6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }else{
            View decorView = getWindow().getDecorView();
// Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;

            decorView.setSystemUiVisibility(uiOptions);

//            getSupportActionBar().hide();


        }
        img1 = (ImageView) findViewById(R.id.imageView6);
        img2 = (ImageView) findViewById(R.id.imageView2);
        img3 = (ImageView) findViewById(R.id.imageView3);
        img4 = (ImageView) findViewById(R.id.imageView4);
        img5 = (ImageView) findViewById(R.id.imageView5);
        an1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.together);
        img1.startAnimation(an1);
        an2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.top);
        an3 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.top);
        an4 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.top);
        an5 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.top);
        an1.setAnimationListener(this);
        an2.setAnimationListener(this);
        an3.setAnimationListener(this);
        an4.setAnimationListener(this);
        an5.setAnimationListener(this);

        an6 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out);
//        if (CommonUtil.isGPS(getApplicationContext())) {
        startApp();
//        } else {
//            mSettingsHelper = new LocationSettingsHelper(this);
//            mSettingsHelper.checkSettings();
//        }

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        // TODO Auto-generated method stub
        Log.i("start", "yes");
        if (animation == an1) {
            img3.setVisibility(View.VISIBLE);
            img3.startAnimation(an3);
        } else if (animation == an3) {
            img2.setVisibility(View.VISIBLE);
            img2.startAnimation(an2);
        } else if (animation == an2) {
            img4.setVisibility(View.VISIBLE);
            img4.startAnimation(an4);
        } else if (animation == an4) {
            img5.setVisibility(View.VISIBLE);
            img5.startAnimation(an5);
        } else if (animation == an5) {
            img5.setVisibility(View.INVISIBLE);
            img4.setVisibility(View.INVISIBLE);
            img3.setVisibility(View.INVISIBLE);
            img2.setVisibility(View.INVISIBLE);
            img2.startAnimation(an6);
            img3.startAnimation(an6);
            img4.startAnimation(an6);
            img5.startAnimation(an6);
            img1.startAnimation(an1);
        }

    }

    public void startApp() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
//                    if (SharedPreferenceUtil.getBoolean(Constants.PrefKeys.PREF_ACCESS_TOKEN, false))
                    if (CommonUtil.isInternet(SplashActivity.this)) {
//                        startActivity(new Intent(getBaseContext(), GifkarActivity.class));
                        startActivity(new Intent(getBaseContext(), StartActivity.class));
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


    @Override
    public void onAnimationRepeat(Animation animation) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAnimationStart(Animation animation) {
        // TODO Auto-generated method stub

    }

}