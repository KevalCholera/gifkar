package com.smartsense.gifkar;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.bumptech.glide.Glide;
import com.mpt.storage.SharedPreferenceUtil;
import com.smartsense.gifkar.utill.CommonUtil;
import com.smartsense.gifkar.utill.Constants;
import com.smartsense.gifkar.utill.DataRequest;
import com.smartsense.gifkar.utill.JsonErrorShow;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class ShopDetailFragment extends Fragment implements View.OnClickListener, Response.Listener<JSONObject>,
        Response.ErrorListener {


    private TextView tvShopDetailEmail;
    private NetworkImageView ivShopTopElementIMG;
    ImageView ivShopDetailMap;
    private TextView tvShopDetailMNo;
    private TextView tvShopDetailAddress;
    private TextView tvShopDetailDays;
    private TextView tvShopDetailOpenTime;
    private TextView tvShopDetailCloseTIme;
    ImageLoader imageLoader = GifkarApp.getInstance().getDiskImageLoader();
    private TextView tvShopTopElementRatting;
    private TextView tvShopTopElementReview;
    LinearLayout llShopDetailDays;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.fragment_shop_detail, container, false);
        View shopTop = (View) getActivity().findViewById(R.id.shopTop);
        tvShopTopElementRatting = (TextView) shopTop.findViewById(R.id.tvShopTopElementRatting);
        tvShopTopElementReview = (TextView) shopTop.findViewById(R.id.tvShopTopElementReview);
        tvShopDetailEmail = (TextView) view.findViewById(R.id.tvShopDetailEmail);
        tvShopDetailMNo = (TextView) view.findViewById(R.id.tvShopDetailMNo);
        tvShopDetailAddress = (TextView) view.findViewById(R.id.tvShopDetailAddress);
//        tvShopDetailDays = (TextView) view.findViewById(R.id.tvShopDetailDays);
        tvShopDetailOpenTime = (TextView) view.findViewById(R.id.tvShopDetailOpenTime);
        tvShopDetailCloseTIme = (TextView) view.findViewById(R.id.tvShopDetailCloseTIme);
        ivShopDetailMap = (ImageView) view.findViewById(R.id.ivShopDetailMap);
        ivShopDetailMap.setOnClickListener(this);
        ivShopTopElementIMG = (NetworkImageView) shopTop.findViewById(R.id.ivShopTopElementIMG);
        llShopDetailDays = (LinearLayout) view.findViewById(R.id.llShopDetailDays);
        getShopDetail();
        return view;
    }

    public void setData(JSONObject response) {
        tvShopDetailEmail.setText(response.optString("email"));
        tvShopDetailMNo.setText(response.optString("primary_contact"));
        tvShopDetailAddress.setText(response.optString("address"));
        tvShopTopElementRatting.setText(response.optString("ratings"));
        tvShopTopElementReview.setText(response.optString("reviews"));
        ivShopTopElementIMG.setImageUrl(Constants.BASE_URL + "/images/shops/" + response.optString("image"), imageLoader);
        String url = "https://maps.googleapis.com/maps/api/staticmap?size=300x300&markers=color:blue|" + response.optString("latitude") + "," + response.optString("longitude") + "&key=AIzaSyC6skw69zy87ANbkWl_Rq05_LYxkji_4fg";
        tvShopDetailEmail.setTag(response.optString("latitude") + "," + response.optString("longitude"));
        Glide.with(ShopDetailFragment.this).load(url).centerCrop().into(ivShopDetailMap);
        String day = "";
        String open = "";
        String close = "";
        llShopDetailDays.removeAllViews();
        for (int i = 0; i < response.optJSONArray("timings").length(); i++) {
//            final TextView days = new TextView(getActivity());
//            days.setText(response.optJSONArray("timings").optJSONObject(i).optString("day").charAt(0)+"\n"+response.optJSONArray("timings").optJSONObject(i).optString("opens_at")+"\n"+response.optJSONArray("timings").optJSONObject(i).optString("closes_at"));


            View view = LayoutInflater.from(getActivity()).inflate(R.layout.element_date, null);
            day = String.valueOf(response.optJSONArray("timings").optJSONObject(i).optString("day").charAt(0));
            open = String.valueOf(response.optJSONArray("timings").optJSONObject(i).optString("opens_at").charAt(0));
            close = String.valueOf(response.optJSONArray("timings").optJSONObject(i).optString("closes_at").charAt(0));
            TextView days = (TextView) view.findViewById(R.id.tvDay);
            days.setText(day.toUpperCase());
            TextView tvOpen = (TextView) view.findViewById(R.id.tvOpen);
            tvOpen.setText(open);
            TextView tvClose = (TextView) view.findViewById(R.id.tvClose);
            tvClose.setText(close);
            llShopDetailDays.addView(view);

        }
//        tvShopDetailDays.setText(day.toUpperCase());
//        tvShopDetailOpenTime.setText(open);
//        tvShopDetailCloseTIme.setText(close);
//        ivShopDetailMap.setImageUrl(url, imageLoader);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivShopDetailMap:
                StringTokenizer st = new StringTokenizer((String) tvShopDetailEmail.getTag(), ",");
                String uri = "google.navigation:q=" + st.nextToken() + "," + st.nextToken();
                Intent mapsIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                mapsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(mapsIntent);
                break;
            default:
        }
    }

    public void getShopDetail() {
        final String tag = "getReview";
        String url = Constants.BASE_URL + "/mobile/shop/get?defaultToken=" + Constants.DEFAULT_TOKEN + "&shopId=" + SharedPreferenceUtil.getString(Constants.PrefKeys.SHOP_ID, "") + "&userToken=" + SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, "") + "&eventId=" + String.valueOf(Constants.Events.EVENT_SHOPLIST);
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
                        case Constants.Events.EVENT_SHOPLIST:
                            setData(response.optJSONObject("data").optJSONObject("shopDetails"));
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
