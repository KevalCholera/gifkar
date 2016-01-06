package com.smartsense.gifkar;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mpt.storage.SharedPreferenceUtil;
import com.smartsense.gifkar.utill.CommonUtil;
import com.smartsense.gifkar.utill.Constants;
import com.smartsense.gifkar.utill.DataRequest;
import com.smartsense.gifkar.utill.JsonErrorShow;

import org.json.JSONException;
import org.json.JSONObject;

public class MyOrdersActivity extends Fragment implements Response.Listener<JSONObject>,
        Response.ErrorListener {
    private ImageView btBack;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.activity_my_orders, container, false);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar_gifkar);
        TextView actionBarTitle = (TextView) toolbar.findViewById(R.id.actionBarHomeTitle);
        actionBarTitle.setText(getResources().getString(R.string.screen_order));
        ImageView btFilter = (ImageView) toolbar.findViewById(R.id.btActionBarfilter);
        btFilter.setVisibility(View.INVISIBLE);
        ImageView btSearch = (ImageView) toolbar.findViewById(R.id.btActionBarSearch);
        btSearch.setVisibility(View.INVISIBLE);
        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("ACTIVE ORDERS"));
        tabLayout.addTab(tabLayout.newTab().setText("PAST ORDERS"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        // Get the ViewPager and set its PagerAdapter so that it can display items
        viewPager = (ViewPager) view.findViewById(R.id.pager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setOffscreenPageLimit(2);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        getOrder();
        return view;
    }


    public class MyOrderPagerAdapter extends FragmentStatePagerAdapter {

        final int TAB_COUNT = 2;
        private Context context;
        private JSONObject response;
        private String[] tabtitles = new String[]{"ACTIVE ORDERS", "PAST ORDERS"};

        public MyOrderPagerAdapter(FragmentManager fm, Context context, JSONObject response) {
            super(fm);
            this.context = context;
            this.response = response;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return ActiveOrdersFragment.newInstance(response.optJSONArray("active").toString());
                case 1:
                    return PastOrdersFragment.newInstance(response.optJSONArray("past").toString());
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return TAB_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            return tabtitles[position];
        }
    }

    public void getOrder() {
        final String tag = "getOrder";
        String url = Constants.BASE_URL + "/mobile/orderDetail/get?defaultToken=" + Constants.DEFAULT_TOKEN + "&order=all&userToken=" + SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, "") + "&eventId=" + String.valueOf(Constants.Events.EVENT_ORDER_HISTORY);
        CommonUtil.showProgressDialog(getActivity(), "Wait...");
        DataRequest loginRequest = new DataRequest(Request.Method.GET, url, null, this, this);
        loginRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        GifkarApp.getInstance().addToRequestQueue(loginRequest, tag);
    }


    @Override
    public void onErrorResponse(VolleyError volleyError) {
        CommonUtil.alertBox(getActivity(), "", getResources().getString(R.string.nointernet_try_again_msg));
        CommonUtil.cancelProgressDialog();
    }


    private void setupViewPager(JSONObject response) {
        try {
            Log.d("hm","no");
            MyOrderPagerAdapter myOrderPagerAdapter = new MyOrderPagerAdapter(getFragmentManager(), getActivity(), response.optJSONObject("data").optJSONObject("orderDetails"));
            Log.d("hm","yes");
            viewPager.setAdapter(myOrderPagerAdapter);
            tabLayout.setupWithViewPager(viewPager);
            CommonUtil.cancelProgressDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResponse(JSONObject response) {
//        CommonUtil.cancelProgressDialog();
        if (response != null) {
            try {
                if (response.getInt("status") == Constants.STATUS_SUCCESS) {
                    switch (Integer.valueOf(response.getString("eventId"))) {
                        case Constants.Events.EVENT_ORDER_HISTORY:
                            Log.d("hm","yes");
                            setupViewPager(response);
                            Log.d("hm", "no");
                            break;
                    }
                } else {
                    CommonUtil.cancelProgressDialog();
                    JsonErrorShow.jsonErrorShow(response, getActivity());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
