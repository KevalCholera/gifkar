package com.smartsense.gifkar.utill;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

//import org.apache.http.HttpStatus;


public class CommonUtil {

    static ProgressDialog pDialog;
    SQLiteDatabase sqLiteDatabase;

    public static void showProgressDialog(Context activity, String msg) {
        pDialog = new ProgressDialog(activity);
        pDialog.setMessage(msg);
        pDialog.show();
    }


    public static void cancelProgressDialog() {
        // pDialog = new ProgressDialog(activity);
        if (pDialog != null)
            pDialog.cancel();
    }

    public static boolean isValidEmail(CharSequence strEmail) {
        return !TextUtils.isEmpty(strEmail) && android.util.Patterns.EMAIL_ADDRESS.matcher(strEmail).matches();
    }

    public static void alertBox(Context context, String title, String msg) {
        Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(title);
        alert.setMessage(msg);
        alert.setPositiveButton("OK", null);
        alert.show();
    }

    static public ActionBar getActionBar(Activity a) {
        return ((AppCompatActivity) a).getSupportActionBar();
    }

    public static boolean isInternet(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }

//    public static void volleyErrorHandler(VolleyError volleyError, Activity activity) {
//
//        NetworkResponse response = null;
//
//        if (volleyError instanceof TimeoutError || volleyError instanceof NoConnectionError) {
//            // CommonUtil.ShowAlert(activity,
//            // "No connection" + volleyError.getMessage());
//            response = volleyError.networkResponse;
//        } else if (volleyError instanceof AuthFailureError) {
//            response = volleyError.networkResponse;
//        } else if (volleyError instanceof ServerError) {
//            response = volleyError.networkResponse;
//        } else if (volleyError instanceof NetworkError) {
//            response = volleyError.networkResponse;
//        } else if (volleyError instanceof ParseError) {
//            String error = volleyError.getMessage();
//            // CommonUtil.ShowAlert(activity, "Wrong data recived" + error);
//
//        }
//
//        if (response != null && response.data != null) {
//
//            switch (response.statusCode) {
//                case HttpStatus.SC_NOT_FOUND:
//
////				Toast.makeText(activity, "Page not found ERROR 404", Toast.LENGTH_LONG).show();
//
//                    CommonUtil
//                            .alertBox(activity, "", "Page not found Error code :404");
//                    break;
//                case HttpStatus.SC_INTERNAL_SERVER_ERROR:
//                    CommonUtil.alertBox(activity, "", "Server Error code :500");
//                    break;
//                default:
//                    CommonUtil.alertBox(activity, "", "Error");
//                    break;
//            }
//        }
//    }

    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
//            NetworkInfo[] info = connectivity.getAllNetworkInfo();
//            if (info != null)
//                for (int i = 0; i < info.length; i++)
//                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
//                        return true;
//                    }
            NetworkInfo activeNetwork = connectivity.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnected();
        }
        return false;

    }

    public static boolean isGPS(Context context) {
        final LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return true;
        } else {
            return false;
        }

    }

    public Cursor rawQuery(DataBaseHelper dbHelper, String sqlQuery) {
        Log.i("sqlQuery", sqlQuery);
        Cursor cursor = null;
        try {
            sqLiteDatabase = dbHelper.openDataBase();
            cursor = sqLiteDatabase.rawQuery(sqlQuery, null);
            cursor.moveToFirst();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqLiteDatabase.close();
        }
        return cursor;
    }

    public Boolean execSQL(DataBaseHelper dbHelper, String sqlQuery) {
        // TODO Auto-generated method stub
        Boolean check = false;
        try {
            sqLiteDatabase = dbHelper.getReadableDatabase();
            sqLiteDatabase.execSQL(sqlQuery);
            check = true;
        } catch (Exception e) {
            e.printStackTrace();
            check = false;
        } finally {
            sqLiteDatabase.close();
        }
        return check;

    }

    public long insert(DataBaseHelper dbHelper, String table, ContentValues values) {
        sqLiteDatabase = dbHelper.getReadableDatabase();
        String nullColumnHack = null;
        return sqLiteDatabase.insert(table, nullColumnHack, values);
    }

    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.NO_WRAP);
        return temp;
    }

    public static Bitmap decodeFromBitmap(String encodedstring) {
        Bitmap decodedByte;
        try {
            byte[] decodedString = Base64.decode(encodedstring, Base64.DEFAULT);
            decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        } catch (Exception e) {
            e.printStackTrace();
            decodedByte = null;
        }
        return decodedByte;
    }

    static public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    static public String getRealPathFromURI(Uri uri, Activity a) {
        Cursor cursor = a.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public static void openAppRating(Context context) {
        Intent rateIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + context.getPackageName()));
        boolean marketFound = false;

        // find all applications able to handle our rateIntent
        final List<ResolveInfo> otherApps = context.getPackageManager().queryIntentActivities(rateIntent, 0);
        for (ResolveInfo otherApp : otherApps) {
            // look for Google Play application
            if (otherApp.activityInfo.applicationInfo.packageName.equals("com.android.vending")) {

                ActivityInfo otherAppActivity = otherApp.activityInfo;
                ComponentName componentName = new ComponentName(
                        otherAppActivity.applicationInfo.packageName,
                        otherAppActivity.name
                );
                rateIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                rateIntent.setComponent(componentName);
                context.startActivity(rateIntent);
                marketFound = true;
                break;

            }
        }

        // if GP not present on device, open web browser
        if (!marketFound) {
            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + context.getPackageName()));
            context.startActivity(webIntent);
        }
    }

    static public void closeKeyboard(Activity a) {
        try {
            InputMethodManager inputManager = (InputMethodManager) a.getSystemService(Context.INPUT_METHOD_SERVICE);

            inputManager.hideSoftInputFromWindow(a.getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {

        }
    }

//    public static Boolean isOnline(Context context) {
//        try {
//            showProgressDialog(context, "Wait...");
//            Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.com");
//            int returnVal = p1.waitFor();
//            boolean reachable = (returnVal == 0);
//            cancelProgressDialog();
//            return reachable;
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        return false;
//    }

    public static boolean isOnline() {
        try {
            checkStrictModeThread();
            HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
            urlc.setRequestProperty("User-Agent", "Test");
            urlc.setRequestProperty("Connection", "close");
            urlc.setConnectTimeout(1000);
            urlc.connect();

            return (urlc.getResponseCode() == 200);

        } catch (IOException e) {

            return false;
        }
    }

    public static void checkStrictModeThread() {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    public static String beforeAlaram(int id) {
        switch (id) {
            case 1:
                return "1 Day Before";
            case 2:
                return "2 Day Before";
            case 3:
                return "1 Hour Before";
            default:
                return "1 Day Before";
        }
    }

}