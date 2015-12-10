package com.smartsense.gifkar;

import android.content.ContentValues;
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

import com.smartsense.gifkar.utill.CommonUtil;
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
    private TextView tvProdCartRs;
    private TextView tvProdCartCount;

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
        getSupportActionBar().setCustomView(v);
        Toolbar parent = (Toolbar) v.getParent();
        parent.setContentInsetsAbsolute(0, 0);
        setContentView(R.layout.activity_product_list);
        viewPager = (ViewPager) findViewById(R.id.vpProdListDetail);
        tabLayout = (TabLayout) findViewById(R.id.tlProdListTabList);
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.tab_idicator));
        tabLayout.setTabTextColors(getResources().getColor(R.color.tab_normal_text), getResources().getColor(R.color.tab_idicator));
        setupViewPager(viewPager);
//        tvProdCartCount = (TextView) findViewById(R.id.tvProdListCartCount);
//        tvProdCartRs = (TextView) findViewById(R.id.tvProdListCartRs);
//        llProdCart =(LinearLayout) findViewById(R.id.llProdListCart);
//        llProdCart.setOnClickListener(this);
//        llProdCheckOut =(LinearLayout) findViewById(R.id.llProdDetailCheckOut);
//        llProdCheckOut.setOnClickListener(this);
        getIntent().getIntExtra("ShopID", 0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btActionBarfilter:
                startActivity(new Intent(this, AddAddressActivity.class));
                break;
            case R.id.btActionBarBack:
                finish();
                break;
            case R.id.btActionBarInfo:
                startActivity(new Intent(this, AddAddressActivity.class));
                break;
            case R.id.llProdListCart:
                startActivity(new Intent(this, AddAddressActivity.class));
                break;
            case R.id.llProdListCheckOut:
                startActivity(new Intent(this, AddAddressActivity.class));
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

    private void setupViewPager(ViewPager viewPager) {
        productListAdapter adapter = new productListAdapter(getSupportFragmentManager());
        String tempary = "{\"eventId\":123,\"errorCode\":0,\"status\":200,\"message\":\"Product List.\",\"data\":{\"products\":[{\"id\":1,\"name\":\"Sub category\",\"products\":[{\"id\":7,\"subCategoryId\":1,\"description\":\"\",\"productCode\":\"123153\",\"quantity\":10,\"price\":\"100.00\",\"image\":\"productImg1446529988.png\",\"itemType\":\"na\",\"earliestDelivery\":12,\"isAvailble\":\"1\",\"unit\":{\"id\":4,\"name\":\"Bottle\",\"description\":\"Bottle\\n\"},\"packageType\":{\"id\":12,\"name\":\"Box\"}},{\"id\":9,\"subCategoryId\":1,\"description\":\"desc2\",\"productCode\":\"12354\",\"quantity\":11,\"price\":\"100.00\",\"image\":\"productImg1446525634.jpg\",\"itemType\":\"non-veg\",\"earliestDelivery\":12,\"isAvailble\":\"1\",\"unit\":{\"id\":2,\"name\":\"Ml\",\"description\":\"Miligram\\n\"},\"packageType\":{\"id\":5,\"name\":\"Bottle\"}},{\"id\":10,\"subCategoryId\":1,\"description\":\"desc\",\"productCode\":\"121356\",\"quantity\":12,\"price\":\"100.00\",\"image\":\"productImg1446530085.jpg\",\"itemType\":\"veg\",\"earliestDelivery\":12,\"isAvailble\":\"1\",\"unit\":{\"id\":3,\"name\":\"Box\",\"description\":\"BOx\"},\"packageType\":{\"id\":12,\"name\":\"Box\"}}]},{\"id\":2,\"name\":\"Sub category1\",\"products\":[{\"id\":12,\"subCategoryId\":2,\"description\":\"description\",\"productCode\":\"123153\",\"quantity\":1,\"price\":\"100.00\",\"image\":\"\",\"itemType\":\"na\",\"earliestDelivery\":12,\"isAvailble\":\"1\",\"unit\":{\"id\":1,\"name\":\"Kg\",\"description\":\"Kilogram\\n\"},\"packageType\":{\"id\":5,\"name\":\"Bottle\"}}]}]}}\n";
        try {
            JSONObject response = new JSONObject(tempary);
            JSONArray product = response.optJSONObject("data").optJSONArray("products");
            insertItemInCart(adapter, product);
            viewPager.setAdapter(adapter);
            tabLayout.setupWithViewPager(viewPager);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void insertItemInCart(productListAdapter adapter, JSONArray product) {
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
                        values.put(DataBaseHelper.COLUMN_PROD_ID, prodJson.optString("id"));
                        values.put(DataBaseHelper.COLUMN_PROD_NAME, prodJson.optString("name"));
                        values.put(DataBaseHelper.COLUMN_PROD_IMAGE, prodJson.optString("image"));
                        values.put(DataBaseHelper.COLUMN_PROD_PRICE, prodJson.optString("price"));
                        values.put(DataBaseHelper.COLUMN_PROD_QUANTITY, prodJson.optString("quantity"));
                        values.put(DataBaseHelper.COLUMN_PROD_ITEM_TYPE, prodJson.optString("itemType"));
                        values.put(DataBaseHelper.COLUMN_PROD_EARLIY_DEL, prodJson.optString("earliestDelivery"));
                        values.put(DataBaseHelper.COLUMN_PROD_IS_AVAIL, prodJson.optString("isAvailble"));
                        values.put(DataBaseHelper.COLUMN_PROD_DESC, prodJson.optString("description"));
                        if (prodJson.has("unit")) {
                            values.put(DataBaseHelper.COLUMN_PROD_UNIT_ID, prodJson.optJSONObject("unit").optString("closesAt"));
                            values.put(DataBaseHelper.COLUMN_PROD_UNIT_NAME, prodJson.optJSONObject("unit").optString("deliveryFrom"));
                        }
                        if (prodJson.has("packageType")) {
                            values.put(DataBaseHelper.COLUMN_PROD_PACKAGE_ID, prodJson.optJSONObject("packageType").optString("deliveryTo"));
                            values.put(DataBaseHelper.COLUMN_PROD_PACKAGE_NAME, prodJson.optJSONObject("packageType").optString("remoteArea"));
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


}
