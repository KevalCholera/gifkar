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
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mpt.storage.SharedPreferenceUtil;
import com.smartsense.gifkar.adapter.ProductAdapter;
import com.smartsense.gifkar.utill.CommonUtil;
import com.smartsense.gifkar.utill.Constants;
import com.smartsense.gifkar.utill.DataBaseHelper;

public class ProductFragment extends Fragment {
    ListView lvProduct;
    LinearLayout llProdListEmpty;
    TextView tvProdListEmpty;
    DataBaseHelper dbHelper = new DataBaseHelper(getActivity());
    CommonUtil commonUtil = new CommonUtil();
    private Fragment fragment = this;
    public static Boolean reloadList = false;
    public static Boolean reloadExit = false;

    public static ProductFragment newInstance(String ID, String categoryName) {
        ProductFragment fragmentFirst = new ProductFragment();
        Bundle args = new Bundle();
        args.putString("ID", ID);
        args.putString("name", categoryName);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.fragment_product, container, false);
        View v = ((AppCompatActivity) getActivity()).getSupportActionBar().getCustomView();
        ImageView btFilter = (ImageView) v.findViewById(R.id.btActionBarfilter);
        btFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment.startActivityForResult(new Intent(getActivity(), ProductFilterActivity.class), 1);
            }
        });
        llProdListEmpty = (LinearLayout) view.findViewById(R.id.llProdListEmapty);
        tvProdListEmpty = (TextView) view.findViewById(R.id.tvProdListEmpty);
        lvProduct = (ListView) view.findViewById(R.id.lvProductList);
//        lvProduct.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getActivity(), ProductDetailActivity.class).putExtra("ProdDEID", (Integer) view.getTag()));
//            }
//        });
//        lvProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(final AdapterView<?> adapterView, View view, final int position, long index) {
////                JSONObject getCodeObj = (JSONObject) adapterView.getItemAtPosition(position);
//                getActivity().startActivity(new Intent(getActivity(), ProductDetailActivity.class).putExtra("ProdDEID", (Integer) view.getTag()));
//
//            }
//        });
        fillProdList();
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
//            s1 = data.getStringExtra("orderBy");
//            if (!s1.equalsIgnoreCase("")) {
            fillProdList();
//            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (reloadList) {
            reloadList = false;
            if (reloadExit) {
                reloadExit = false;
                getActivity().finish();
            }
            fillProdList();
        }
    }

    private void fillProdList() {
        try {
            final Cursor cursor;
            if (SharedPreferenceUtil.getBoolean(Constants.PrefKeys.FILTER_PROD_NAME, false) | SharedPreferenceUtil.getBoolean(Constants.PrefKeys.FILTER_PROD_PRICE, false)) {
                cursor = commonUtil.rawQuery(dbHelper, "SELECT * FROM " + DataBaseHelper.TABLE_PRODUCT + "  WHERE " + DataBaseHelper.COLUMN_PROD_CATEGORY_ID + " = '"
                        + getArguments().getString("ID") + "' ORDER BY " + SharedPreferenceUtil.getString(Constants.PrefKeys.PROD_FILTER, "") + " COLLATE NOCASE");
            } else {
                cursor = commonUtil.rawQuery(dbHelper, "SELECT * FROM " + DataBaseHelper.TABLE_PRODUCT + "  WHERE " + DataBaseHelper.COLUMN_PROD_CATEGORY_ID + " = '"
                        + getArguments().getString("ID") + "'");
            }
            if (cursor.getCount() > 0) {
                lvProduct.setAdapter(new ProductAdapter(getActivity(), cursor, dbHelper, true));
                llProdListEmpty.setVisibility(View.GONE);
                lvProduct.setVisibility(View.VISIBLE);
            } else {
                tvProdListEmpty.setText("Currently " + getArguments().getString("name") + " category product not available.");
                llProdListEmpty.setVisibility(View.VISIBLE);
                lvProduct.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
