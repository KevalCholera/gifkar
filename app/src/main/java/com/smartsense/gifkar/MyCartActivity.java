package com.smartsense.gifkar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class MyCartActivity extends AppCompatActivity implements View.OnClickListener{
    ListView lvMyCart;
    LinearLayout llMyCart,llMyCartEmpty;
    private ImageView btBack, btCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.action_bar_info, null);
        TextView titleTextView = (TextView) v.findViewById(R.id.actionBarTitle);
        titleTextView.setText(getResources().getString(R.string.screen_my_cart));
        btBack = (ImageView) v.findViewById(R.id.btActionBarBack);
        btBack.setOnClickListener(this);
        btCart = (ImageView) v.findViewById(R.id.btActionBarInfo);
        btCart.setOnClickListener(this);
        getSupportActionBar().setCustomView(v);
        Toolbar parent = (Toolbar) v.getParent();//first get parent toolbar of current action bar
        parent.setContentInsetsAbsolute(0, 0);

        setContentView(R.layout.activity_my_cart);

        llMyCart=(LinearLayout) findViewById(R.id.ll_cart);
        llMyCartEmpty=(LinearLayout) findViewById(R.id.ll_cart_empty);
        lvMyCart=(ListView) findViewById(R.id.lvCart);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llMyAddress:
                startActivity(new Intent(this, MobileNoActivity.class));
                break;
            case R.id.btActionBarBack:
                finish();
                break;
            case R.id.btActionBarInfo:
                AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
                alertbox.setCancelable(true);
                alertbox.setMessage("Are you sure you want to delete ?");
                alertbox.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
//                        JSONObject objAddress = (JSONObject) view.getTag();
//                        String deleteId=objAddress.optString("");
                    }

                });
                alertbox.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });
                alertbox.show();
                break;
            default:
        }
    }
}
