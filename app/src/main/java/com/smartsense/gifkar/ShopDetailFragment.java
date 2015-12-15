package com.smartsense.gifkar;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

public class ShopDetailFragment extends Fragment implements View.OnClickListener {


    private TextView tvShopDetailEmail;
    private NetworkImageView ivShopDetailMap;
    private TextView tvShopDetailMNo;
    private TextView tvShopDetailAddress;
    private TextView tvShopDetailDays;
    private TextView tvShopDetailOpenTime;
    private TextView tvShopDetailCloseTIme;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.fragment_shop_detail, container, false);
        tvShopDetailEmail=(TextView) view.findViewById(R.id.tvShopDetailEmail);
        tvShopDetailMNo=(TextView) view.findViewById(R.id.tvShopDetailMNo);
        tvShopDetailAddress=(TextView) view.findViewById(R.id.tvShopDetailAddress);
        tvShopDetailDays=(TextView) view.findViewById(R.id.tvShopDetailDays);
        tvShopDetailOpenTime=(TextView) view.findViewById(R.id.tvShopDetailOpenTime);
        tvShopDetailCloseTIme=(TextView) view.findViewById(R.id.tvShopDetailCloseTIme);
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
