package com.smartsense.gifkar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AboutUsActivity extends Fragment implements View.OnClickListener {
    ImageView btBack;
    private LinearLayout llGifkar, llHelp, llFAQs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.activity_about_us, container, false);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar_gifkar);
        TextView actionBarTitle = (TextView) toolbar.findViewById(R.id.actionBarHomeTitle);
        actionBarTitle.setText(getResources().getString(R.string.screen_about));
        ImageView btFilter = (ImageView) toolbar.findViewById(R.id.btActionBarfilter);
        btFilter.setVisibility(View.INVISIBLE);
        ImageView btSearch = (ImageView) toolbar.findViewById(R.id.btActionBarSearch);
        btSearch.setVisibility(View.INVISIBLE);
        llGifkar=(LinearLayout) view.findViewById(R.id.llGifkar);
        llGifkar.setOnClickListener(this);
        llHelp=(LinearLayout) view.findViewById(R.id.llHelp);
        llHelp.setOnClickListener(this);
        llFAQs=(LinearLayout) view.findViewById(R.id.llFAQS);
        llFAQs.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.btActionBarBack:
//                finish();
//                break;
//            case R.id.llGifkar:
//                finish();
//                break;
//            case R.id.llHelp:
//                finish();
//                break;
//            case R.id.llFAQS:
//                finish();
//                break;

        }
    }
}
