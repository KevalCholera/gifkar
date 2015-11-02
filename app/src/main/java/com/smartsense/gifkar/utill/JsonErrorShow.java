package com.smartsense.gifkar.utill;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import com.mpt.storage.SharedPreferenceUtil;
import com.smartsense.gifkar.GifkarActivity;

import org.json.JSONObject;

/**
 * Created by Sanchi on 10/15/2015.
 */
public class JsonErrorShow {
    public static void jsonErrorShow(JSONObject response, Activity a) {
        String msg = response.optString("message");
        int errorCode = Integer.valueOf(response.optString("error_code"));
        switch (errorCode) {
            case 0:
                diloagMsgShow(a, msg);
                break;
            case Constants.ErrorCode.INVALID_CREDENTIALS:
                diloagMsgShow(a, msg);
                break;
            case Constants.ErrorCode.UNAUTHORIZED:
                SharedPreferenceUtil.remove(Constants.PrefKeys.PREF_ACCESS_TOKEN);
                SharedPreferenceUtil.save();
                a.startActivity(new Intent(a, GifkarActivity.class));
                a.finish();
                break;
            case Constants.ErrorCode.PARAM_MISSING:
                diloagMsgShow(a, msg);
                break;
            case Constants.ErrorCode.SERVER_ERROR:
                a.startActivity(new Intent(a, GifkarActivity.class));
                a.finish();
                break;
            case Constants.ErrorCode.UNVERIFIED:
                diloagMsgShow(a, msg);
                break;
            default:
                a.startActivity(new Intent(a, GifkarActivity.class));
                a.finish();
                break;
        }
    }

    public static void diloagMsgShow(Activity a, String msg) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(a);
//        alert.setTitle("Error!");
        alert.setMessage(msg);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }

        });
        alert.show();
    }
}
