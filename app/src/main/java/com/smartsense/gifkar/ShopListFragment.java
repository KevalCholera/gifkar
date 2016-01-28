package com.smartsense.gifkar;


import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mpt.storage.SharedPreferenceUtil;
import com.smartsense.gifkar.adapter.NewFeatureAdapter;
import com.smartsense.gifkar.utill.CommonUtil;
import com.smartsense.gifkar.utill.Constants;
import com.smartsense.gifkar.utill.DataBaseHelper;
import com.smartsense.gifkar.utill.DataRequest;
import com.smartsense.gifkar.utill.JsonErrorShow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShopListFragment extends Fragment implements ViewPager.OnPageChangeListener, Response.Listener<JSONObject>,
        Response.ErrorListener {
    ViewPager viewPager;
    TabLayout tabLayout;
    private Handler handler;
    View newFeaturesView;
    TextView tvGreetText;
    private Fragment fragment=this;

    public ShopListFragment() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_shoplist, container, false);
        handler = new Handler();
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar_gifkar);
        TextView actionBarTitle = (TextView) toolbar.findViewById(R.id.actionBarHomeTitle);
        actionBarTitle.setText(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_AREA_NAME, "") + ", " + SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_AREA_PIN_CODE, ""));
        newFeaturesView = (View) rootView.findViewById(R.id.newfeaturesView);
        CommonUtil.disableView(newFeaturesView);
        tvGreetText = (TextView) rootView.findViewById(R.id.tvGreetText);
        ImageView btFilter = (ImageView) toolbar.findViewById(R.id.btActionBarfilter);
        btFilter.setVisibility(View.VISIBLE);
        ImageView btSearch = (ImageView) toolbar.findViewById(R.id.btActionBarSearch);
        btSearch.setVisibility(View.VISIBLE);

        btFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment.startActivityForResult(new Intent(getActivity(), ShopFilterActivity.class), 2);
            }
        });
        btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SearchShopActivity.class).putExtra("id", Adapter.mFragmentID.get(tabLayout.getSelectedTabPosition())));
            }
        });

//        FrameLayout fm = (FrameLayout) rootView.findViewById(R.id.fl_category);
        int height = getResources().getDisplayMetrics().heightPixels;
        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) newFeaturesView.getLayoutParams();
        params.height = (int) (height / 3.5);
        newFeaturesView.setLayoutParams(params);
//        setReference();
        // tablayout and viewpager of category
        viewPager = (ViewPager) rootView.findViewById(R.id.viewpager_main);
        tabLayout = (TabLayout) rootView.findViewById(R.id.tabs_main);
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.tab_idicator));
        tabLayout.setTabTextColors(getResources().getColor(R.color.tab_normal_text), getResources().getColor(R.color.tab_idicator));

        getBanner();
        getBottom();
        getShopList();

        return rootView;
    }

    DataBaseHelper dbHelper;

    private void setupViewPager(JSONArray category) {
        dbHelper = new DataBaseHelper(getActivity());
        Adapter adapter = new Adapter(getChildFragmentManager());
//        String tempary = "{ \"eventId\" : 123, \n" +
//                "\"errorCode\":0,\n" +
//                " \"status\":200,\n" +
//                " \"message\":\"Category List.\", \n" +
//                "\"data\":{\"categories\":[{\"id\":\"1\",\"name\":\"Cakes\",\"shops\":[{\"id\":\"3\",\"name\":\"Raju japan1\",\"image\":\"shopImage1446635108.jpg\",\"cutOffTime\":\"12\",\"minOrder\":\"12\",\"midnightDeliveryStatus\":\"1\",\"sameDayDeliveryStatus\":\"0\",\"rating\":4.25,\"opensAt\":\"00:15:00\",\"closesAt\":\"20:30:00\",\"deliveryFrom\":\"00:00:00\",\"deliveryTo\":\"01:15:00\",\"remoteArea\":true,\"tags\":[{\"name\":\"tag1\",\"pivot\":{\"shop_id\":\"3\",\"tag_id\":\"1\"}},{\"name\":\"tag2\",\"pivot\":{\"shop_id\":\"3\",\"tag_id\":\"2\"}},{\"name\":\"Backery\",\"pivot\":{\"shop_id\":\"3\",\"tag_id\":\"3\"}}]},{\"id\":\"9\",\"name\":\"Shop\",\"image\":\"shopImage1446306375.png\",\"cutOffTime\":\"0\",\"minOrder\":\"12\",\"midnightDeliveryStatus\":\"1\",\"sameDayDeliveryStatus\":\"1\",\"rating\":null,\"opensAt\":null,\"closesAt\":null,\"deliveryFrom\":null,\"deliveryTo\":null,\"remoteArea\":true,\"tags\":[]}]},{\"id\":\"2\",\"name\":\"Sweets\",\"shops\":[{\"id\":\"9\",\"name\":\"Shop\",\"image\":\"shopImage1446306375.png\",\"cutOffTime\":\"0\",\"minOrder\":\"12\",\"midnightDeliveryStatus\":\"1\",\"sameDayDeliveryStatus\":\"1\",\"rating\":null,\"opensAt\":null,\"closesAt\":null,\"deliveryFrom\":null,\"deliveryTo\":null,\"remoteArea\":true,\"tags\":[]}]},{\"id\":\"3\",\"name\":\"Flowers\",\"shops\":[{\"id\":\"3\",\"name\":\"Raju japan1\",\"image\":\"shopImage1446635108.jpg\",\"cutOffTime\":\"12\",\"minOrder\":\"12\",\"midnightDeliveryStatus\":\"1\",\"sameDayDeliveryStatus\":\"0\",\"rating\":4.25,\"opensAt\":\"00:15:00\",\"closesAt\":\"20:30:00\",\"deliveryFrom\":\"00:00:00\",\"deliveryTo\":\"01:15:00\",\"remoteArea\":true,\"tags\":[{\"name\":\"tag1\",\"pivot\":{\"shop_id\":\"3\",\"tag_id\":\"1\"}},{\"name\":\"tag2\",\"pivot\":{\"shop_id\":\"3\",\"tag_id\":\"2\"}},{\"name\":\"Backery\",\"pivot\":{\"shop_id\":\"3\",\"tag_id\":\"3\"}}]},{\"id\":\"9\",\"name\":\"Shop\",\"image\":\"shopImage1446306375.png\",\"cutOffTime\":\"0\",\"minOrder\":\"12\",\"midnightDeliveryStatus\":\"1\",\"sameDayDeliveryStatus\":\"1\",\"rating\":null,\"opensAt\":null,\"closesAt\":null,\"deliveryFrom\":null,\"deliveryTo\":null,\"remoteArea\":true,\"tags\":[]}]},{\"id\":\"4\",\"name\":\"Backery\",\"shops\":[{\"id\":\"9\",\"name\":\"Shop\",\"image\":\"shopImage1446306375.png\",\"cutOffTime\":\"0\",\"minOrder\":\"12\",\"midnightDeliveryStatus\":\"1\",\"sameDayDeliveryStatus\":\"1\",\"rating\":null,\"opensAt\":null,\"closesAt\":null,\"deliveryFrom\":null,\"deliveryTo\":null,\"remoteArea\":true,\"tags\":[]}]},{\"id\":\"5\",\"name\":\"Indian\",\"shops\":[{\"id\":\"3\",\"name\":\"Raju japan1\",\"image\":\"shopImage1446635108.jpg\",\"cutOffTime\":\"12\",\"minOrder\":\"12\",\"midnightDeliveryStatus\":\"1\",\"sameDayDeliveryStatus\":\"0\",\"rating\":4.25,\"opensAt\":\"00:15:00\",\"closesAt\":\"20:30:00\",\"deliveryFrom\":\"00:00:00\",\"deliveryTo\":\"01:15:00\",\"remoteArea\":true,\"tags\":[{\"name\":\"tag1\",\"pivot\":{\"shop_id\":\"3\",\"tag_id\":\"1\"}},{\"name\":\"tag2\",\"pivot\":{\"shop_id\":\"3\",\"tag_id\":\"2\"}},{\"name\":\"Backery\",\"pivot\":{\"shop_id\":\"3\",\"tag_id\":\"3\"}}]},{\"id\":\"9\",\"name\":\"Shop\",\"image\":\"shopImage1446306375.png\",\"cutOffTime\":\"0\",\"minOrder\":\"12\",\"midnightDeliveryStatus\":\"1\",\"sameDayDeliveryStatus\":\"1\",\"rating\":null,\"opensAt\":null,\"closesAt\":null,\"deliveryFrom\":null,\"deliveryTo\":null,\"remoteArea\":true,\"tags\":[]}]},{\"id\":\"6\",\"name\":\"Italian\",\"shops\":[{\"id\":\"3\",\"name\":\"Raju japan1\",\"image\":\"shopImage1446635108.jpg\",\"cutOffTime\":\"12\",\"minOrder\":\"12\",\"midnightDeliveryStatus\":\"1\",\"sameDayDeliveryStatus\":\"0\",\"rating\":4.25,\"opensAt\":\"00:15:00\",\"closesAt\":\"20:30:00\",\"deliveryFrom\":\"00:00:00\",\"deliveryTo\":\"01:15:00\",\"remoteArea\":true,\"tags\":[{\"name\":\"tag1\",\"pivot\":{\"shop_id\":\"3\",\"tag_id\":\"1\"}},{\"name\":\"tag2\",\"pivot\":{\"shop_id\":\"3\",\"tag_id\":\"2\"}},{\"name\":\"Backery\",\"pivot\":{\"shop_id\":\"3\",\"tag_id\":\"3\"}}]}]}]}}\n";
        try {
//            JSONObject response = new JSONObject(tempary);
//            JSONArray category = response.optJSONObject("data").optJSONArray("categories");
            insertItemInCart(adapter, category);
            viewPager.setAdapter(adapter);
            tabLayout.setupWithViewPager(viewPager);
            CommonUtil.cancelProgressDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    CommonUtil commonUtil = new CommonUtil();

    public void insertItemInCart(Adapter adapter, JSONArray category) {
        try {
//            SQLiteDatabase db;
//            db = dbHelper.getWritableDatabase();
//            db.execSQL("DELETE FROM " + DataBaseHelper.TABLE_SHOP);
            commonUtil.execSQL(dbHelper, "DELETE FROM " + DataBaseHelper.TABLE_SHOP);
            Adapter.mFragmentTitles.clear();
            Adapter.mFragmentID.clear();
            tabLayout.removeAllTabs();
//            db.close();
            for (int i = 0; i < category.length(); i++) {
                JSONObject catJson = category.optJSONObject(i);
                if (catJson.has("shops")) {
                    for (int j = 0; j < catJson.optJSONArray("shops").length(); j++) {
                        JSONObject prodJson = catJson.optJSONArray("shops").optJSONObject(j);
                        String str = "";
                        if (prodJson.optJSONArray("tags").length() > 0) {
                            for (int l = 0; l < prodJson.optJSONArray("tags").length(); l++) {
                                str = str + ((l == 0 ? "" : ", ") + prodJson.optJSONArray("tags").optJSONObject(l).opt("name"));
                            }
                        }
                        ContentValues values = new ContentValues();
                        values.put(DataBaseHelper.COLUMN_SHOP_ID, prodJson.optString("id"));
                        values.put(DataBaseHelper.COLUMN_SHOP_NAME, prodJson.optString("name"));
                        values.put(DataBaseHelper.COLUMN_SHOP_IMAGE, Constants.BASE_URL + "/images/shops/" + prodJson.optString("image"));
                        values.put(DataBaseHelper.COLUMN_SHOP_IMAGE_THUMB, Constants.BASE_URL + "/images/shops/" + prodJson.optString("image"));
                        values.put(DataBaseHelper.COLUMN_CUT_OF_TIME, prodJson.optString("cutOffTime"));
                        values.put(DataBaseHelper.COLUMN_MIN_ORDER, prodJson.optString("minOrder"));
                        values.put(DataBaseHelper.COLUMN_MID_NIGHT_DEL, prodJson.optString("midnightDeliveryStatus"));
                        values.put(DataBaseHelper.COLUMN_SAME_DAY_DELIVERY, prodJson.optString("sameDayDeliveryStatus"));
                        values.put(DataBaseHelper.COLUMN_RATING, prodJson.optString("rating"));
                        values.put(DataBaseHelper.COLUMN_OPEN_AT, prodJson.optString("opensAt"));
                        values.put(DataBaseHelper.COLUMN_CLOSE_AT, prodJson.optString("closesAt"));
                        values.put(DataBaseHelper.COLUMN_DELIVERY_FROM, prodJson.optString("deliveryFrom"));
                        values.put(DataBaseHelper.COLUMN_DELIVERY_TO, prodJson.optString("deliveryTo"));
                        values.put(DataBaseHelper.COLUMN_REMOTE_DELIVERY, prodJson.optString("remoteArea"));
                        values.put(DataBaseHelper.COLUMN_DELIVERY_CHARGE, prodJson.optString("minDeliveryCharge"));
                        values.put(DataBaseHelper.COLUMN_FREE_DELIVERY, prodJson.optString("freeDeliveryStatus"));
                        values.put(DataBaseHelper.COLUMN_TAGS, str.toString());
                        values.put(DataBaseHelper.COLUMN_CATEGORY_ID, catJson.optString("id"));
                        values.put(DataBaseHelper.COLUMN_CATEGORY_NAME, catJson.optString("name"));
                        commonUtil.insert(dbHelper, DataBaseHelper.TABLE_SHOP, values);
//                    db.insert(DataBaseHelper.TABLE_SHOP, null, values);
//                    db.close();
                    }

                }
                adapter.addFragment(catJson.optString("id"), "     "+catJson.optString("name")+"     ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < dotsCount; i++) {
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
        }
        dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    static class Adapter extends FragmentPagerAdapter {
        //        private final List<Fragment> mFragments = new ArrayList<>();
        static List<String> mFragmentTitles = new ArrayList<>();
        static List<String> mFragmentID = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(String id, String title) {
            mFragmentID.add(id);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return ShopFragment.newInstance(mFragmentID.get(position), mFragmentTitles.get(position));
        }

        @Override
        public int getCount() {
            return mFragmentTitles.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

    private ViewPager intro_images;
    private LinearLayout pager_indicator;
    private int dotsCount;
    private ImageView[] dots;
    private NewFeatureAdapter newFeatureAdapter;
    Runnable runnable;
    int position = 0;
    Integer[] mImageResources = {R.mipmap.one, R.mipmap.two, R.mipmap.three, R.mipmap.four};

    public void setReference(final JSONArray mImageResources) {
        intro_images = (ViewPager) newFeaturesView.findViewById(R.id.pager_introduction);
        pager_indicator = (LinearLayout) newFeaturesView.findViewById(R.id.viewPagerCountDots);
        dotsCount = mImageResources.length();
        newFeatureAdapter = new NewFeatureAdapter(getActivity(), mImageResources);
        intro_images.setAdapter(newFeatureAdapter);
        intro_images.setCurrentItem(0);

        intro_images.setOnPageChangeListener(this);
        runnable = new Runnable() {
            public void run() {
                if (position >= mImageResources.length()) {
                    position = 0;
                } else {
                    position = position + 1;
                }
                intro_images.setCurrentItem(position, true);
                handler.postDelayed(runnable, 3000);
            }
        };
        setUiPageViewController();
    }

    private void setUiPageViewController() {
//        dotsCount = newFeatureAdapter.getCount();
        dots = new ImageView[dotsCount];

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new ImageView(getActivity());
            dots[i].setImageResource(R.drawable.nonselecteditem_dot);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(4, 0, 4, 0);

            pager_indicator.addView(dots[i], params);
        }

        dots[0].setImageResource(R.drawable.selecteditem_dot);
    }


    @Override
    public void onPause() {
        super.onPause();
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        handler.postDelayed(runnable, 3000);
        CommonUtil.cancelProgressDialog();
    }

    public void getBanner() {
        final String tag = "EVENT_FEATURE";
        String url = Constants.BASE_URL + "/mobile/banners/get?defaultToken=" + Constants.DEFAULT_TOKEN + "&eventId=" + String.valueOf(Constants.Events.EVENT_FEATURE);
        DataRequest loginRequest = new DataRequest(Request.Method.GET, url, null, this, this);
        loginRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        GifkarApp.getInstance().addToRequestQueue(loginRequest, tag);
    }

    public void getBottom() {
        final String tag = "EVENT_BOTTOM";
        String url = Constants.BASE_URL + "/mobile/footerNote/get?defaultToken=" + Constants.DEFAULT_TOKEN + "&eventId=" + String.valueOf(Constants.Events.EVENT_BOTTOM);
        DataRequest loginRequest = new DataRequest(Request.Method.GET, url, null, this, this);
        loginRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        GifkarApp.getInstance().addToRequestQueue(loginRequest, tag);
    }

    public void getShopList() {
        final String tag = "EVENT_SHOPLIST";
        String url = Constants.BASE_URL + "/mobile/shop/getByCategory";
        Map<String, String> params = new HashMap<String, String>();
        params.put("areaId", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_AREA_ID, ""));
        params.put("eventId", String.valueOf(Constants.Events.EVENT_SHOPLIST));
        params.put("defaultToken", Constants.DEFAULT_TOKEN);
        Log.i("params", params.toString());
        CommonUtil.showProgressDialog(getActivity(), "Wait...");
        DataRequest loginRequest = new DataRequest(Request.Method.POST, url, params, this, this);
        loginRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        GifkarApp.getInstance().addToRequestQueue(loginRequest, tag);
    }


    @Override
    public void onErrorResponse(VolleyError volleyError) {
        CommonUtil.alertBox(getActivity(), "", getResources().getString(R.string.nointernet_try_again_msg));
        CommonUtil.cancelProgressDialog();
    }

    @Override
    public void onResponse(JSONObject response) {
        if (response != null) {
            try {
                if (response.getInt("status") == Constants.STATUS_SUCCESS) {
                    switch (Integer.valueOf(response.getString("eventId"))) {
                        case Constants.Events.EVENT_FEATURE:
                            setReference(response.optJSONObject("data").optJSONArray("banners"));
                            break;
                        case Constants.Events.EVENT_BOTTOM:
                            if (response.optJSONObject("data").has("footerNote"))
                                tvGreetText.setText(response.optJSONObject("data").optJSONObject("footerNote").optString("message"));
                            break;
                        case Constants.Events.EVENT_SHOPLIST:
                            setupViewPager(response.optJSONObject("data").optJSONArray("categories"));
                            break;


                    }
                } else {
                    JsonErrorShow.jsonErrorShow(response, getActivity());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragments = getChildFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

}
