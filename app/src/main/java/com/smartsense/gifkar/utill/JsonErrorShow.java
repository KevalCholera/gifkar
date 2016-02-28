package com.smartsense.gifkar.utill;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.mpt.storage.SharedPreferenceUtil;
import com.parse.ParseInstallation;
import com.smartsense.gifkar.GifkarActivity;
import com.smartsense.gifkar.StartActivity;

import org.json.JSONObject;

/**
 * Created by Ronak on 10/15/2015.
 */
public class JsonErrorShow {
    public static void jsonErrorShow(JSONObject response, Activity a) {
        String msg = response.optString("message");
        switch (response.optInt("errorCode")) {
            case 0:
                diloagMsgShow(a, msg);
                break;
            case Constants.ErrorCode.INVALID_CREDENTIALS:
            case Constants.ErrorCode.UNAUTHORIZED:
                diloagMsgShow(a, msg);
                ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                installation.put("userId", "");
                installation.saveInBackground();
                SharedPreferenceUtil.remove(Constants.PrefKeys.PREF_USER_ID);
                SharedPreferenceUtil.remove(Constants.PrefKeys.PREF_USER_FULLNAME);
                SharedPreferenceUtil.remove(Constants.PrefKeys.PREF_USER_EMAIL);
                SharedPreferenceUtil.remove(Constants.PrefKeys.PREF_USER_MNO);
                SharedPreferenceUtil.remove(Constants.PrefKeys.PREF_ACCESS_TOKEN);
                SharedPreferenceUtil.remove(Constants.PrefKeys.PREF_USER_PROIMG);
                SharedPreferenceUtil.remove(Constants.PrefKeys.PREF_USER_INFO);
                SharedPreferenceUtil.save();
                a.startActivity(new Intent(a, StartActivity.class));
                a.finish();
//                SharedPreferenceUtil.remove(Constants.PrefKeys.PREF_ACCESS_TOKEN);
//                SharedPreferenceUtil.save();
//                a.startActivity(new Intent(a, GifkarActivity.class));
//                a.finish();
                break;
            case Constants.ErrorCode.PARAM_MISSING:
                diloagMsgShow(a, msg);
                break;
            case Constants.ErrorCode.SERVER_ERROR:
//                a.startActivity(new Intent(a, GifkarActivity.class));
//                a.finish();
                diloagMsgShow(a, msg);
                break;
//            case Constants.ErrorCode.UNVERIFIED:
//                diloagMsgShow(a, msg);
//                break;
            default:
                a.startActivity(new Intent(a, GifkarActivity.class));
                a.finish();
                break;
        }
    }

    public static void diloagMsgShow(Activity a, String msg) {
        Toast.makeText(a, msg, Toast.LENGTH_SHORT).show();
//        final AlertDialog.Builder alert = new AlertDialog.Builder(a);
////        alert.setTitle("Error!");
//        alert.setMessage(msg);
//        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//
//            }
//
//        });
//        alert.show();
    }
}
