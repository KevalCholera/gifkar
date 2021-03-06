package com.smartsense.gifkar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MyAddressActivity extends AppCompatActivity implements View.OnClickListener, Response.Listener<JSONObject>,
        Response.ErrorListener {
    ListView lvMyAddress;
    TextView tvMyAddress;
    LinearLayout llAddress, llNoAddress;
    Button btnAddAddress;
    private ImageView btBack, btInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_address);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.action_bar_info, null);
        TextView titleTextView = (TextView) v.findViewById(R.id.actionBarTitle);
        titleTextView.setText(getResources().getString(R.string.screen_my_address));
        btBack = (ImageView) v.findViewById(R.id.btActionBarBack);
        btBack.setOnClickListener(this);
        btInfo = (ImageView) v.findViewById(R.id.btActionBarInfo);
//        btInfo.setBackground(getResources().getDrawable(R.drawable.));
        btInfo.setOnClickListener(this);
        getSupportActionBar().setCustomView(v);
//        Toolbar parent = (Toolbar) v.getParent();//first get parent toolbar of current action bar
//        parent.setContentInsetsAbsolute(0, 0);
        tvMyAddress = (TextView) findViewById(R.id.tvMyAddress);
        tvMyAddress.setOnClickListener(this);
        lvMyAddress = (ListView) findViewById(R.id.lvMyAddress);
        if (getIntent().getBooleanExtra(Constants.SCREEN, false)) {
            titleTextView.setText(getResources().getString(R.string.screen_my_address1));
            lvMyAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(final AdapterView<?> adapterView, View view, final int position, long index) {
                    JSONObject addressObj = (JSONObject) adapterView.getItemAtPosition(position);
                    Intent intent = new Intent();
                    String s1 = addressObj.optString("recipientName") + "\n" + addressObj.optString("recipientContact") + "\n" + addressObj.optString("address") + " " + addressObj.optString("companyName") + " " + addressObj.optString("landmark") + "\n" + addressObj.optJSONObject("area").optString("name") + "," + addressObj.optJSONObject("area").optString("pincode") + " " + addressObj.optJSONObject("city").optString("name");
                    intent.putExtra("address", s1);
                    intent.putExtra("addressId", addressObj.optString("id"));
                    setResult(0, intent);
                    finish();
                }
            });
        }
        btnAddAddress = (Button) findViewById(R.id.btnAddAddress);
        btnAddAddress.setOnClickListener(this);
        llAddress = (LinearLayout) findViewById(R.id.llMyAddress);
        llNoAddress = (LinearLayout) findViewById(R.id.llNoAddreess);
        getAddress();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvMyAddress:
            case R.id.btnAddAddress:
                if (getIntent().getBooleanExtra(Constants.SCREEN, false)) {
                    startActivityForResult(new Intent(this, AddAddressActivity.class).putExtra("area", true), 0);
                } else {
                    startActivityForResult(new Intent(this, AddAddressActivity.class).putExtra("area", false), 0);
                }
                break;
            case R.id.btActionBarBack:
                Intent intent = new Intent();
                intent.putExtra("address", "");
                setResult(0, intent);
                finish();
                break;
            case R.id.btActionBarInfo:
                openInfoPopup();
                break;
            default:
        }
    }

    public void myAddressFill(JSONObject address) {
        MyAddressAdapter myAddressAdapter = null;
        try {
            if (address.getJSONObject("data").getJSONArray("deliveryAddresses").length() > 0) {
                llAddress.setVisibility(View.VISIBLE);
                llNoAddress.setVisibility(View.GONE);
                myAddressAdapter = new MyAddressAdapter(this, address.getJSONObject("data").getJSONArray("deliveryAddresses"), getIntent().getBooleanExtra(Constants.SCREEN, false));
                lvMyAddress.setAdapter(myAddressAdapter);
            } else {
                llAddress.setVisibility(View.GONE);
                llNoAddress.setVisibility(View.VISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent intent = new Intent();
        intent.putExtra("address", "");
        setResult(0, intent);
        finish();
    }

    public void openInfoPopup() {
        try {
            final AlertDialog.Builder alertDialogs = new AlertDialog.Builder(this);
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View dialog = inflater.inflate(R.layout.dialog_info, null);
            alertDialogs.setView(dialog);
            alertDialogs.setCancelable(true);
            TextView tvDialog = (TextView) dialog.findViewById(R.id.textInfoDialog);
            if (getIntent().getBooleanExtra(Constants.SCREEN, false))
                tvDialog.setText(getResources().getString(R.string.address_des1));
            else
                tvDialog.setText(getResources().getString(R.string.address_des));
            AlertDialog alert = alertDialogs.create();
            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getAddress() {
        final String tag = "address";
        String url = Constants.BASE_URL + "/mobile/deliveryAddress/get";
        Map<String, String> params = new HashMap<String, String>();
        if (getIntent().getBooleanExtra(Constants.SCREEN, false))
            params.put("areaId", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_AREA_ID, ""));
        else
            params.put("address", "all");
        params.put("userToken", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, ""));
        params.put("eventId", String.valueOf(Constants.Events.EVENT_GET_ADDRESS));
        params.put("defaultToken", Constants.DEFAULT_TOKEN);
        Log.i("params", params.toString());
        CommonUtil.showProgressDialog(this, "Wait...");
        DataRequest loginRequest = new DataRequest(Request.Method.POST, url, params, this, this);
        loginRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        GifkarApp.getInstance().addToRequestQueue(loginRequest, tag);
    }

    public void deleteAddress(String id) {
        final String tag = "deladdress";
        String url = Constants.BASE_URL + "/mobile/deliveryAddress/delete";
        Map<String, String> params = new HashMap<String, String>();
        params.put("addressId", id);
        params.put("userToken", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, ""));
        params.put("eventId", String.valueOf(Constants.Events.EVENT_DEL_ADDRESS));
        params.put("defaultToken", Constants.DEFAULT_TOKEN);
        Log.i("params", params.toString());
        CommonUtil.showProgressDialog(this, "Wait...");
        DataRequest loginRequest = new DataRequest(Request.Method.POST, url, params, this, this);
        loginRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        GifkarApp.getInstance().addToRequestQueue(loginRequest, tag);
    }


    @Override
    public void onErrorResponse(VolleyError volleyError) {
        CommonUtil.alertBox(this, "", getResources().getString(R.string.nointernet_try_again_msg));
        CommonUtil.cancelProgressDialog();
    }

    @Override
    public void onResponse(JSONObject response) {
        CommonUtil.cancelProgressDialog();
        if (response != null) {
            try {
                if (response.getInt("status") == Constants.STATUS_SUCCESS) {
                    switch (Integer.valueOf(response.getString("eventId"))) {
                        case Constants.Events.EVENT_GET_ADDRESS:
                            myAddressFill(response);
                            break;
                        case Constants.Events.EVENT_DEL_ADDRESS:
                            getAddress();
                            break;
                    }
                } else {
                    JsonErrorShow.jsonErrorShow(response, this);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getAddress();
    }

    public class MyAddressAdapter extends BaseAdapter implements View.OnClickListener {
        JSONArray dataArray;
        private LayoutInflater inflater;
        Activity activity;
        Boolean check;

        public MyAddressAdapter(Activity activity, JSONArray dataArray, Boolean check) {
            this.activity = activity;
            this.dataArray = dataArray;
            this.check = check;
            inflater = LayoutInflater.from(activity);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return dataArray.length();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return dataArray.optJSONObject(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            final ViewHolder holder;

            if (view == null) {
                holder = new ViewHolder();
                view = inflater.inflate(R.layout.element_my_address, parent, false);
                // TODO Auto-generated method stub
                holder.tvName = (TextView) view.findViewById(R.id.tvMyAddressElementName);

                holder.tvNo = (TextView) view.findViewById(R.id.tvMyAddressElementNo);

                holder.tvStreet = (TextView) view.findViewById(R.id.tvMyAddressElementFlatNo);

                holder.tvCity = (TextView) view.findViewById(R.id.tvMyAddressElementStreet);

                holder.ivEdit = (ImageView) view.findViewById(R.id.ivMyAddressElementAddressEdit);

                holder.ivDelete = (ImageView) view.findViewById(R.id.ivMyAddressElementAddressDelete);


                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            JSONObject addressObj = dataArray.optJSONObject(position);
            holder.tvName.setText(addressObj.optString("recipientName"));
            holder.tvNo.setText(addressObj.optString("recipientContact"));
            holder.tvStreet.setText(addressObj.optString("address") + " " + addressObj.optString("companyName") + " " + addressObj.optString("landmark"));
            holder.tvCity.setText(addressObj.optJSONObject("area").optString("name") + "," + addressObj.optJSONObject("area").optString("pincode") + " " + addressObj.optJSONObject("city").optString("name"));
            holder.ivDelete.setOnClickListener(this);
            holder.ivDelete.setTag(addressObj.toString());
            holder.ivEdit.setOnClickListener(this);
            holder.ivEdit.setTag(addressObj.toString());
            if (check) {
                holder.ivDelete.setVisibility(View.GONE);
                holder.ivEdit.setVisibility(View.GONE);
            }
            return view;
        }

        class ViewHolder {
            TextView tvName;
            TextView tvNo;
            TextView tvStreet;
            TextView tvCity;
            ImageView ivEdit;
            ImageView ivDelete;
        }

        @Override
        public void onClick(final View view) {
            switch (view.getId()) {
                case R.id.ivMyAddressElementAddressDelete:
                    AlertDialog.Builder alertbox = new AlertDialog.Builder(activity);
                    alertbox.setCancelable(true);
                    alertbox.setTitle("Delete Address");
                    alertbox.setMessage("Are you sure you want to delete this address?");
                    alertbox.setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            JSONObject objReminder = null;
                            try {
                                objReminder = new JSONObject((String) view.getTag());
                                deleteAddress(objReminder.optString("id"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    });
                    alertbox.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {

                        }
                    });
                    alertbox.show();
                    break;
                case R.id.ivMyAddressElementAddressEdit:
                    activity.startActivityForResult(new Intent(activity, AddAddressActivity.class).putExtra(Constants.SCREEN, Constants.ScreenCode.SCREEN_MYADDRESS).putExtra("Address", (String) view.getTag()).putExtra("area", false), 0);
                    break;
            }

        }

    }
}
