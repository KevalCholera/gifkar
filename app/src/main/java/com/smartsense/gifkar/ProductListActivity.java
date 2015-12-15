package com.smartsense.gifkar;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mpt.storage.SharedPreferenceUtil;
import com.smartsense.gifkar.utill.CommonUtil;
import com.smartsense.gifkar.utill.Constants;
import com.smartsense.gifkar.utill.DataBaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProductListActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView btBack;
    private ImageView btFilter;
    private ImageView btInfo;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    DataBaseHelper dbHelper;
    CommonUtil commonUtil = new CommonUtil();
    private LinearLayout llProdCheckOut;
    private LinearLayout llProdCart;
    private static TextView tvProdCartRs;
    private static TextView tvProdCartCount;
    private static LinearLayout llProdBottom;
    private productListAdapter adapter;
    private ImageView btActionBarSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DataBaseHelper(this);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.action_bar_prod, null);
        TextView titleTextView = (TextView) v.findViewById(R.id.actionBarTitle);
        titleTextView.setText(getResources().getString(R.string.shop_name));
        btBack = (ImageView) v.findViewById(R.id.btActionBarBack);
        btBack.setOnClickListener(this);
        btFilter = (ImageView) v.findViewById(R.id.btActionBarfilter);
        btFilter.setOnClickListener(this);
        btInfo = (ImageView) v.findViewById(R.id.btActionBarInfo);
        btInfo.setOnClickListener(this);
        btActionBarSearch= (ImageView) v.findViewById(R.id.btActionBarSearch);
        btActionBarSearch.setOnClickListener(this);
        getSupportActionBar().setCustomView(v);
        Toolbar parent = (Toolbar) v.getParent();
        parent.setContentInsetsAbsolute(0, 0);
        setContentView(R.layout.activity_product_list);
        viewPager = (ViewPager) findViewById(R.id.vpProdListDetail);
        tabLayout = (TabLayout) findViewById(R.id.tlProdListTabList);
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.tab_idicator));
        tabLayout.setTabTextColors(getResources().getColor(R.color.tab_normal_text), getResources().getColor(R.color.tab_idicator));
        setupViewPager();
        tvProdCartCount = (TextView) findViewById(R.id.tvProdListCartCount);
        tvProdCartRs = (TextView) findViewById(R.id.tvProdListCartRs);
        llProdCart = (LinearLayout) findViewById(R.id.llProdListCart);
        llProdCart.setOnClickListener(this);
        llProdBottom = (LinearLayout) findViewById(R.id.llProdListBottom);
        llProdCheckOut =(LinearLayout) findViewById(R.id.llProdListCheckOut);
        llProdCheckOut.setOnClickListener(this);
        getIntent().getIntExtra("ShopID", 0);
        getCartItem();
    }

    public void getCartItem() {
        try {
            JSONArray productArray;
            if (SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_PROD_LIST, "") == "")
                productArray = new JSONArray();
            else
                productArray = new JSONArray(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_PROD_LIST, ""));
            checkCart(ProductListActivity.this, productArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btActionBarfilter:
                startActivity(new Intent(this, ProductFilterActivity.class));
                break;
            case R.id.btActionBarSearch:
                startActivity(new Intent(this, SearchActivity.class));
                break;
            case R.id.btActionBarBack:
                finish();
                break;
            case R.id.btActionBarInfo:
                startActivity(new Intent(this, ShopActivity.class));
                break;
            case R.id.llProdListCart:
                startActivity(new Intent(this, MyCartActivity.class));
                break;
            case R.id.llProdListCheckOut:
                startActivity(new Intent(this, Checkout1Activity.class));
                break;
            default:
        }
    }

    static class productListAdapter extends FragmentPagerAdapter {
        //        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();
        private final List<String> mFragmentID = new ArrayList<>();

        public productListAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(String id, String title) {
            mFragmentID.add(id);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return ProductFragment.newInstance(mFragmentID.get(position));
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

    private void setupViewPager() {
        adapter = new productListAdapter(getSupportFragmentManager());
        String tempary = "{\"eventId\":123,\"errorCode\":0,\"status\":200,\"message\":\"Product List.\",\"data\":{\"products\":[{\"id\":1,\"name\":\"Sub category\",\"products\":[{\"productDetailId\":7,\"subCategoryId\":1,\"description\":\"\",\"productCode\":\"123153\",\"quantity\":10,\"price\":\"100.00\",\"image\":\"productImg1446529988.png\",\"itemType\":\"na\",\"earliestDelivery\":12,\"isAvailble\":\"1\",\"unit\":{\"id\":4,\"name\":\"Bottle\",\"description\":\"Bottle\\n\"},\"productId\":6,\"name\":\"product6\",\"packageType\":{\"id\":12,\"name\":\"Box\"}},{\"productDetailId\":9,\"subCategoryId\":1,\"description\":\"desc2\",\"productCode\":\"12354\",\"quantity\":11,\"price\":\"100.00\",\"image\":\"productImg1446525634.jpg\",\"itemType\":\"non-veg\",\"earliestDelivery\":12,\"isAvailble\":\"1\",\"unit\":{\"id\":2,\"name\":\"Ml\",\"description\":\"Miligram\\n\"},\"productId\":6,\"name\":\"product6\",\"packageType\":{\"id\":5,\"name\":\"Bottle\"}},{\"productDetailId\":10,\"subCategoryId\":1,\"description\":\"desc\",\"productCode\":\"121356\",\"quantity\":12,\"price\":\"100.00\",\"image\":\"productImg1446530085.jpg\",\"itemType\":\"veg\",\"earliestDelivery\":12,\"isAvailble\":\"1\",\"unit\":{\"id\":3,\"name\":\"Box\",\"description\":\"BOx\"},\"productId\":6,\"name\":\"product6\",\"packageType\":{\"id\":12,\"name\":\"Box\"}}]},{\"id\":2,\"name\":\"Sub category1\",\"products\":[{\"productDetailId\":12,\"subCategoryId\":2,\"description\":\"description\",\"productCode\":\"123153\",\"quantity\":1,\"price\":\"100.00\",\"image\":\"\",\"itemType\":\"na\",\"earliestDelivery\":12,\"isAvailble\":\"1\",\"unit\":{\"id\":1,\"name\":\"Kg\",\"description\":\"Kilogram\\n\"},\"productId\":9,\"name\":\"Some Item\",\"packageType\":{\"id\":5,\"name\":\"Bottle\"}}]}]}}\n";
        try {
            JSONObject response = new JSONObject(tempary);
            JSONArray product = response.optJSONObject("data").optJSONArray("products");
            insertItemInCart(product);
            adpterViewPager();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void adpterViewPager() {
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onResume() {
        super.onResume();
        adpterViewPager();
        getCartItem();
    }


    public void insertItemInCart(JSONArray product) {
        try {
//            SQLiteDatabase db;
//            db = dbHelper.getReadableDatabase();
            commonUtil.execSQL(dbHelper, "DELETE FROM " + DataBaseHelper.TABLE_PRODUCT);
            for (int i = 0; i < product.length(); i++) {
                JSONObject catJson = product.optJSONObject(i);
                if (catJson.has("products")) {
                    for (int j = 0; j < catJson.optJSONArray("products").length(); j++) {
                        JSONObject prodJson = catJson.optJSONArray("products").optJSONObject(j);
                        ContentValues values = new ContentValues();

                        values.put(DataBaseHelper.COLUMN_PROD_CODE, prodJson.optString("productCode"));
                        values.put(DataBaseHelper.COLUMN_PROD_DETAIL_ID, prodJson.optString("productDetailId"));
                        values.put(DataBaseHelper.COLUMN_PROD_ID, prodJson.optString("productId"));
                        values.put(DataBaseHelper.COLUMN_PROD_NAME, prodJson.optString("name"));
                        values.put(DataBaseHelper.COLUMN_PROD_IMAGE, prodJson.optString("image"));
                        values.put(DataBaseHelper.COLUMN_PROD_PRICE, prodJson.optString("price"));
                        values.put(DataBaseHelper.COLUMN_PROD_QUANTITY, prodJson.optString("quantity"));
                        values.put(DataBaseHelper.COLUMN_PROD_ITEM_TYPE, prodJson.optString("itemType"));
                        values.put(DataBaseHelper.COLUMN_PROD_EARLIY_DEL, prodJson.optString("earliestDelivery"));
                        values.put(DataBaseHelper.COLUMN_PROD_IS_AVAIL, prodJson.optString("isAvailble"));
                        values.put(DataBaseHelper.COLUMN_PROD_DESC, prodJson.optString("description"));
                        if (prodJson.has("unit")) {
                            values.put(DataBaseHelper.COLUMN_PROD_UNIT_ID, prodJson.optJSONObject("unit").optString("id"));
                            values.put(DataBaseHelper.COLUMN_PROD_UNIT_NAME, prodJson.optJSONObject("unit").optString("name"));
                        }
                        if (prodJson.has("packageType")) {
                            values.put(DataBaseHelper.COLUMN_PROD_PACKAGE_ID, prodJson.optJSONObject("packageType").optString("id"));
                            values.put(DataBaseHelper.COLUMN_PROD_PACKAGE_NAME, prodJson.optJSONObject("packageType").optString("name"));
                        }
                        values.put(DataBaseHelper.COLUMN_PROD_CATEGORY_ID, catJson.optString("id"));
                        values.put(DataBaseHelper.COLUMN_PROD_CATEGORY_NAME, catJson.optString("name"));
                        commonUtil.insert(dbHelper, DataBaseHelper.TABLE_PRODUCT, values);
//                    db.insert(DataBaseHelper.TABLE_SHOP, null, values);
//                    db.close();
                    }

                }
                adapter.addFragment(catJson.optString("id"), catJson.optString("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void checkCart(Context context, JSONArray productArray) {
        double totalAmount = 0;
        for (int i = 0; i < productArray.length(); i++) {
            totalAmount += (productArray.optJSONObject(i).optDouble(DataBaseHelper.COLUMN_PROD_PRICE) * productArray.optJSONObject(i).optDouble("quantity"));
        }

        if (totalAmount == 0) {
            llProdBottom.setVisibility(View.GONE);
        } else {
            llProdBottom.setVisibility(View.VISIBLE);
            tvProdCartRs.setText("₹" + totalAmount);
            tvProdCartCount.setText("" + productArray.length());
        }

    }


}
