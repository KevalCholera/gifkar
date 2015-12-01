//package com.smartsense.gifkar;
//
///**
// * Created by smartSense on 02-11-2015.
// */
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//
//import com.facebook.AccessToken;
//import com.facebook.AccessTokenTracker;
//import com.facebook.CallbackManager;
//import com.facebook.FacebookCallback;
//import com.facebook.FacebookException;
//import com.facebook.FacebookSdk;
//import com.facebook.GraphRequest;
//import com.facebook.GraphResponse;
//import com.facebook.Profile;
//import com.facebook.ProfileTracker;
//import com.facebook.login.LoginManager;
//import com.facebook.login.LoginResult;
//
//import org.json.JSONObject;
//
//import java.util.Arrays;
//
//
///**
// * A placeholder fragment containing a simple view.
// */
//public class FacebookLogin extends Fragment {
//
//    private CallbackManager callbackManager;
////    private TextView textView;
//
//    private AccessTokenTracker accessTokenTracker;
//    private ProfileTracker profileTracker;
//
//    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
//        @Override
//        public void onSuccess(LoginResult loginResult) {
//            AccessToken accessToken = loginResult.getAccessToken();
//            Profile profile = Profile.getCurrentProfile();
////            LoginManager.getInstance().logOut();
////            displayMessage(profile);
//            GraphRequest request = GraphRequest.newMeRequest(
//                    loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
//                        @Override
//                        public void onCompleted(JSONObject me, GraphResponse response) {
//                            Log.d("All requested data", "yes");
//                            if (response.getError() != null) {
//                                // handle error
//                            } else {
//                                String email = me.optString("email");
//                                Log.d("All requested data", me.toString());
//                                try {
//                                    String accessToken = AccessToken.getCurrentAccessToken().getToken();
//                                    Log.d("accessToken", accessToken);
//                                    // Logout immediately after fetching data
////                                    LoginManager.getInstance().logOut();
//
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
////                                    Intent i = new Intent(this, SignUpActivity.class);
////                                    startActivity(new Intent(this, SignUpActivity.class).putExtra("json", me.toString()));
//                            }
//                        }
//                    });
//            Bundle parameters = new Bundle();
//            parameters.putString("fields", "id,name, email, birthday,gender,first_name,last_name");
//            request.setParameters(parameters);
//            request.executeAsync();
//
//        }
//
//        @Override
//        public void onCancel() {
//
//        }
//
//        @Override
//        public void onError(FacebookException e) {
//
//        }
//    };
//
//    public FacebookLogin() {
//
//    }
//
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
//
//        callbackManager = CallbackManager.Factory.create();
//
//        accessTokenTracker = new AccessTokenTracker() {
//            @Override
//            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {
//
//            }
//        };
//
//        profileTracker = new ProfileTracker() {
//            @Override
//            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
//                displayMessage(newProfile);
//            }
//        };
//
//        accessTokenTracker.startTracking();
//        profileTracker.startTracking();
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fb_button, container, false);
//    }
//
//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        Button loginButton = (Button) view.findViewById(R.id.fb_login_button);
//
////        loginButton.setReadPermissions("public_profile");
////        loginButton.setReadPermissions("email");
//        loginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //Login with facebook on clicking custom button
//                LoginManager.getInstance().logInWithReadPermissions(getActivity(), Arrays.asList("public_profile", "email"));
//            }
//        });
//
////        textView = (TextView) view.findViewById(R.id.textView);
//
////        loginButton.setReadPermissions("user_friends");
////        ((LoginButton) loginButton).setFragment(this);
//        LoginManager.getInstance().registerCallback(callbackManager, callback);
//
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        callbackManager.onActivityResult(requestCode, resultCode, data);
//        Log.d("All requested data", "onActivityResult");
//
//    }
//
//    private void displayMessage(Profile profile) {
//        if (profile != null) {
////            textView.setText(profile.getName());
//        }
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        accessTokenTracker.stopTracking();
//        profileTracker.stopTracking();
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        Profile profile = Profile.getCurrentProfile();
//        displayMessage(profile);
//    }
//}