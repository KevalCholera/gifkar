package com.smartsense.gifkar;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ReferFriendsActivity extends Fragment implements View.OnClickListener {
    ImageView btBack;
    LinearLayout llReferMsg;
    private LinearLayout llReferFB;
    private LinearLayout llReferGmail;
    private LinearLayout llReferWhats;
    private LinearLayout llReferGPlus;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.activity_refer_friends, container, false);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar_gifkar);
        TextView actionBarTitle = (TextView) toolbar.findViewById(R.id.actionBarHomeTitle);
        actionBarTitle.setText(getResources().getString(R.string.screen_refer));
        ImageView btFilter = (ImageView) toolbar.findViewById(R.id.btActionBarfilter);
        btFilter.setVisibility(View.INVISIBLE);
        ImageView btSearch = (ImageView) toolbar.findViewById(R.id.btActionBarSearch);
        btSearch.setVisibility(View.INVISIBLE);
        llReferMsg = (LinearLayout) view.findViewById(R.id.llReferMsg);
        llReferMsg.setOnClickListener(this);
        llReferFB = (LinearLayout) view.findViewById(R.id.llReferFB);
        llReferFB.setOnClickListener(this);
        llReferGmail = (LinearLayout) view.findViewById(R.id.llReferGMAIL);
        llReferGmail.setOnClickListener(this);
        llReferGPlus = (LinearLayout) view.findViewById(R.id.llReferGPlus);
        llReferGPlus.setOnClickListener(this);
        llReferWhats = (LinearLayout) view.findViewById(R.id.llReferWhatsApp);
        llReferWhats.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.btActionBarBack:
//                finish();
//                break;
            case R.id.llReferWhatsApp:
                if (isAppInstalled("com.whatsapp")) {
                    share("com.whatsapp");
                } else {
                    Toast.makeText(getActivity(), "WhatsApp not Installed", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            case R.id.llReferMsg:

                if (isAppInstalled("com.android.mms")) {
                    share("com.android.mms");
                } else {
                    Toast.makeText(getActivity(), "Message not Installed", Toast.LENGTH_SHORT)
                            .show();
                }

                break;
            case R.id.llReferGPlus:
                if (isAppInstalled("com.google.android.apps.plus")) {
                    share("com.google.android.apps.plus");
                } else {
                    Toast.makeText(getActivity(), "Google + not Installed", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            case R.id.llReferGMAIL:
                if (isAppInstalled("com.google.android.gm")) {
                    share("com.google.android.gm");
                } else {
                    Toast.makeText(getActivity(), "Gmail not Installed", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            case R.id.llReferFB:

                if (isAppInstalled("com.facebook.katana")) {
                    share("com.facebook.katana");
                } else {
                    Toast.makeText(getActivity(), "Facebook not Installed", Toast.LENGTH_SHORT)
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
        PackageManager pm = getActivity().getPackageManager();
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
            intent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.share_msg));
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
