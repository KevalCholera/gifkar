package com.smartsense.gifkar;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

public class SignupFragment extends Fragment {
    EditText etFullName,etMobileNo,etEmail,etPass,etConPass,etCountryCode;
    Button btSignUp;
    TextView tvSkip,tvTerms;
    RadioButton rbTerms;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.fragment_signup, container, false);
        etFullName = (EditText) view.findViewById(R.id.etSignUpFullName);
        etMobileNo = (EditText) view.findViewById(R.id.etSignUpMobileNo);
        etEmail = (EditText) view.findViewById(R.id.etSignUpEmailId);
        etPass = (EditText) view.findViewById(R.id.etSignUpPassword);
        etConPass = (EditText) view.findViewById(R.id.etSignUpConfirmPassword);
        etCountryCode = (EditText) view.findViewById(R.id.etSignUpCountryCode);
        btSignUp=(Button) view.findViewById(R.id.btnSignup);
        tvSkip=(TextView) view.findViewById(R.id.tvSignupSkip);
        tvTerms=(TextView) view.findViewById(R.id.tvSignupTerms);
        rbTerms=(RadioButton) view.findViewById(R.id.rbSignupTerms);
        return view;
    }
}
