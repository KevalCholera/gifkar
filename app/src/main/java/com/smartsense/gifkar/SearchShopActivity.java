package com.smartsense.gifkar;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mpt.storage.SharedPreferenceUtil;
import com.smartsense.gifkar.adapter.ShopListAdapter;
import com.smartsense.gifkar.utill.CommonUtil;
import com.smartsense.gifkar.utill.Constants;
import com.smartsense.gifkar.utill.DataBaseHelper;
import com.smartsense.gifkar.utill.RecyclerItemClickListener;

import java.util.StringTokenizer;

public class SearchShopActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView btBack;
    private EditText etSearch;
    private RecyclerView lvSearch;
    private LinearLayout llSearch;
    DataBaseHelper dbHelper = new DataBaseHelper(SearchShopActivity.this);
    CommonUtil commonUtil = new CommonUtil();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.action_bar_center, null);
        TextView titleTextView = (TextView) v.findViewById(R.id.actionBarTitle);
        titleTextView.setText(getResources().getString(R.string.search));
        btBack = (ImageView) v.findViewById(R.id.btActionBarBack);
        btBack.setOnClickListener(this);
        getSupportActionBar().setCustomView(v);
        setContentView(R.layout.activity_shop_search);
        etSearch = (EditText) findViewById(R.id.etSearch);
        lvSearch = (RecyclerView) findViewById(R.id.lvSearch);
        lvSearch.setLayoutManager(new LinearLayoutManager(lvSearch.getContext()));
        llSearch = (LinearLayout) findViewById(R.id.llSearch);
        lvSearch.setVisibility(View.GONE);
        llSearch.setVisibility(View.VISIBLE);
        Log.i("id", "" + getIntent().getStringExtra("id"));
        etSearch.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) { // TODO Auto-generated method stub
                if (!s.toString().equalsIgnoreCase("")) {
                    Cursor cursor = commonUtil.rawQuery(dbHelper, "SELECT * FROM " + DataBaseHelper.TABLE_SHOP + "  WHERE " + DataBaseHelper.COLUMN_CATEGORY_ID + " = '"
                            + getIntent().getStringExtra("id") + "' AND (" + DataBaseHelper.COLUMN_SHOP_NAME + " like '" + s.toString() + "%' OR " + DataBaseHelper.COLUMN_TAGS + " like '%" + s.toString() + "%')");
                    if (cursor.getCount() > 0) {
                        lvSearch.setVisibility(View.VISIBLE);
                        llSearch.setVisibility(View.GONE);
                        lvSearch.setAdapter(new ShopListAdapter(SearchShopActivity.this, cursor));
//                        Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(cursor));
                    } else {

                    }
                    s.toString();
                } else {
                    lvSearch.setVisibility(View.GONE);
                    llSearch.setVisibility(View.VISIBLE);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });
        lvSearch.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
//                        Log.i("ShopID", String.valueOf());
                        String str = (String) view.getTag();
//                        String[] separated = str.split(" ");
                        StringTokenizer st = new StringTokenizer(str, " ");
                        SharedPreferenceUtil.putValue(Constants.PrefKeys.SHOP_ID, st.nextToken());
                        SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_CATEGORY_ID, st.nextToken());
                        SharedPreferenceUtil.putValue(Constants.PrefKeys.SHOP_NAME, st.nextToken());
                        SharedPreferenceUtil.save();
                        startActivity(new Intent(SearchShopActivity.this, ProductListActivity.class));
                    }
                })
        );
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btActionBarBack:
                CommonUtil.closeKeyboard(this);
                finish();
                break;
            default:
        }
    }
}
