package com.smartsense.gifkar;

import android.content.Intent;
import android.content.IntentSender;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.mpt.storage.SharedPreferenceUtil;
import com.smartsense.gifkar.utill.CommonUtil;
import com.smartsense.gifkar.utill.Constants;
import com.smartsense.gifkar.utill.DataRequest;
import com.smartsense.gifkar.utill.JsonErrorShow;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, Response.Listener<JSONObject>, Response.ErrorListener, View.OnClickListener {

//    TextInputLayout usernameWrapper;
//    TextInputLayout passwordWrapper;

    private CallbackManager callbackManager;
    private LoginButton fbLoginButton;
    ImageButton iv_close_login;

    private Button btnLogin, btnSignup;

    private static final String TAG = "GplusLogin";

    private static final int STATE_DEFAULT = 0;
    private static final int STATE_SIGN_IN = 1;
    private static final int STATE_IN_PROGRESS = 2;
    TextView tvLoginForgotPwd;

    private static final int RC_SIGN_IN = 0;

    /* Client used to interact with Google APIs. */
    private GoogleApiClient mGoogleApiClient;

    /* Is there a ConnectionResult resolution in progress? */
    private boolean mIsResolving = false;

    /* Should we automatically resolve ConnectionResults when possible? */
    private boolean mShouldResolve = false;

    private SignInButton mSignInButton;
    private TextView mStatus;
    TextInputLayout usernameWrapper;
    TextInputLayout passwordWrapper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_login);

        iv_close_login = (ImageButton) findViewById(R.id.iv_close_login);
        iv_close_login.setOnClickListener(this);


        usernameWrapper = (TextInputLayout) findViewById(R.id.ti_usernameWrapper_Login);
        passwordWrapper = (TextInputLayout) findViewById(R.id.ti_passwordWrapper_Login);

        tvLoginForgotPwd = (TextView) findViewById(R.id.tvLoginForgotPwd);
        tvLoginForgotPwd.setOnClickListener(this);
        btnSignup = (Button) findViewById(R.id.btnSignup);
        btnSignup.setOnClickListener(this);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
        mSignInButton = (SignInButton) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(this);


        fbLoginButton = (LoginButton) findViewById(R.id.fb_login_button);
        fbLoginButton.setReadPermissions("public_profile");
        fbLoginButton.setReadPermissions("email");
//        fbLoginButton.setReadPermissions(Arrays.asList("email", "user_photos", "public_profile"));
        fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                final String token = loginResult.getAccessToken().getToken();
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject me, GraphResponse response) {
                                if (response.getError() != null) {
                                    // handle error
                                } else {
                                    String email = me.optString("email");
                                    System.out.println(me);
                                    try {
                                        String accessToken = AccessToken.getCurrentAccessToken().getToken();
                                        Log.d("accessToken", accessToken);
//                                        if (me.has("email"))
                                        doSignup1(me.getString("name"), Constants.FB, me.getString("id"), token, me.optString("email"));
//                                        else
//                                            doSignup1(me.getString("name"), Constants.FB, me.getString("id"), token, "");
                                        LoginManager.getInstance().logOut();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
//                                    Intent i = new Intent(this, SignUpActivity.class);
//                                    startActivity(new Intent(LoginActivity.this, SignUpActivity.class).putExtra("json", me.toString()));
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters
                        .putString("fields",
                                "id,name, email, birthday,gender,first_name,last_name");
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "Login cancelled by user!", Toast.LENGTH_LONG).show();
                System.out.println("Facebook Login failed!!");

            }

            @Override
            public void onError(FacebookException e) {
                Toast.makeText(getApplicationContext(), "Login unsuccessful!", Toast.LENGTH_LONG).show();
                System.out.println("Facebook Login failed!!");
            }


        });
    }


    public void doLogin(String email, String passsword) {
        final String tag = "login";
        String url = Constants.BASE_URL + "login";
        Map<String, String> params = new HashMap<String, String>();
        params.put("event_id", String.valueOf(Constants.Events.EVENT_LOGIN));
        params.put("email_id", email);
        params.put("password", passsword);
        CommonUtil.showProgressDialog(this, "Wait...");
        Log.d("login Params", params.toString());
        DataRequest loginRequest = new DataRequest(Request.Method.POST, url, params, this, this);
        loginRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        GifkarApp.getInstance().addToRequestQueue(loginRequest, tag);

    }

    public void doForgot(String email) {
        final String tag = "forgotpassword";
        String url = Constants.BASE_URL + "forgotpassword";
        Map<String, String> params = new HashMap<String, String>();
        params.put("event_id", String.valueOf(Constants.Events.EVENT_FORGOT_PASS));
        params.put("email_id", email);
        CommonUtil.showProgressDialog(this, "Wait...");
        DataRequest loginRequest = new DataRequest(Request.Method.POST, url, params, this, this);
        loginRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        GifkarApp.getInstance().addToRequestQueue(loginRequest, tag);

    }


    private GoogleApiClient buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
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

        if (requestCode == RC_SIGN_IN) {
            // If the error resolution was not successful we should not resolve further.
            if (resultCode != RESULT_OK) {
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

        Person currentUser = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
        final String email = Plus.AccountApi.getAccountName(mGoogleApiClient);

        String personPhoto = currentUser.getImage().getUrl();
        String urlImage = personPhoto.substring(0, personPhoto.lastIndexOf("=") + 1) + "200";

        //Toast.makeText(this,"log In",Toast.LENGTH_LONG).show();
        String username = currentUser.getDisplayName();
        Log.d(TAG, currentUser.getDisplayName());
        Log.d(TAG, email + urlImage);

        String token = null;
//        final String SCOPES = "https://www.googleapis.com/auth/plus.login ";
        final String SCOPES = "https://www.googleapis.com/auth/userinfo.email";

//        try {
//            token = GoogleAuthUtil.getToken(getApplicationContext(), email, "oauth2:" + SCOPES);
//            Log.d("Access Token", token);
//        } catch (IOException | GoogleAuthException e) {
//            e.printStackTrace();
//        }

        new GetIdTokenTask().execute(email, username);
//        googlePlusLogout();
//
//        doSignup1(currentUser.getDisplayName(), Constants.GOOGLE, email, token);
//        Intent i = new Intent(this, SignUpActivity.class).putExtra("json", "").putExtra("name", currentUser.getDisplayName()).putExtra("email", email).putExtra("url", urlImage);
//        startActivity(i);

//        finish();
    }

    @Override
    public void onClick(View view) {
        String emailId = usernameWrapper.getEditText().getText().toString().trim();
        switch (view.getId()) {
            case R.id.iv_close_login:
                finish();
                break;
            case R.id.tvLoginForgotPwd:
                CommonUtil.closeKeyboard(LoginActivity.this);
                if (!CommonUtil.isInternet(this))
                    CommonUtil.alertBox(this, "Error", getResources().getString(R.string.nointernet_try_again_msg));
                else {
                    if (!CommonUtil.isValidEmail(emailId)) {
                        usernameWrapper.getEditText().setError(getString(R.string.wrn_email));
                    } else if (TextUtils.isEmpty(emailId)) {
                        passwordWrapper.getEditText().setError(getString(R.string.wrn_em));
                    } else {
                        try {
                            doForgot(emailId);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            case R.id.btnSignup:
                startActivity(new Intent(getBaseContext(), SignUpActivity.class));
                finish();
                break;
            case R.id.btnLogin:
                CommonUtil.showProgressDialog(this, "Wait...");
                CommonUtil.closeKeyboard(LoginActivity.this);

                String password = passwordWrapper.getEditText().getText().toString().trim();
                if (!CommonUtil.isValidEmail(emailId)) {
                    usernameWrapper.getEditText().setError(getString(R.string.wrn_email));
                } else if (TextUtils.isEmpty(emailId)) {
                    passwordWrapper.getEditText().setError(getString(R.string.wrn_em));
                } else if (TextUtils.isEmpty(password)) {
                    passwordWrapper.getEditText().setError(getString(R.string.wrn_pwd));
                } else if (password.length() < 6) {
                    passwordWrapper.getEditText().setError(getString(R.string.wrn_pwd_len));
                } else {
                    try {
                        if (!CommonUtil.isInternet(this))
                            CommonUtil.alertBox(this, "Error", getResources().getString(R.string.nointernet_try_again_msg));
                        else
                            doLogin(emailId, password);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.sign_in_button:
                if (!CommonUtil.isInternet(this))
                    CommonUtil.alertBox(this, "Error", getResources().getString(R.string.nointernet_try_again_msg));
                else {
                    mGoogleApiClient = buildGoogleApiClient();
                    mShouldResolve = true;
                    mGoogleApiClient.connect();
                }
                break;
//            case R.id.iv_close_login:
//                break;
            default:
        }

    }

    //below class is for Get Id token for after Google login
    private class GetIdTokenTask extends AsyncTask<String, Void, String> {
        String email, username;

        @Override
        protected String doInBackground(String... params) {
            email = params[0];
            username = params[1];
            String scopes = "oauth2:profile email";
            //            String scopes = "audience:server:client_id:" + SERVER_CLIENT_ID; // Not the app's client ID.
            try {
                return GoogleAuthUtil.getToken(getApplicationContext(), email, scopes);
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
                doSignup1(username, Constants.GOOGLE, email, result, email);
//            public void doSignup1(String name, String flag, String authenticatedId, String token) {

            } else {
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
                    connectionResult.startResolutionForResult(this, RC_SIGN_IN);
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

    }

    @Override
    public void onResponse(JSONObject response) {
        CommonUtil.cancelProgressDialog();
        if (response != null) {
            try {
                if (response.getString("status_code").equalsIgnoreCase(Constants.STATUS_SUCCESS)) {
                    switch (Integer.valueOf(response.getString("event_id"))) {
                        case Constants.Events.EVENT_LOGIN:
                            if (response.getJSONArray("data").getJSONObject(0).getString("is_verify").equals("0")) {
                                SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_USER_ID, response.getJSONArray("data").getJSONObject(0).getString("user_id"));
                                SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_USER_MNO, response.getJSONArray("data").getJSONObject(0).getString("mobile_no"));

                                SharedPreferenceUtil.save();
                                startActivity(new Intent(getBaseContext(), OTPActivity.class).putExtra("flag", false));
                                finish();
                            } else {
                                loginResponse(response);
                            }
                            break;
                        case Constants.Events.EVENT_FORGOT_PASS:
                            CommonUtil.alertBox(LoginActivity.this, "Success", response.optString("message"));
                            break;
                        case Constants.Events.EVENT_SIGNUP:
                            if (response.getJSONArray("data").getJSONObject(0).getString("is_verify").equals("0")) {
                                SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_USER_ID, response.getJSONArray("data").getJSONObject(0).getString("user_id"));
                                SharedPreferenceUtil.remove(Constants.PrefKeys.PREF_USER_MNO);
                                SharedPreferenceUtil.save();
                                startActivity(new Intent(getBaseContext(), OTPActivity.class).putExtra("flag", false));
                                finish();
                            } else {
                                loginResponse(response);
                            }
//                            CommonUtil.alertBox(LoginActivity.this, "Success", response.optString("message"));
                            break;
                    }
                } else {
                    JsonErrorShow.jsonErrorShow(response, this);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void loginResponse(JSONObject response) {
        try {
            SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_USER_ID, response.getJSONArray("data").getJSONObject(0).getString("id"));
            SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_USER_EMAIL, response.getJSONArray("data").getJSONObject(0).getString("email"));
            SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_USER_MNO, response.getJSONArray("data").getJSONObject(0).getString("mobile"));
            SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_USER_FULLNAME, response.getJSONArray("data").getJSONObject(0).getString("name"));
            SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_REFER_CODE, response.getJSONArray("data").getJSONObject(0).getString("referCode"));
            SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_USER_PROIMG, response.getJSONArray("data").getJSONObject(0).getString("profileimage"));
            SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_USER_REFER, response.getJSONArray("data").getJSONObject(0).getString("hasrefered"));
            SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_REFER_MSG, response.getJSONArray("data").getJSONObject(0).getString("referMessage"));
            SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_USER_PASS, response.getJSONArray("data").getJSONObject(0).getString("password"));
            SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_DJ_WALLET, response.getJSONArray("data").getJSONObject(0).getString("wallet_amount"));
            SharedPreferenceUtil.putValue(Constants.PrefKeys.PREF_ACCESS_TOKEN, response.getString("token"));
            SharedPreferenceUtil.save();
            if (getIntent().getBooleanExtra("check", true))
//                startActivity(new Intent(getBaseContext(), DeliveryJunction.class));
                finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doSignup1(String name, String flag, String authenticatedId, String token, String email) {
        final String tag = "doSignup1";
        String url = Constants.BASE_URL + "authenticateUser";
        Map<String, String> params = new HashMap<String, String>();
        params.put("name", name);
        params.put("flag", flag);
        params.put("authenticatedId", authenticatedId);
        params.put("email", email);
        params.put("token", token);
        params.put("event_id", String.valueOf(Constants.Events.EVENT_SIGNUP));
        CommonUtil.showProgressDialog(this, "Wait...");
        Log.d("params", params.toString());
        DataRequest loginRequest = new DataRequest(Request.Method.POST, url, params, this, this);
        loginRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        GifkarApp.getInstance().addToRequestQueue(loginRequest, tag);

    }

}