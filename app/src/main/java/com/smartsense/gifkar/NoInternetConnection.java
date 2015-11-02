package com.smartsense.gifkar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.smartsense.gifkar.utill.CommonUtil;


public class NoInternetConnection extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet_connection);

        Button btnnointernet = (Button) findViewById(R.id.btn_retry_noconnection);
        btnnointernet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CommonUtil.isInternetAvailable(NoInternetConnection.this)) {
                    Intent i = new Intent(NoInternetConnection.this, SplashActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Toast.makeText(NoInternetConnection.this, "Check Internet Connection", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
