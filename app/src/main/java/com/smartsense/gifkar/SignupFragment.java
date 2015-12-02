package com.smartsense.gifkar;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class SignupFragment extends Fragment implements View.OnClickListener{
    EditText etFirstName,etLastName,etMobileNo,etEmail,etPass,etConPass,etCountryCode;
    Button btSignUp;
    TextView tvSkip,tvTerms;
    CheckBox cbTerms;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.fragment_signup, container, false);
        etFirstName = (EditText) view.findViewById(R.id.etSignUpFirstName);
        etLastName = (EditText) view.findViewById(R.id.etSignUpLastName);
        etMobileNo = (EditText) view.findViewById(R.id.etSignUpMobileNo);
        etEmail = (EditText) view.findViewById(R.id.etSignUpEmailId);
        etPass = (EditText) view.findViewById(R.id.etSignUpPassword);
        etConPass = (EditText) view.findViewById(R.id.etSignUpConfirmPassword);
        etCountryCode = (EditText) view.findViewById(R.id.etSignUpCountryCode);
        btSignUp=(Button) view.findViewById(R.id.btnSignup);
        btSignUp.setOnClickListener(this);
        tvSkip=(TextView) view.findViewById(R.id.tvSignupSkip);
        tvTerms=(TextView) view.findViewById(R.id.tvSignupTerms);
        cbTerms=(CheckBox) view.findViewById(R.id.rbSignupTerms);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSignup:
                startActivity(new Intent(getActivity(), OTPActivity.class));
                break;
            default:
        }
    }
}
