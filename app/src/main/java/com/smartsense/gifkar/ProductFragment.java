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

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smartsense.gifkar.adapter.ShopListAdapter;
import com.smartsense.gifkar.utill.CommonUtil;
import com.smartsense.gifkar.utill.DataBaseHelper;
import com.smartsense.gifkar.utill.SimpleDividerItemDecoration;

public class ProductFragment extends Fragment {

//    static JSONArray jsonArray;
    static RecyclerView rv;
    DataBaseHelper dbHelper = new DataBaseHelper(getActivity());
    CommonUtil commonUtil = new CommonUtil();

    public static ProductFragment newInstance(String ID) {
        ProductFragment fragmentFirst = new ProductFragment();
        Bundle args = new Bundle();
        args.putString("ID", ID);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rv = (RecyclerView) inflater.inflate(R.layout.recycle_view, container, false);
        rv.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        try {
//            jsonArray = new JSONArray(getArguments().getString("ID"));
            Cursor cursor = commonUtil.rawQuery(dbHelper, "SELECT * FROM "+ DataBaseHelper.TABLE_SHOP+"  WHERE "+DataBaseHelper.COLUMN_CATEGORY_ID+" = '"
                    + getArguments().getString("ID")+"'");
            rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));
            if (cursor.getCount() > 0) {
                rv.setAdapter(new ShopListAdapter(getActivity(), cursor));
            }
//            rv.setAdapter(new ProductGridAdapter(getActivity(), jsonArray, true));
        } catch (Exception e) {
            e.printStackTrace();
        }return rv;
    }


}
