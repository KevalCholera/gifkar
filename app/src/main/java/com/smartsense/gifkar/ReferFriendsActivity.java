package com.smartsense.gifkar;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ReferFriendsActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView btBack;
    LinearLayout llReferMsg;
    private LinearLayout llReferFB;
    private LinearLayout llReferGmail;
    private LinearLayout llReferWhats;
    private LinearLayout llReferGPlus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.action_bar_center, null);
        TextView titleTextView = (TextView) v.findViewById(R.id.actionBarTitle);
        titleTextView.setText(getResources().getString(R.string.screen_notification));
        btBack = (ImageView) v.findViewById(R.id.btActionBarBack);
        btBack.setOnClickListener(this);
        getSupportActionBar().setCustomView(v);
        setContentView(R.layout.activity_refer_friends);
        llReferMsg = (LinearLayout) findViewById(R.id.llReferMsg);
        llReferMsg.setOnClickListener(this);
        llReferFB = (LinearLayout) findViewById(R.id.llReferFB);
        llReferFB.setOnClickListener(this);
        llReferGmail = (LinearLayout) findViewById(R.id.llReferGMAIL);
        llReferGmail.setOnClickListener(this);
        llReferGPlus = (LinearLayout) findViewById(R.id.llReferGPlus);
        llReferGPlus.setOnClickListener(this);
        llReferWhats = (LinearLayout) findViewById(R.id.llReferWhatsApp);
        llReferWhats.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btActionBarBack:
                finish();
                break;
            case R.id.llReferWhatsApp:
                if (isAppInstalled("com.whatsapp")) {
                    share("com.whatsapp");
                } else {
                    Toast.makeText(this, "WhatsApp not Installed", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            case R.id.llReferMsg:

                if (isAppInstalled("com.android.mms")) {
                    share("com.android.mms");
                } else {
                    Toast.makeText(this, "WhatsApp not Installed", Toast.LENGTH_SHORT)
                            .show();
                }

                break;
            case R.id.llReferGPlus:
                if (isAppInstalled("com.google.android.apps.plus")) {
                    share("com.google.android.apps.plus");
                } else {
                    Toast.makeText(this, "Google + not Installed", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            case R.id.llReferGMAIL:
                if (isAppInstalled("com.google.android.gm")) {
                    share("com.google.android.gm");
                } else {
                    Toast.makeText(this, "Gmail not Installed", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            case R.id.llReferFB:

                if (isAppInstalled("com.facebook.katana")) {
                    share("com.facebook.katana");
                } else {
                    Toast.makeText(this, "Facebook not Installed", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
        }
    }

    private void shareByMessage() {
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
        smsIntent.setData(Uri.parse("sms:"));
        smsIntent.putExtra("sms_body", getResources().getString(R.string.share_msg));
    }

    private boolean isAppInstalled(String packageName) {
        PackageManager pm = this.getPackageManager();
        boolean installed = false;
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
        }
        return installed;
    }

    private void share(String packageName) {

        if (packageName.equalsIgnoreCase("com.facebook.katana")) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setPackage("com.facebook.katana");
            intent.setType("text/plain");
            intent.putExtra( Intent.EXTRA_TEXT, getResources().getString(R.string.share_msg));
            startActivity(intent);
        } else {
            Intent waIntent = new Intent(Intent.ACTION_SEND);
            waIntent.setType("text/plain");
            waIntent.setPackage(packageName);
            waIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.share_msg));
            startActivity(Intent.createChooser(waIntent, "Share with"));
        }
    }
}
