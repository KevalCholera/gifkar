/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.smartsense.gifkar;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mpt.storage.SharedPreferenceUtil;
import com.smartsense.gifkar.adapter.ShopListAdapter;
import com.smartsense.gifkar.utill.CommonUtil;
import com.smartsense.gifkar.utill.Constants;
import com.smartsense.gifkar.utill.DataBaseHelper;
import com.smartsense.gifkar.utill.DataRequest;
import com.smartsense.gifkar.utill.JsonErrorShow;
import com.smartsense.gifkar.utill.RecyclerItemClickListener;
import com.smartsense.gifkar.utill.SimpleDividerItemDecoration;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.StringTokenizer;

public class ShopFragment extends Fragment implements Response.Listener<JSONObject>,
        Response.ErrorListener {

    //    static JSONArray jsonArray;
    RecyclerView recyclerView;
    DataBaseHelper dbHelper = new DataBaseHelper(getActivity());
    CommonUtil commonUtil = new CommonUtil();
    private LinearLayout llListEmpty;
    private TextView tvListEmpty;
    Fragment fragment = this;
//    private String s1 = "";

    public static ShopFragment newInstance(String ID, String name) {
        ShopFragment fragmentFirst = new ShopFragment();
        Bundle args = new Bundle();
        args.putString("ID", ID);
        args.putString("name", name);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.fragment_shop, container, false);
//        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar_gifkar);
//        ImageView btFilter = (ImageView) toolbar.findViewById(R.id.btActionBarfilter);
//        btFilter.setVisibility(View.VISIBLE);
//        btFilter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                fragment.startActivityForResult(new Intent(getActivity(), ShopFilterActivity.class), 2);
//            }
//        });
        recyclerView = (RecyclerView) view.findViewById(R.id.recycle_view);
        llListEmpty = (LinearLayout) view.findViewById(R.id.llShopListEmapty);
        tvListEmpty = (TextView) view.findViewById(R.id.tvShopListEmpty);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        fillShopList();
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        final String str = (String) view.getTag();
                        String[] separated = str.split("_");
                        if (CommonUtil.checkCartCount() != 0 & !SharedPreferenceUtil.getString(Constants.PrefKeys.SHOP_ID, "").equalsIgnoreCase(separated[0])) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                            alert.setTitle("Empty Cart?");
                            alert.setMessage("Do you wish to discard your current Cart?");
                            alert.setPositiveButton("DISCARD", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    //Do something here where "ok" clicked
                                    SharedPreferenceUtil.remove(Constants.PrefKeys.PREF_PROD_LIST);
                                    openShop(str);
                                }
                            });
                            alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    //Do something here where "Cancel" clicked
                                    dialog.cancel();
                                }
                            });
                            alert.show();
                        } else {
                            openShop(str);
                        }

                    }
                })
        );
        return view;
    }

    public void openShop(String str) {
        StringTokenizer st = new StringTokenizer(str, "_");
        Log.i("Shop", st.toString());
        SharedPreferenceUtil.putValue(Constants.PrefKeys.SHOP_ID, st.nextToken());
        SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_CATEGORY_ID, st.nextToken());
        SharedPreferenceUtil.putValue(Constants.PrefKeys.SHOP_IMAGE, st.nextToken());
        SharedPreferenceUtil.putValue(Constants.PrefKeys.SHOP_NAME, st.nextToken());
        SharedPreferenceUtil.putValue(Constants.PrefKeys.MIN_ORDER, st.nextToken());
        SharedPreferenceUtil.putValue(Constants.PrefKeys.DELIVERY_CHARGES, st.nextToken());
        SharedPreferenceUtil.save();
        getShopStatus();
//        startActivity(new Intent(getActivity(), ProductListActivity.class));
    }

    public void fillShopList() {
        try {
//            jsonArray = new JSONArray(getArguments().getString("ID"));
            final Cursor cursor;
            if (SharedPreferenceUtil.getBoolean(Constants.PrefKeys.FILTER_SHOP_NAME, false) | SharedPreferenceUtil.getBoolean(Constants.PrefKeys.FILTER_SHOP_RATTING, false) | SharedPreferenceUtil.getBoolean(Constants.PrefKeys.FILTER_SHOP_MIN, false)) {
                cursor = commonUtil.rawQuery(dbHelper, "SELECT * FROM " + DataBaseHelper.TABLE_SHOP + "  WHERE " + DataBaseHelper.COLUMN_CATEGORY_ID + " = '"
                        + getArguments().getString("ID") + "' ORDER BY " + SharedPreferenceUtil.getString(Constants.PrefKeys.SHOP_FILTER, "") + " COLLATE NOCASE");
            } else {
                cursor = commonUtil.rawQuery(dbHelper, "SELECT * FROM " + DataBaseHelper.TABLE_SHOP + "  WHERE " + DataBaseHelper.COLUMN_CATEGORY_ID + " = '"
                        + getArguments().getString("ID") + "' ");
            }
            if (cursor.getCount() > 0) {
                recyclerView.setAdapter(new ShopListAdapter(getActivity(), cursor));
                llListEmpty.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            } else {
                tvListEmpty.setText("Currently " + getArguments().getString("name").trim() + " shop not available.");
                llListEmpty.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
//            s1 = data.getStringExtra("orderBy");
//            if (!s1.equalsIgnoreCase("")) {
            fillShopList();
//            }
        }
    }

    public void getShopStatus() {
        CommonUtil.showProgressDialog(getActivity(),"Please wait");
        final String tag = "EVENT_Shop_Status";
        String url = Constants.BASE_URL + "/mobile/shop/checkOpenStatus?defaultToken=" + Constants.DEFAULT_TOKEN + "&shopId=" + SharedPreferenceUtil.getString(Constants.PrefKeys.SHOP_ID, "") + "&eventId=" + String.valueOf(Constants.Events.EVENT_SHOP_STATUS);
        DataRequest loginRequest = new DataRequest(Request.Method.GET, url, null, this, this);
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
                        case Constants.Events.EVENT_SHOP_STATUS:
                            if (response.optJSONObject("data").optInt("shopStatus") == 0) {
                                android.app.AlertDialog.Builder alertbox = new android.app.AlertDialog.Builder(getActivity());
                                alertbox.setCancelable(true);
                                alertbox.setMessage("Sorry this shop is closed for accepting new orders Today. Please check Delivery Timings and Cut of Time.\n" +
                                        "Do you want to place order for tomorrow or day after?");
                                alertbox.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        startActivity(new Intent(getActivity(), ProductListActivity.class));

                                    }

                                });
                                alertbox.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {

                                    }
                                });
                                alertbox.show();
                            } else {
                                startActivity(new Intent(getActivity(), ProductListActivity.class));
                            }
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
