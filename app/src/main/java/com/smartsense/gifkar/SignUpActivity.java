package com.smartsense.gifkar;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.mpt.storage.SharedPreferenceUtil;
import com.smartsense.gifkar.utill.CommonUtil;
import com.smartsense.gifkar.utill.Constants;
import com.smartsense.gifkar.utill.DataRequest;
import com.smartsense.gifkar.utill.JsonErrorShow;
import com.smartsense.gifkar.utill.MultipartRequestJson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
//import android.util.Log;

public class SignUpActivity extends AppCompatActivity implements Response.Listener<JSONObject>,
        Response.ErrorListener {
    TextInputLayout emailWrapper;
    TextInputLayout passwordWrapper;
    TextInputLayout nameWrapper;
    TextInputLayout repasswordWrapper;
    TextInputLayout mnoWrapper;
    TextInputLayout refer_code;
    String isEmailVerified = "0";
    String isMnoVerified = "0";

    ImageButton iv_close_register;
    com.smartsense.gifkar.utill.CircleImageView1 iv_profile1_Reg;
    Button signup;

    ImageLoader imgLoader = GifkarApp.getInstance().getDiskImageLoader();
    private static final int REQUEST_CAMERA = 0;
    private final int SELECT_FILE = 1;
    File dir = null;
    private String outputFile = "";
    private ProgressBar progressBaremail, progressBarmno;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DeliveryJunction");
        if (!dir.exists()) {
            dir.mkdir();
        }
//        outputFile = "android.resource://com.smartsense.deliveryjunction/" + R.drawable.profile_screen_icon;

//        iv_profile_Reg=new CircleImageView(this);

        iv_close_register = (ImageButton) findViewById(R.id.iv_close_register);


        //progresbar

        progressBaremail = (ProgressBar) findViewById(R.id.progressBar1);
        progressBarmno = (ProgressBar) findViewById(R.id.progressBar2);
        progressBaremail.setVisibility(View.INVISIBLE);
        progressBarmno.setVisibility(View.INVISIBLE);

        iv_profile1_Reg = (com.smartsense.gifkar.utill.CircleImageView1) findViewById(R.id.iv_profile1_Reg);


        emailWrapper = (TextInputLayout) findViewById(R.id.ti_emailWrapper_Reg);
        final EditText signupemail = (EditText) findViewById(R.id.etSignupEmailId);

        signupemail.setOnFocusChangeListener(new View.OnFocusChangeListener() {

                                                 public void onFocusChange(View v, boolean hasFocus) {
                                                     if (!hasFocus) {
                                                         // code to execute when EditText loses focus
                                                         final String emailid = signupemail.getText().toString();
                                                         if (!CommonUtil.isValidEmail(emailid)) {
                                                             emailWrapper.getEditText().setError(getString(R.string.wrn_email));
                                                         } else if (TextUtils.isEmpty(emailid)) {
                                                             passwordWrapper.getEditText().setError(getString(R.string.wrn_pwd));
                                                         } else {
                                                             progressBaremail.setVisibility(View.VISIBLE);
                                                             isEmailVerified = "email";
                                                             new CheckAvailable().execute(emailid);

                                                         }
                                                     } else {
                                                         if (isEmailVerified.equals("1")) {
                                                             emailWrapper.getEditText().setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                                                             isEmailVerified = "0";
                                                             signup.setEnabled(false);
                                                         }
                                                     }
                                                 }
                                             }

        );
        final EditText signupmno = (EditText) findViewById(R.id.etSignupMobileNo);
        signupmno.setOnFocusChangeListener(new View.OnFocusChangeListener() {

                                               public void onFocusChange(View v, boolean hasFocus) {
                                                   if (!hasFocus) {
                                                       // code to execute when EditText loses focus
                                                       final String mno = signupmno.getText().toString();
                                                       if (mno.length() != 10) {
                                                           mnoWrapper.getEditText().setError("Check Phone Number Format");
                                                       } else if (TextUtils.isEmpty(mno)) {
                                                           mnoWrapper.getEditText().setError(getString(R.string.wrn_mno));
                                                       } else {
                                                           progressBarmno.setVisibility(View.VISIBLE);
                                                           isMnoVerified = "mno";
                                                           new CheckAvailable().execute(mno);

                                                       }
                                                   } else {
                                                       if (isMnoVerified.equals("1")) {
                                                           mnoWrapper.getEditText().setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                                                           isMnoVerified = "0";
                                                           signup.setEnabled(false);
                                                       }
                                                   }
                                               }
                                           }

        );

        passwordWrapper = (TextInputLayout) findViewById(R.id.ti_passWrapper_Reg);

        nameWrapper = (TextInputLayout) findViewById(R.id.ti_NameWrapper_Reg);

        repasswordWrapper = (TextInputLayout) findViewById(R.id.ti_repassWrapper_Reg);

        mnoWrapper = (TextInputLayout) findViewById(R.id.ti_MnoWrapper_Reg);

        refer_code = (TextInputLayout) findViewById(R.id.ti_referCodeWrapper_Reg);

        signup = (Button) findViewById(R.id.btnSignup);
        signup.setEnabled(false);


        signup.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                CommonUtil.closeKeyboard(SignUpActivity.this);
                String emailId = emailWrapper.getEditText().getText().toString().trim();
                String password = passwordWrapper.getEditText().getText().toString().trim();
                String rePassword = repasswordWrapper.getEditText().getText().toString().trim();
                String fullname = nameWrapper.getEditText().getText().toString().trim();
                String mobileNum = mnoWrapper.getEditText().getText().toString().trim();
                String referCode = refer_code.getEditText().getText().toString().trim();
                if (!CommonUtil.isValidEmail(emailId)) {
                    emailWrapper.getEditText().setError(getString(R.string.wrn_email));

                } else if (TextUtils.isEmpty(password)) {
                    passwordWrapper.getEditText().setError(getString(R.string.wrn_pwd));
                } else if (TextUtils.isEmpty(rePassword)) {
                    repasswordWrapper.getEditText().setError(getString(R.string.wrn_repwd));
                } else if (!TextUtils.equals(password, rePassword)) {
                    repasswordWrapper.getEditText().setError(getString(R.string.wrn_match));
                } else if (TextUtils.isEmpty(fullname)) {
                    nameWrapper.getEditText().setError(getString(R.string.wrn_name));
                } else if (TextUtils.isEmpty(mobileNum)) {
                    mnoWrapper.getEditText().setError(getString(R.string.wrn_mno));
                } else if (mobileNum.length() != 10) {
                    mnoWrapper.getEditText().setError(getString(R.string.wrn_valid_mno));
                }
//                else if (TextUtils.isEmpty(referCode)) {
//                    refer_code.getEditText().setError(getString(R.string.wrn_rco));
//                }
                else {
                    try {
                        if (outputFile.equalsIgnoreCase("")) {

                            doSignup1(URLEncoder.encode(fullname, "utf-8"), password, mobileNum, emailId, referCode);
                        } else {
                            doSignup(URLEncoder.encode(fullname, "utf-8"), password, mobileNum, emailId, referCode);
                        }

                    } catch (UnsupportedEncodingException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

            }
        });
        iv_profile1_Reg.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                images();
            }
        });


        iv_close_register.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                finish();
            }
        });


    }


    public class CheckAvailable extends AsyncTask<String, Void, Void> {

        //The variables you need to set go here

        @Override
        protected Void doInBackground(String... params) {
            // Do your loading here. Don't touch any views from here, and then return null
//                            String emailid = signupemail.getText().toString();
            String input = params[0];
            getemailstatus(input);
            return null;
        }


        @Override
        protected void onPostExecute(final Void result) {
            // Update your views here
            progressBaremail.setVisibility(View.GONE);
            progressBarmno.setVisibility(View.GONE);
        }

    }

    private void getemailstatus(String emailid) {

        final String tag = "checkuser";
        String url = Constants.BASE_URL + "checkuser";
        Map<String, String> params = new HashMap<String, String>();
        params.put("email_id", emailid);
//        CommonUtil.showProgressDialog(this, "Wait...");
        params.put("event_id", String.valueOf(Constants.Events.EVENT_EMAIL_CHECK));
        DataRequest loginRequest = new DataRequest(Request.Method.POST, url, params, this, this);
        loginRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        GifkarApp.getInstance().addToRequestQueue(loginRequest, tag);

    }

    private void doSignup1(String name, String password, String mno, String email, String refer) {
        final String tag = "doSignup1";
        String url = Constants.BASE_URL + "registerAPI";
        Map<String, String> params = new HashMap<String, String>();
        params.put("name", name);
        params.put("password", password);
        params.put("mobile_no", mno);
        params.put("email_Id", email);
        params.put("refercode", refer);
        params.put("event_id", String.valueOf(Constants.Events.EVENT_SIGNUP));
        CommonUtil.showProgressDialog(this, "Wait...");
        DataRequest loginRequest = new DataRequest(Request.Method.POST, url, params, this, this);
        loginRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        GifkarApp.getInstance().addToRequestQueue(loginRequest, tag);

    }

    private void doSignup(String name, String password, String mno, String email, String refer) {
        final String tag = "doUpload";
        File file = null;
        file = new File(outputFile);
        CommonUtil.showProgressDialog(this, "Wait...");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("name", name);
        params.put("password", password);
        params.put("mobile_no", mno);
        params.put("email_Id", email);
        params.put("refercode", refer);
        params.put("event_id", String.valueOf(Constants.Events.EVENT_SIGNUP));
        Log.i("params", params.toString());
        MultipartRequestJson multipartRequest = new MultipartRequestJson(Constants.BASE_URL + "registerAPI",
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("response", response.toString());
                        CommonUtil.cancelProgressDialog();
                        try {
                            if (response.getString("status_code").equalsIgnoreCase(Constants.STATUS_SUCCESS)) {
                                SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_USER_ID, response.getJSONArray("data").getJSONObject(0).getString("id"));
                                SharedPreferenceUtil.save();
                                final Builder alert = new AlertDialog.Builder(SignUpActivity.this);
                                alert.setTitle("Success!");
                                alert.setMessage(response.optString("message"));
                                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        finish();
                                    }

                                });
                                alert.show();

                            } else {
                                JsonErrorShow.jsonErrorShow(response, SignUpActivity.this);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                CommonUtil.cancelProgressDialog();
                Log.e("Volley Request Error", error.getLocalizedMessage());

            }

        }, file, params);
        multipartRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        GifkarApp.getInstance().addToRequestQueue(multipartRequest, tag);

    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
//        CommonUtil.alertBox(getApplicationContext(), "", getResources().getString(R.string.nointernet_try_again_msg));
//        CommonUtil.cancelProgressDialog();
        CommonUtil.cancelProgressDialog();
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Network Problem!");
        alert.setMessage(getResources().getString(R.string.internet_error));
        alert.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String emailId = emailWrapper.getEditText().getText().toString().trim();
                String password = passwordWrapper.getEditText().getText().toString().trim();
                String rePassword = repasswordWrapper.getEditText().getText().toString().trim();
                String fullname = nameWrapper.getEditText().getText().toString().trim();
                String mobileNum = mnoWrapper.getEditText().getText().toString().trim();
                String referCode = refer_code.getEditText().getText().toString().trim();
                try {
                    if (outputFile.equalsIgnoreCase("")) {

                        doSignup1(URLEncoder.encode(fullname, "utf-8"), password, mobileNum, emailId, referCode);
                    } else {
                        doSignup(URLEncoder.encode(fullname, "utf-8"), password, mobileNum, emailId, referCode);
                    }

                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        });
        alert.show();
    }

    @Override
    public void onResponse(JSONObject response) {
        CommonUtil.cancelProgressDialog();

        if (response != null) {
            try {

                if (response.getString("status_code").equalsIgnoreCase(Constants.STATUS_SUCCESS)) {

                    switch (Integer.valueOf(response.getString("event_id"))) {

                        case Constants.Events.EVENT_EMAIL_CHECK:
                            String flag = response.getJSONObject("data").getString("flag");
                            Log.d("flag-----", flag);
                            //assign global check for signup approved

                            if (isEmailVerified.contains("email"))
                                isEmailVerified = isEmailVerified + flag;
                            else
                                isMnoVerified = isMnoVerified + flag;
                            emailresults(flag);
                            break;

                        case Constants.Events.EVENT_SIGNUP:
                            SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_USER_ID, response.getJSONArray("data").getJSONObject(0).getString("user_id"));
                            SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_USER_MNO, response.getJSONArray("data").getJSONObject(0).getString("mobile_no"));
                            SharedPreferenceUtil.save();
                            final Builder alert = new AlertDialog.Builder(this);
                            alert.setTitle("Success!");
                            alert.setMessage(response.getString("message"));
                            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    startActivity(new Intent(SignUpActivity.this, OTPActivity.class).putExtra("flag", true));
                                    finish();
                                }

                            });
                            alert.show();
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

    private void emailresults(String flag) {
        if (isEmailVerified.contains("email")) {

            // for email verify
            if (flag.equals("1")) {
                //User not available
//                emailWrapper.getEditText().setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_email_approved, 0);
                if (isMnoVerified.contains("1")) {
                    signup.setEnabled(true);
                }
            } else {
                //useravailable
                emailWrapper.getEditText().setError(getString(R.string.user_available));
                emailWrapper.getEditText().requestFocus();
            }
            isEmailVerified = "1";
        } else {
            // for mobile verify
            if (flag.equals("1")) {
                //User not available
//                mnoWrapper.getEditText().setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_email_approved, 0);
                if (isEmailVerified.contains("1")) {
                    signup.setEnabled(true);
                }
            } else {
                //useravailable
                mnoWrapper.getEditText().setError(getString(R.string.user_available));
                mnoWrapper.getEditText().requestFocus();
            }
            isMnoVerified="1";
        }
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
                    iv_profile1_Reg.setImageBitmap(photo);
                    // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                    Uri tempUri = CommonUtil.getImageUri(getApplicationContext(), photo);

                    // CALL THIS METHOD TO GET THE ACTUAL PATH
                    outputFile = CommonUtil.getRealPathFromURI(tempUri, SignUpActivity.this);
                }
            }

            if (requestCode == SELECT_FILE) {
                Bundle extras2 = data.getExtras();
                if (extras2 != null) {
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    iv_profile1_Reg.setImageBitmap(photo);
                    // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                    Uri tempUri = CommonUtil.getImageUri(getApplicationContext(), photo);

                    // CALL THIS METHOD TO GET THE ACTUAL PATH
                    outputFile = CommonUtil.getRealPathFromURI(tempUri, SignUpActivity.this);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void images() {
        final CharSequence[] items = {"Camera", "Gallery",
                "Cancel"};
        Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Source");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Camera")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString());
                    intent.putExtra("crop", "true");
                    intent.putExtra("aspectX", 0);
                    intent.putExtra("aspectY", 0);
                    intent.putExtra("outputX", 100);
                    intent.putExtra("outputY", 100);
                    try {
                        intent.putExtra("return-data", true);
                        startActivityForResult(intent, REQUEST_CAMERA);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                    }
                } else if (items[item].equals("Gallery")) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
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
                }
            }
        });
        builder.show();
    }


}