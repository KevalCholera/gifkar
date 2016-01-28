package com.smartsense.gifkar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mpt.storage.SharedPreferenceUtil;
import com.smartsense.gifkar.utill.CommonUtil;
import com.smartsense.gifkar.utill.Constants;

public class FeedUsActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView btBack;
    private LinearLayout llRateUs, llEmailUs, llContactUs;
    TextView llSuggestShop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.action_bar_center, null);
        TextView titleTextView = (TextView) v.findViewById(R.id.actionBarTitle);
        titleTextView.setText(getResources().getString(R.string.screen_feed));
        btBack = (ImageView) v.findViewById(R.id.btActionBarBack);
        btBack.setOnClickListener(this);
        getSupportActionBar().setCustomView(v);

        setContentView(R.layout.activity_feed_us);
        llContactUs = (LinearLayout) findViewById(R.id.llContactUs);
        llContactUs.setOnClickListener(this);
        llRateUs = (LinearLayout) findViewById(R.id.llRateUs);
        llRateUs.setOnClickListener(this);
        llEmailUs = (LinearLayout) findViewById(R.id.llEmailUs);
        llEmailUs.setOnClickListener(this);
        llSuggestShop = (TextView) findViewById(R.id.llSuggestShop);
        llSuggestShop.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btActionBarBack:
                finish();
                break;
            case R.id.llEmailUs:
                final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setType("plain/text");


                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                        "Android APP-Feedback");
                emailIntent.setType("plain/text");
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,
                        "\n\n from " + SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_FULLNAME, ""));
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"feedback@gifkar.com"});
                startActivity(Intent.createChooser(emailIntent, "Send mail..."));
//                openInfoPopup();
                break;
            case R.id.llRateUs:
                CommonUtil.openAppRating(this);
                break;
            case R.id.llContactUs:
                openInfoPopup();
//                Intent intent = new Intent(Intent.ACTION_DIAL);
//                intent.setData(Uri.parse("tel:9999999999"));
//                startActivity(intent);
                break;
            case R.id.llSuggestShop:
                startActivity(new Intent(this, SuggestNewShopActivity.class));
                break;


        }
    }

    public void openInfoPopup() {
        try {
            final AlertDialog.Builder alertDialogs = new AlertDialog.Builder(this);
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View dialog = inflater.inflate(R.layout.dialog_info, null);
            alertDialogs.setView(dialog);
            alertDialogs.setCancelable(true);
            TextView tvDialog=(TextView) dialog.findViewById(R.id.textInfoDialog);
            tvDialog.setText("Gifkar Technology Private Limited\n" +
                    "OFFICE NO 2, C WING GROUND FLOOR,\n" +
                    "SHREENATH PLAZA,1184/4\n" +
                    "FC ROAD, SHIVAJI NAGAR\n" +
                    "PUNE. MAHARASHTRA - 411005\n" +
                    "www.gifkar.com\n" +
                    "info@gifkar.com");
            TextView tvDialogHead=(TextView) dialog.findViewById(R.id.textInfoDialogHead);
            tvDialogHead.setText("Contact us");
            AlertDialog alert = alertDialogs.create();
            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
