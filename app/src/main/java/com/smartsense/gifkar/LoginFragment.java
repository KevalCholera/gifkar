package com.smartsense.gifkar;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.mpt.storage.SharedPreferenceUtil;
import com.parse.ParseInstallation;
import com.smartsense.gifkar.utill.CommonUtil;
import com.smartsense.gifkar.utill.Constants;
import com.smartsense.gifkar.utill.DataRequest;
import com.smartsense.gifkar.utill.JsonErrorShow;

import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class LoginFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, Response.Listener<JSONObject>, Response.ErrorListener, View.OnClickListener {
    private static final int PERMISSION_REQUEST_CODE = 1;

    public static CallbackManager callbackManager;

    private Button btnLogin;

    private static final String TAG = "GplusLogin";

    private static final int STATE_DEFAULT = 0;
    private static final int STATE_SIGN_IN = 1;
    private static final int STATE_IN_PROGRESS = 2;
    TextView tvLoginForgotPwd;

    public static final int RC_SIGN_IN = 0;

    /* Client used to interact with Google APIs. */
    public static GoogleApiClient mGoogleApiClient;

    /* Is there a ConnectionResult resolution in progress? */
    public static boolean mIsResolving = false;

    /* Should we automatically resolve ConnectionResults when possible? */
    public static boolean mShouldResolve = false;

    private Button mSignInButton;
    private TextView mStatus;
    Fragment fragment = this;
    EditText etInputemail;
    EditText etInputPassword;
    TextView tvSkip;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.fragment_login, container, false);
        // TODO Auto-generated method stub

        FacebookSdk.sdkInitialize(getActivity());
        callbackManager = CallbackManager.Factory.create();
//        printHashKey(getActivity());
        // Input Email and Password field
        etInputemail = (EditText) view.findViewById(R.id.etLoginEmailId);
        etInputPassword = (EditText) view.findViewById(R.id.etLoginPassword);
        // Forgot Password
        tvLoginForgotPwd = (TextView) view.findViewById(R.id.tvLoginForgotPwd);
        tvLoginForgotPwd.setOnClickListener(this);
        tvLoginForgotPwd.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN || event.getAction() == EditorInfo.IME_ACTION_DONE || event.getAction() == KeyEvent.KEYCODE_ENTER) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    doLogin(etInputemail.getText().toString(), etInputPassword.getText().toString());
                    return true;
                }
                return false;
            }
        });
        //skip
        tvSkip = (TextView) view.findViewById(R.id.tvLoginSkip);
        tvSkip.setOnClickListener(this);
        // Login Button
        btnLogin = (Button) view.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
        //Google Sign in button
        mSignInButton = (Button) view.findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(this);
//
        Button loginButton = (Button) view.findViewById(R.id.fb_login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Login with facebook on clicking custom button
                LoginManager.getInstance().logInWithReadPermissions(getActivity(), Arrays.asList("public_profile", "email"));
            }
        });
        LoginManager.getInstance().registerCallback(callbackManager, callback);
//        doTest();
        return view;
    }

    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            Log.d("All requested data", "success");
            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject me, GraphResponse response) {

                            Log.d("All requested data", me.toString());
                            if (response.getError() != null) {
                                // handle error
                            } else {
                                String email = me.optString("email");
                                Log.d("All requested data", me.toString());
                                try {
                                    String accessToken = AccessToken.getCurrentAccessToken().getToken();
                                    doLoginwithSocial("facebook", accessToken);
                                    Log.d("accessToken", accessToken);
                                    // Logout immediately after fetching data
                                    LoginManager.getInstance().logOut();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
//                                    Intent i = new Intent(this, SignUpActivity.class);
//                                    startActivity(new Intent(this, SignUpActivity.class).putExtra("json", me.toString()));
                            }
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name, email, birthday,gender,first_name,last_name");
            request.setParameters(parameters);
            request.executeAsync();

        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException e) {

        }
    };


    public void doLogin(String emailId, String password) {
        if (TextUtils.isEmpty(emailId)) {
            etInputemail.setError(getString(R.string.wrn_em_mo));
        } else if (TextUtils.isEmpty(password)) {
            etInputPassword.setError(getString(R.string.wrn_pwd));
        } else if (password.length() < 6) {
            etInputPassword.setError(getString(R.string.wrn_pwd_len));
        } else {
            if (!CommonUtil.isInternet(getActivity()))
                CommonUtil.alertBox(getActivity(), "Error", getResources().getString(R.string.nointernet_try_again_msg));
            else {
                final String tag = "login";
                String url = Constants.BASE_URL + "/mobile/user/login";
                Map<String, String> params = new HashMap<String, String>();
                params.put("eventId", String.valueOf(Constants.Events.EVENT_LOGIN));
                params.put("defaultToken", Constants.DEFAULT_TOKEN);
                params.put("username", emailId);
                params.put("password", password);
                CommonUtil.showProgressDialog(getActivity(), "Wait...");
                Log.d("login Params", params.toString());
                DataRequest loginRequest = new DataRequest(Request.Method.POST, url, params, this, this);
                loginRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                GifkarApp.getInstance().addToRequestQueue(loginRequest, tag);
            }
        }
    }


    private GoogleApiClient buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(new Scope(Scopes.PROFILE))
                .addScope(new Scope(Scopes.EMAIL))
                .build();
        return mGoogleApiClient;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult:" + requestCode + ":" + resultCode + ":" + data);
        Log.d("All requested data", "Result");
        if (requestCode == RC_SIGN_IN) {
            // If the error resolution was not successful we should not resolve further.
            if (resultCode != Activity.RESULT_OK) {
                mShouldResolve = false;
            }
            mIsResolving = false;
            mGoogleApiClient.connect();
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);

        }
    }


    // onConnected for Google Plus Login
    @Override
    public void onConnected(Bundle bundle) {

        Log.i(TAG, "onConnected");
        mShouldResolve = false;

//        Person currentUser = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
        final String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
//
//        String personPhoto = currentUser.getImage().getUrl();
//        String urlImage = personPhoto.substring(0, personPhoto.lastIndexOf("=") + 1) + "200";
//
//        //Toast.makeText(this,"log In",Toast.LENGTH_LONG).show();
//        String username = currentUser.getDisplayName();
//        Log.d(TAG, currentUser.getDisplayName());
//        Log.d(TAG, email + urlImage);


//        final String SCOPES = "https://www.googleapis.com/auth/plus.login ";
//        final String SCOPES = "https://www.googleapis.com/auth/userinfo.email";

        new GetIdTokenTask().execute(email);
//        new GetIdTokenTask().execute();
    }

    @Override
    public void onClick(View view) {
        String emailId = etInputemail.getText().toString();
        switch (view.getId()) {
            case R.id.tvLoginForgotPwd:
                startActivity(new Intent(getActivity(), ForgotPasswordActivity.class));
                break;
            case R.id.btnLogin:
                String password = etInputPassword.getText().toString().trim();
                doLogin(emailId, password);
                break;
            case R.id.sign_in_button:
                if (!CommonUtil.isInternet(getActivity()))
                    CommonUtil.alertBox(getActivity(), "Error", getResources().getString(R.string.nointernet_try_again_msg));
                else {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (!checkPermission()) {
                            requestPermission();
                        } else {
                            mGoogleApiClient = buildGoogleApiClient();
                            mShouldResolve = true;
                            mGoogleApiClient.connect();
                        }
                    } else {
                        mGoogleApiClient = buildGoogleApiClient();
                        mShouldResolve = true;
                        mGoogleApiClient.connect();
                    }

                }
                break;
            case R.id.tvLoginSkip:
                if (SharedPreferenceUtil.contains(Constants.PrefKeys.PREF_AREA_PIN_CODE))
                    startActivity(new Intent(getActivity(), GifkarActivity.class));
                else
                    startActivity(new Intent(getActivity(), CitySelectActivity.class));
            default:
        }

    }

    //below class is for Get Id token for after Google login
    private class GetIdTokenTask extends AsyncTask<String, Void, String> {
        String email, username;

        @Override
        protected String doInBackground(String... params) {
            email = params[0];
//            username = params[1];
            String scopes = "oauth2:profile email";
            //            String scopes = "audience:server:client_id:" + SERVER_CLIENT_ID; // Not the app's client ID.
            try {
                return GoogleAuthUtil.getToken(getActivity(), email, scopes);
            } catch (IOException e) {
                Log.e(TAG, "Error retrieving ID token.", e);
                return null;
            } catch (GoogleAuthException e) {
                Log.e(TAG, "Error retrieving ID token.", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Log.i(TAG, "ID token: " + result);
            if (result != null) {
                // Successfully retrieved ID Token
                googlePlusLogout();
//                doSignup1(username, Constants.GOOGLE, email, result, email);
                doLoginwithSocial("google", result);
//            public void doSignup1(String name, String flag, String authenticatedId, String token) {

            } else {
                Log.e(TAG, "Error retrieving ID token.");
                // There was some error getting the ID Token
                // ...
            }
        }

    }


    private void googlePlusLogout() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // Could not connect to Google Play Services.  The user needs to select an account,
        // grant permissions or resolve an error in order to sign in. Refer to the javadoc for
        // ConnectionResult to see possible error codes.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);

        if (!mIsResolving && mShouldResolve) {
            if (connectionResult.hasResolution()) {
                try {
                    connectionResult.startResolutionForResult(getActivity(), RC_SIGN_IN);
                    mIsResolving = true;
                } catch (IntentSender.SendIntentException e) {
                    Log.e(TAG, "Could not resolve ConnectionResult.", e);
                    mIsResolving = false;
                    mGoogleApiClient.connect();
                }
            } else {
                // Could not resolve the connection result, show the user an
                // error dialog.
//                showErrorDialog(connectionResult);
            }
        } else {
            // Show the signed-out UI
//            showSignedOutUI();
        }
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        CommonUtil.alertBox(getActivity(), "", getResources().getString(R.string.nointernet_try_again_msg));
        CommonUtil.cancelProgressDialog();
    }

    @Override
    public void onResponse(JSONObject response) {
        CommonUtil.cancelProgressDialog();
        if (response != null) {
            try {
                if (response.getInt("status") == Constants.STATUS_SUCCESS) {
                    switch (Integer.valueOf(response.getString("eventId"))) {
                        case Constants.Events.EVENT_LOGIN:
                            etInputemail.setText("");
                            etInputPassword.setText("");

                            if (response.getJSONObject("data").has("isVerified")) {
                                SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_USER_ID, response.getJSONObject("data").getString("userId"));
                                SharedPreferenceUtil.save();
                                startActivity(new Intent(getActivity(), MobileNoActivity.class).putExtra(Constants.SCREEN, Constants.ScreenCode.SCREEN_LOGIN));
                            } else {
                                ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                                installation.put("UserID", response.getJSONObject("data").getString("userId"));
//                                installation.addAllUnique("channels", Arrays.asList("test"));
                                installation.saveInBackground();
                                SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_USER_ID, response.getJSONObject("data").getString("userId"));
                                SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_ACCESS_TOKEN, response.getJSONObject("data").getString("userToken"));
                                SharedPreferenceUtil.save();
                                if (SharedPreferenceUtil.contains(Constants.PrefKeys.PREF_AREA_PIN_CODE))
                                    startActivity(new Intent(getActivity(), GifkarActivity.class));
                                else
                                    startActivity(new Intent(getActivity(), CitySelectActivity.class));
                                getActivity().finish();
                            }
                            break;
                        case Constants.Events.EVENT_SIGNUP:
                            ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                            installation.put("UserID", response.getJSONObject("data").getString("userId"));
//                                installation.addAllUnique("channels", Arrays.asList("test"));
                            installation.saveInBackground();
                            SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_ACCESS_TOKEN, response.getJSONObject("data").getString("userToken"));
                            SharedPreferenceUtil.save();
                            if (SharedPreferenceUtil.contains(Constants.PrefKeys.PREF_AREA_PIN_CODE))
                                startActivity(new Intent(getActivity(), GifkarActivity.class));
                            else
                                startActivity(new Intent(getActivity(), CitySelectActivity.class));
                            getActivity().finish();
                            break;
                    }
                } else {
                    JsonErrorShow.jsonErrorShow(response, getActivity());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void doLoginwithSocial(String socialMediaType, String token) {
        final String tag = "doLoginwithSocial";
        String url = Constants.BASE_URL + "/mobile/user/loginBySocialMedia";
        Map<String, String> params = new HashMap<String, String>();
        params.put("socialMediaType", socialMediaType);
        params.put("accessToken", token);
        params.put("eventId", String.valueOf(Constants.Events.EVENT_SIGNUP));
        params.put("defaultToken", Constants.DEFAULT_TOKEN);
        CommonUtil.showProgressDialog(getActivity(), "Wait...");
        Log.d("params", params.toString());
        DataRequest loginRequest = new DataRequest(Request.Method.POST, url, params, this, this);
        loginRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        GifkarApp.getInstance().addToRequestQueue(loginRequest, tag);
    }

    public void doTest() {
        final String tag = "test";
        String url = "https://gifkar.cloudapp.net/mobile/test";
        DataRequest loginRequest = new DataRequest(Request.Method.POST, url, null, this, this);
        loginRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        GifkarApp.getInstance().addToRequestQueue(loginRequest, tag);

    }


    public static void printHashKey(Context pContext) {
        try {
            PackageInfo info = pContext.getPackageManager().getPackageInfo(pContext.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i(TAG, "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "printHashKey()", e);
        } catch (Exception e) {
            Log.e(TAG, "printHashKey()", e);
        }
    }


    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.GET_ACCOUNTS);
        if (result == PackageManager.PERMISSION_GRANTED) {

            return true;

        } else {

            return false;

        }
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.GET_ACCOUNTS)) {

            showMessageOKCancel("You need to allow access to Get Account",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                requestPermissions(new String[]{android.Manifest.permission.GET_ACCOUNTS},
                                        PERMISSION_REQUEST_CODE);
                            }
                        }
                    });

        } else {

            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.GET_ACCOUNTS}, PERMISSION_REQUEST_CODE);
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getActivity())
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
                    mGoogleApiClient = buildGoogleApiClient();
                    mShouldResolve = true;
                    mGoogleApiClient.connect();
                } else {
                    Toast.makeText(getActivity(), "Permission Denied, You cannot access get account.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }


}