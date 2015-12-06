package com.smartsense.gifkar;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

public class PastOrdersFragment extends Fragment implements View.OnClickListener {
    Button btnStartGift;
    private ListView lvPastOrders;
    private LinearLayout ll_past_order;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.fragment_past_order, container, false);
        lvPastOrders = (ListView) view.findViewById(R.id.lvPastOrders);
        ll_past_order = (LinearLayout) view.findViewById(R.id.ll_past_order);
        btnStartGift=(Button) view.findViewById(R.id.btnPastStartGifiting);
        btnStartGift.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnPastStartGifiting:

                break;
            default:
        }
    }
}
