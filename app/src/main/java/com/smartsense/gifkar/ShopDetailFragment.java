package com.smartsense.gifkar;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ShopDetailFragment extends Fragment implements View.OnClickListener {
    




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.fragment_shop_detail, container, false);

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
