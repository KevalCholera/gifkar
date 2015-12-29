package com.smartsense.gifkar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartsense.gifkar.utill.CommonUtil;

public class FeedUsActivity extends Fragment implements View.OnClickListener {

    private ImageView btBack;
    private LinearLayout llRateUs, llEmailUs, llContactUs;
    TextView llSuggestShop;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = (View) inflater.inflate(R.layout.activity_feed_us, container, false);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar_gifkar);
        TextView actionBarTitle = (TextView) toolbar.findViewById(R.id.actionBarHomeTitle);
        actionBarTitle.setText(getResources().getString(R.string.screen_feed));
        ImageView btFilter = (ImageView) toolbar.findViewById(R.id.btActionBarfilter);
        btFilter.setVisibility(View.INVISIBLE);
        ImageView btSearch = (ImageView) toolbar.findViewById(R.id.btActionBarSearch);
        btSearch.setVisibility(View.INVISIBLE);
        llContactUs = (LinearLayout) view.findViewById(R.id.llContactUs);
        llContactUs.setOnClickListener(this);
        llRateUs = (LinearLayout) view.findViewById(R.id.llRateUs);
        llRateUs.setOnClickListener(this);
        llEmailUs = (LinearLayout) view.findViewById(R.id.llEmailUs);
        llEmailUs.setOnClickListener(this);
        llSuggestShop = (TextView) view.findViewById(R.id.llSuggestShop);
        llSuggestShop.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.btActionBarBack:
//                finish();
//                break;
            case R.id.llEmailUs:
                openInfoPopup();
                break;
            case R.id.llRateUs:
                CommonUtil.openAppRating(getActivity());
                break;
            case R.id.llContactUs:
                openInfoPopup();
                break;
            case R.id.llSuggestShop:
                startActivity(new Intent(getActivity(), SuggestNewShopActivity.class));
                break;


        }
    }

    public void openInfoPopup() {
        try {
            final AlertDialog.Builder alertDialogs = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View dialog = inflater.inflate(R.layout.dialog_info, null);
            alertDialogs.setView(dialog);
//            alertDialogs.setCancelable(false);
            AlertDialog alert = alertDialogs.create();
            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
