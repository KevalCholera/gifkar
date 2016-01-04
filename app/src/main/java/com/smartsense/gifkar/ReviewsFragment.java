package com.smartsense.gifkar;


import android.app.AlertDialog;
import android.content.Context;
import android.media.Rating;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mpt.storage.SharedPreferenceUtil;
import com.smartsense.gifkar.adapter.ReviewAdapter;
import com.smartsense.gifkar.utill.CircleImageView;
import com.smartsense.gifkar.utill.CommonUtil;
import com.smartsense.gifkar.utill.Constants;
import com.smartsense.gifkar.utill.DataRequest;
import com.smartsense.gifkar.utill.JsonErrorShow;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ReviewsFragment extends Fragment implements View.OnClickListener, Response.Listener<JSONObject>,
        Response.ErrorListener {
    private CircleImageView ivReviewUser;
    private TextView tvUserName;
    private EditText etReviewAdd;
    private Button btAddReview;
    private Rating rbReview;
    private TextView llShopReview;
    LinearLayout llReview, llNoReview;
    ListView lvReview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.fragment_reviews, container, false);
        llShopReview = (TextView) view.findViewById(R.id.tvReview);
        llShopReview.setOnClickListener(this);
        llReview = (LinearLayout) view.findViewById(R.id.llReview);
        llNoReview = (LinearLayout) view.findViewById(R.id.llNoReview);
        lvReview = (ListView) view.findViewById(R.id.lvReview);
        btAddReview = (Button) view.findViewById(R.id.btnAddReview);
        getReview();
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvReview:
            case R.id.btnAddReview:
                openAddReviewPopup();
                break;
            default:
        }
    }

    public void reviewFill(JSONObject reviews) {
        ReviewAdapter reviewAdapter = null;
        try {
            if (reviews.getJSONObject("data").getJSONArray("shopReviews").length() > 0) {
                llReview.setVisibility(View.VISIBLE);
                llNoReview.setVisibility(View.GONE);
                reviewAdapter = new ReviewAdapter(getActivity(), reviews.getJSONObject("data").getJSONArray("shopReviews"), true);
                lvReview.setAdapter(reviewAdapter);
            } else {
                llReview.setVisibility(View.GONE);
                llNoReview.setVisibility(View.VISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
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


    public void getReview() {
        final String tag = "getReview";
        String url = Constants.BASE_URL + "/mobile/shopReview/get?defaultToken=" + Constants.DEFAULT_TOKEN + "&shopId=" + SharedPreferenceUtil.getString(Constants.PrefKeys.SHOP_ID, "") + "&userToken=" + SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, "") + "&eventId=" + String.valueOf(Constants.Events.EVENT_GET_REVIEW);
        CommonUtil.showProgressDialog(getActivity(), "Wait...");
        DataRequest loginRequest = new DataRequest(Request.Method.GET, url, null, this, this);
        loginRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        GifkarApp.getInstance().addToRequestQueue(loginRequest, tag);
    }


    public void deleteAddress(String id) {
        final String tag = "deladdress";
        String url = Constants.BASE_URL + "/mobile/orderDetail/delete";
        Map<String, String> params = new HashMap<String, String>();
        params.put("addressId", id);
        params.put("userToken", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, ""));
        params.put("eventId", String.valueOf(Constants.Events.EVENT_DEL_ADDRESS));
        params.put("defaultToken", Constants.DEFAULT_TOKEN);
        Log.i("params", params.toString());
        CommonUtil.showProgressDialog(getActivity(), "Wait...");
        DataRequest loginRequest = new DataRequest(Request.Method.POST, url, params, this, this);
        loginRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        GifkarApp.getInstance().addToRequestQueue(loginRequest, tag);
    }


    @Override
    public void onErrorResponse(VolleyError volleyError) {
        CommonUtil.alertBox(getActivity(), "", getResources().getString(R.string.nointernet_try_again_msg));
        CommonUtil.cancelProgressDialog();
    }

    @Override
    public void onResponse(JSONObject response) {
        CommonUtil.cancelProgressDialog();
        if (response != null) {
            try {
                if (response.getInt("status") == Constants.STATUS_SUCCESS) {
                    switch (Integer.valueOf(response.getString("eventId"))) {
                        case Constants.Events.EVENT_GET_REVIEW:
                            reviewFill(response);
                            break;
                        case Constants.Events.EVENT_DEL_ADDRESS:

                            break;
                    }
                } else {
                    JsonErrorShow.jsonErrorShow(response, getActivity());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
