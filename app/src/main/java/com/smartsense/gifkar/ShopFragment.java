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

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mpt.storage.SharedPreferenceUtil;
import com.smartsense.gifkar.adapter.ShopListAdapter;
import com.smartsense.gifkar.utill.CommonUtil;
import com.smartsense.gifkar.utill.Constants;
import com.smartsense.gifkar.utill.DataBaseHelper;
import com.smartsense.gifkar.utill.RecyclerItemClickListener;
import com.smartsense.gifkar.utill.SimpleDividerItemDecoration;

import java.util.StringTokenizer;

public class ShopFragment extends Fragment {

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

                        String str = (String) view.getTag();
//                        String[] separated = str.split(" ");
                        StringTokenizer st = new StringTokenizer(str, " ");
                        Log.i("Shop", st.toString());
                        SharedPreferenceUtil.putValue(Constants.PrefKeys.SHOP_ID, st.nextToken());
                        SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_CATEGORY_ID, st.nextToken());
                        SharedPreferenceUtil.putValue(Constants.PrefKeys.SHOP_NAME, st.nextToken());
                        SharedPreferenceUtil.putValue(Constants.PrefKeys.SHOP_IMAGE, st.nextToken());
                        SharedPreferenceUtil.save();
                        startActivity(new Intent(getActivity(), ProductListActivity.class));
                    }
                })
        );
        return view;
    }

    public void fillShopList() {
        try {
//            jsonArray = new JSONArray(getArguments().getString("ID"));
            final Cursor cursor;
            if (SharedPreferenceUtil.getBoolean(Constants.PrefKeys.FILTER_SHOP_NAME, false) | SharedPreferenceUtil.getBoolean(Constants.PrefKeys.FILTER_SHOP_RATTING, false) | SharedPreferenceUtil.getBoolean(Constants.PrefKeys.FILTER_SHOP_MIN, false)) {
                cursor = commonUtil.rawQuery(dbHelper, "SELECT * FROM " + DataBaseHelper.TABLE_SHOP + "  WHERE " + DataBaseHelper.COLUMN_CATEGORY_ID + " = '"
                        + getArguments().getString("ID") + "' ORDER BY " + SharedPreferenceUtil.getString(Constants.PrefKeys.SHOP_FILTER,"") + " COLLATE NOCASE");
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

}
