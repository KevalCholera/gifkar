package com.smartsense.gifkar;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

public class ActiveOrdersFragment extends Fragment implements View.OnClickListener {
    
    Button btnStartGift;
    private ListView lvLiveOrders;
    private LinearLayout ll_active_order;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.fragment_active_order, container, false);
        lvLiveOrders = (ListView) view.findViewById(R.id.lvLiveOrders);
        ll_active_order = (LinearLayout) view.findViewById(R.id.ll_active_order);
        btnStartGift=(Button) view.findViewById(R.id.btnActiveStartGifiting);
        btnStartGift.setOnClickListener(this);
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
