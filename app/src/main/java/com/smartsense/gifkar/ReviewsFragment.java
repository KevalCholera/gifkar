package com.smartsense.gifkar;


import android.app.AlertDialog;
import android.content.Context;
import android.media.Rating;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartsense.gifkar.utill.CircleImageView;

public class ReviewsFragment extends Fragment implements View.OnClickListener {
    private CircleImageView ivReviewUser;
    private TextView tvUserName;
    private EditText etReviewAdd;
    private Button btAddReview;
    private Rating rbReview;
    private LinearLayout llShopReview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.fragment_reviews, container, false);
        llShopReview = (LinearLayout) view.findViewById(R.id.llShopReview);
        llShopReview.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llShopReview:
                openAddReviewPopup();
                break;
            default:
        }
    }

    public void openAddReviewPopup() {
        try {
            final AlertDialog.Builder alertDialogs = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View dialog = inflater.inflate(R.layout.dialog_write_review, null);
            alertDialogs.setView(dialog);
//            ivReviewUser=(CircleImageView) dialog.findViewById(R.id.ivReviewUser);
            tvUserName = (TextView) dialog.findViewById(R.id.tvReviewUserName);
            etReviewAdd = (EditText) dialog.findViewById(R.id.etReviewAdd);
            btAddReview = (Button) dialog.findViewById(R.id.btnReviewAdd);
//            rbReview=(Rating) dialog.findViewById(R.id.rbReview);
//            alertDialogs.setCancelable(false);
            AlertDialog alert = alertDialogs.create();
            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
