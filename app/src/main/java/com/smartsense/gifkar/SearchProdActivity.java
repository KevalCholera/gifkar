package com.smartsense.gifkar;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.smartsense.gifkar.adapter.ProductAdapter;
import com.smartsense.gifkar.utill.CommonUtil;
import com.smartsense.gifkar.utill.DataBaseHelper;

public class SearchProdActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView btBack;
    private EditText etSearch;
    private ListView lvSearch;
    private LinearLayout llSearch;
    DataBaseHelper dbHelper = new DataBaseHelper(SearchProdActivity.this);
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
        setContentView(R.layout.activity_prod_search);
        etSearch = (EditText) findViewById(R.id.etSearch);
        lvSearch = (ListView) findViewById(R.id.lvSearch);
        llSearch = (LinearLayout) findViewById(R.id.llSearch);
        lvSearch.setVisibility(View.GONE);
        llSearch.setVisibility(View.VISIBLE);
//        Log.i("id", "" + getIntent().getStringExtra("id"));
        etSearch.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) { // TODO Auto-generated method stub
                if (!s.toString().equalsIgnoreCase("")) {
                    Cursor cursor = commonUtil.rawQuery(dbHelper, "SELECT * FROM " + DataBaseHelper.TABLE_PRODUCT + "  WHERE  (" + DataBaseHelper.COLUMN_PROD_NAME + " like '" + s.toString() + "%' OR " + DataBaseHelper.COLUMN_PROD_CATEGORY_NAME + " like '%" + s.toString() + "%')");
                    if (cursor.getCount() > 0) {
                        lvSearch.setVisibility(View.VISIBLE);
                        llSearch.setVisibility(View.GONE);
                        lvSearch.setAdapter(new ProductAdapter(SearchProdActivity.this, cursor,dbHelper,true));
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
        lvSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(final AdapterView<?> adapterView, View view, final int position, long index) {
//                JSONObject getCodeObj = (JSONObject) adapterView.getItemAtPosition(position);
                startActivity(new Intent(SearchProdActivity.this, ProductDetailActivity.class).putExtra("ProdDEID", (Integer) view.getTag()));

            }
        });
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
