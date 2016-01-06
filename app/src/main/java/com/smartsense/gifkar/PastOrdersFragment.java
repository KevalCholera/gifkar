package com.smartsense.gifkar;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import org.json.JSONObject;

public class PastOrdersFragment extends Fragment implements View.OnClickListener {
    Button btnStartGift;
    private ListView lvPastOrders;
    private LinearLayout ll_past_order;

    public static PastOrdersFragment newInstance(String orderDetail) {
        PastOrdersFragment fragmentFirst = new PastOrdersFragment();
        Bundle args = new Bundle();
        args.putString("orderDetail", orderDetail);
        Log.i("orderDetail", orderDetail);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.fragment_past_order, container, false);
        lvPastOrders = (ListView) view.findViewById(R.id.lvPastOrders);
        ll_past_order = (LinearLayout) view.findViewById(R.id.ll_past_order);
        btnStartGift = (Button) view.findViewById(R.id.btnPastStartGifiting);
        btnStartGift.setOnClickListener(this);
        orderFill();
        lvPastOrders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(final AdapterView<?> adapterView, View view, final int position, long index) {
                JSONObject getCodeObj = (JSONObject) adapterView.getItemAtPosition(position);
                startActivity(new Intent(getActivity(), OrderDetailActivity.class).putExtra("id", getCodeObj.optString("orderDetailId")));
            }
        });
        return view;
    }

    public void orderFill() {
        MyOrderAdapter myOrderAdapter = null;
        try {
            JSONArray arrActive = new JSONArray(getArguments().getString("orderDetail"));
            if (arrActive.length() > 0) {
                lvPastOrders.setVisibility(View.VISIBLE);
                ll_past_order.setVisibility(View.GONE);
                myOrderAdapter = new MyOrderAdapter(getActivity(), arrActive, true);
                lvPastOrders.setAdapter(myOrderAdapter);
            } else {
                lvPastOrders.setVisibility(View.GONE);
                ll_past_order.setVisibility(View.VISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

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
