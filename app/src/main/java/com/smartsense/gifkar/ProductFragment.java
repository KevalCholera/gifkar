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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.smartsense.gifkar.adapter.ProductAdapter;
import com.smartsense.gifkar.utill.CommonUtil;
import com.smartsense.gifkar.utill.DataBaseHelper;

public class ProductFragment extends Fragment {
    ListView lvProduct;
    LinearLayout llProdListEmpty;
    TextView tvProdListEmpty;
    DataBaseHelper dbHelper = new DataBaseHelper(getActivity());
    CommonUtil commonUtil = new CommonUtil();

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
        lvProduct = (ListView) view.findViewById(R.id.lvProductList);
        llProdListEmpty=(LinearLayout) view.findViewById(R.id.llProdListEmapty);
        tvProdListEmpty=(TextView) view.findViewById(R.id.tvProdListEmpty);
        try {
            Cursor cursor = commonUtil.rawQuery(dbHelper, "SELECT * FROM " + DataBaseHelper.TABLE_PRODUCT + "  WHERE " + DataBaseHelper.COLUMN_PROD_CATEGORY_ID + " = '"
                    + getArguments().getString("ID") + "'");
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
        lvProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(final AdapterView<?> adapterView, View view, final int position, long index) {
//                JSONObject getCodeObj = (JSONObject) adapterView.getItemAtPosition(position);
                startActivity(new Intent(getActivity(), ProductDetailActivity.class).putExtra("ProdDEID", (Integer) view.getTag()));

            }
        });
        return view;
    }


}
