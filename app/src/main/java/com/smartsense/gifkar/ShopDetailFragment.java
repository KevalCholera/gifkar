package com.smartsense.gifkar;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.android.volley.toolbox.NetworkImageView;

public class ShopDetailFragment extends Fragment implements View.OnClickListener {


    private EditText tvShopDetailEmail;
    private NetworkImageView ivShopDetailMap;
    private EditText tvShopDetailMNo;
    private EditText tvShopDetailAddress;
    private EditText tvShopDetailDays;
    private EditText tvShopDetailOpenTime;
    private EditText tvShopDetailCloseTIme;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.fragment_shop_detail, container, false);
        tvShopDetailEmail=(EditText) view.findViewById(R.id.tvShopDetailEmail);
        tvShopDetailMNo=(EditText) view.findViewById(R.id.tvShopDetailMNo);
        tvShopDetailAddress=(EditText) view.findViewById(R.id.tvShopDetailAddress);
        tvShopDetailDays=(EditText) view.findViewById(R.id.tvShopDetailDays);
        tvShopDetailOpenTime=(EditText) view.findViewById(R.id.tvShopDetailOpenTime);
        tvShopDetailCloseTIme=(EditText) view.findViewById(R.id.tvShopDetailCloseTIme);
        ivShopDetailMap=(NetworkImageView) view.findViewById(R.id.ivShopDetailMap);
        return  view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnActiveStartGifiting:

                break;
            default:
        }
    }
}
