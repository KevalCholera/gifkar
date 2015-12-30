package com.smartsense.gifkar;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.smartsense.gifkar.adapter.MyOrderAdapter;

import org.json.JSONArray;
import org.json.JSONException;

public class ActiveOrdersFragment extends Fragment implements View.OnClickListener {

    Button btnStartGift;
    private ListView lvLiveOrders;
    private LinearLayout ll_active_order;

    public static ActiveOrdersFragment newInstance(String orderDetail) {
        ActiveOrdersFragment fragmentFirst = new ActiveOrdersFragment();
        Bundle args = new Bundle();
        args.putString("orderDetail", orderDetail);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.fragment_active_order, container, false);
        lvLiveOrders = (ListView) view.findViewById(R.id.lvLiveOrders);
        ll_active_order = (LinearLayout) view.findViewById(R.id.ll_active_order);
        btnStartGift = (Button) view.findViewById(R.id.btnActiveStartGifiting);
        btnStartGift.setOnClickListener(this);
        orderFill();
        lvLiveOrders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(final AdapterView<?> adapterView, View view, final int position, long index) {
//                JSONObject getCodeObj = (JSONObject) adapterView.getItemAtPosition(position);
                startActivity(new Intent(getActivity(), OrderDetailActivity.class).putExtra("id", (String) view.getTag()));
            }
        });
        return view;
    }

    public void orderFill() {
        MyOrderAdapter myOrderAdapter = null;
        try {
            JSONArray arrActive = new JSONArray(getArguments().getString("orderDetail"));
            if (arrActive.length() > 0) {
                lvLiveOrders.setVisibility(View.VISIBLE);
                ll_active_order.setVisibility(View.GONE);
                myOrderAdapter = new MyOrderAdapter(getActivity(), arrActive, true);
                lvLiveOrders.setAdapter(myOrderAdapter);
            } else {
                lvLiveOrders.setVisibility(View.GONE);
                ll_active_order.setVisibility(View.VISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

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
