package com.smartsense.gifkar;

import android.*;
import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.mpt.storage.SharedPreferenceUtil;
import com.smartsense.gifkar.utill.CircleImageView;
import com.smartsense.gifkar.utill.CommonUtil;
import com.smartsense.gifkar.utill.Constants;
import com.smartsense.gifkar.utill.DataRequest;
import com.smartsense.gifkar.utill.JsonErrorShow;
import com.smartsense.gifkar.utill.MultipartRequestJson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView btBack;
    TextView tvName, tvMobile, tvVerified;
    CircleImageView ivProfileImage;
    ImageLoader imageLoader;
    private static final int REQUEST_CAMERA = 0;
    private final int SELECT_FILE = 1;
    File dir = null;
    private String outputFile = "";
    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.action_bar_center, null);
        TextView titleTextView = (TextView) v.findViewById(R.id.actionBarTitle);
        titleTextView.setText(getResources().getString(R.string.screen_profile));
        btBack = (ImageView) v.findViewById(R.id.btActionBarBack);
        btBack.setOnClickListener(this);
        getSupportActionBar().setCustomView(v);
        tvMobile = (TextView) findViewById(R.id.tVProfileMobileNo);
        tvMobile.setOnClickListener(this);
        tvName = (TextView) findViewById(R.id.tvProfileName);
        tvVerified = (TextView) findViewById(R.id.tVProfileVerified);
        ivProfileImage = (CircleImageView) findViewById(R.id.ivProfileImage);
        ivProfileImage.setOnClickListener(this);
        imageLoader = GifkarApp.getInstance().getDiskImageLoader();
        try {
            JSONObject userInfo = new JSONObject(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_INFO, ""));
            userInfo = userInfo.optJSONObject("userDetails");
//            Constants.BASE_URL + "/images/users/" +
            ivProfileImage.setDefaultImageResId(R.drawable.ic_user);
            ivProfileImage.setImageUrl(SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_PROIMG, ""), imageLoader);
            tvName.setText(userInfo.optString("firstName") + " " + userInfo.optString("lastName"));
            tvMobile.setText(userInfo.optString("mobile"));
            if (userInfo.optString("mobile").equalsIgnoreCase("")) {
                tvVerified.setVisibility(View.GONE);
                tvMobile.setText("Add Mobile No.");
//                tvMobile.setVisibility(View.GONE);
            } else
                tvMobile.setText(userInfo.optString("mobile"));

            if (userInfo.optString("isMobileVerified").equalsIgnoreCase("1")) {
                tvVerified.setText("Verified");
                tvVerified.setTextColor(getResources().getColor(R.color.black));
                tvVerified.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.verified, 0);
            } else {
                tvVerified.setText("Unverified");
                tvVerified.setTextColor(getResources().getColor(R.color.red));
                tvVerified.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.unverified, 0);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("PROFILE"));
        tabLayout.addTab(tabLayout.newTab().setText("ADDRESS"));
        tabLayout.addTab(tabLayout.newTab().setText("CHANGE PASSWORD"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new ProfilePagerAdapter(getSupportFragmentManager(), this));
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        if (SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_IS_SOCIAL, "").equalsIgnoreCase("facebook") | SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_IS_SOCIAL, "").equalsIgnoreCase("google")) {
            tabLayout.removeTab(tabLayout.getTabAt(2));
        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btActionBarBack:
                finish();
                break;
            case R.id.tVProfileMobileNo:
                startActivity(new Intent(ProfileActivity.this, MobileNoActivity.class).putExtra(Constants.SCREEN, Constants.ScreenCode.SCREEN_LOGIN));
                break;
            case R.id.ivProfileImage:
//                images();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!checkPermission()) {
                        requestPermission();
                    } else {
                        images();
                    }
                } else {
                    images();
                }
                break;
            default:
        }
    }

    public class ProfilePagerAdapter extends FragmentPagerAdapter {

        final int TAB_COUNT = 3;
        private String[] tabtitles = new String[]{"PROFILE", "ADDRESS", "CHANGE PASSWORD"};
        private Context context;

        public ProfilePagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new ProfileFragment();
                case 1:
                    return new AddressFragment();
                case 2:
                    return new ChangePasswordFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return TAB_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            return tabtitles[position];
        }
    }


    private void doUpload() {
        final String tag = "doUpload";
        File file = null;
        file = new File(outputFile);
        CommonUtil.showProgressDialog(this, "Wait...");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("flag", "image");
        params.put("removeImage", "0");
        params.put("userToken", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, ""));
        params.put("eventId", String.valueOf(Constants.Events.EVENT_UPDATE));
        params.put("defaultToken", Constants.DEFAULT_TOKEN);

        Log.i("params", params.toString());
        MultipartRequestJson multipartRequest = new MultipartRequestJson(Constants.BASE_URL + "/mobile/user/update",
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("response", response.optJSONObject("data").optString("imageName"));
                        CommonUtil.cancelProgressDialog();
                        try {
                            if (response.getInt("status") == Constants.STATUS_SUCCESS) {
//                                ivProfileImage.setDefaultImageResId(R.drawable.ic_user);
                                SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_USER_PROIMG, Constants.BASE_URL + "/images/users/" + response.optJSONObject("data").optString("imageName"));
                                SharedPreferenceUtil.save();
                                Log.d("response", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_USER_PROIMG, ""));
                                CommonUtil.alertBox(ProfileActivity.this, "", response.optString("message"));
//                                ivProfileImage.setDefaultImageResId(R.drawable.ic_user);
//                                ivProfileImage.setImageUrl(Constants.BASE_URL + "/images/users/" + userInfo.optString("image"), imageLoader);
//                                final AlertDialog.Builder alert = new AlertDialog.Builder(ProfileActivity.this);
//                                alert.setTitle("Success!");
//                                alert.setMessage(response.optString("message"));
//                                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//
//                                    }
//
//                                });
//                                alert.show();

                            } else {
                                JsonErrorShow.jsonErrorShow(response, ProfileActivity.this);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                CommonUtil.alertBox(ProfileActivity.this, "", getResources().getString(R.string.nointernet_try_again_msg));
                CommonUtil.cancelProgressDialog();

                Log.e("Volley Request Error", error.getLocalizedMessage());

            }

        }, file, params);
        multipartRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        GifkarApp.getInstance().addToRequestQueue(multipartRequest, tag);

    }


    private void doRemove() {
        final String tag = "doRemove";
        File file = null;
        file = new File(outputFile);
        CommonUtil.showProgressDialog(this, "Wait...");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("flag", "image");
        params.put("removeImage", "1");
        params.put("userToken", SharedPreferenceUtil.getString(Constants.PrefKeys.PREF_ACCESS_TOKEN, ""));
        params.put("eventId", String.valueOf(Constants.Events.EVENT_UPDATE_PHOTO_REMOVE));
        params.put("defaultToken", Constants.DEFAULT_TOKEN);
        Log.i("params", params.toString());
        DataRequest loginRequest = new DataRequest(Request.Method.POST, Constants.BASE_URL + "/mobile/user/update", params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("response", response.toString());
                        CommonUtil.cancelProgressDialog();
                        ivProfileImage.setDefaultImageResId(R.drawable.ic_user);
                        SharedPreferenceUtil.remove(Constants.PrefKeys.PREF_USER_PROIMG);
                        SharedPreferenceUtil.save();
                        try {
                            if (response.getInt("status") == Constants.STATUS_SUCCESS) {
                                CommonUtil.alertBox(ProfileActivity.this,"",response.optString("message"));
//                                final AlertDialog.Builder alert = new AlertDialog.Builder(ProfileActivity.this);
//                                alert.setTitle("Success!");
//                                alert.setMessage(response.optString("message"));
//                                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//
//                                    }
//
//                                });
//                                alert.show();

                            } else {
                                JsonErrorShow.jsonErrorShow(response, ProfileActivity.this);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                CommonUtil.cancelProgressDialog();
                CommonUtil.alertBox(ProfileActivity.this, "", getResources().getString(R.string.nointernet_try_again_msg));
                Log.e("Volley Request Error", error.getLocalizedMessage());

            }

        });
        loginRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        GifkarApp.getInstance().addToRequestQueue(loginRequest, tag);

    }

    public void images() {
        final CharSequence[] items = {"Camera", "Gallery", "Remove Image",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Source");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Camera")) {
//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT,
//                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString());
//                    intent.putExtra("crop", "true");
////                    intent.putExtra("aspectX", 0);
////                    intent.putExtra("aspectY", 0);
////                    intent.putExtra("outputX", 100);
////                    intent.putExtra("outputY", 100);
//                    try {
//                        intent.putExtra("return-data", true);
//                        startActivityForResult(intent, REQUEST_CAMERA);
//                    } catch (ActivityNotFoundException e) {
//                        e.printStackTrace();
//                    }
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Gallery")) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_PICK);
                    intent.putExtra("crop", "true");
//                    intent.putExtra("aspectX", 0);
//                    intent.putExtra("aspectY", 0);
//                    intent.putExtra("outputX", 100);
//                    intent.putExtra("outputY", 100);
                    try {
                        intent.putExtra("return-data", true);
                        startActivityForResult(Intent.createChooser(intent,
                                "Complete action using"), SELECT_FILE);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                    }
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                } else {
                    doRemove();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == REQUEST_CAMERA) {
                Bundle extras = data.getExtras();
                if (extras != null) {
//                    Bitmap photo = extras.getParcelable("data");
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    ivProfileImage.setImageBitmap(photo);
                    // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                    Uri tempUri = CommonUtil.getImageUri(getApplicationContext(), photo);

                    // CALL THIS METHOD TO GET THE ACTUAL PATH
                    outputFile = CommonUtil.getRealPathFromURI(tempUri, ProfileActivity.this);
                    doUpload();
                }
            }

            if (requestCode == SELECT_FILE) {
                Bundle extras2 = data.getExtras();
                if (extras2 != null) {
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    ivProfileImage.setImageBitmap(photo);
                    // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                    Uri tempUri = CommonUtil.getImageUri(getApplicationContext(), photo);

                    // CALL THIS METHOD TO GET THE ACTUAL PATH
                    outputFile = CommonUtil.getRealPathFromURI(tempUri, ProfileActivity.this);
                    doUpload();
                }
            }


            if (requestCode == SELECT_FILE) {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;

        } else {

            return false;

        }
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {

            showMessageOKCancel("You need to allow access to Read Storage",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                                        PERMISSION_REQUEST_CODE);
                            }
                        }
                    });

        } else {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    images();
                } else {
                    Toast.makeText(this, "Permission Denied, You cannot access storage data.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

}
