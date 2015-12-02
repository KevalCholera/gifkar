package com.smartsense.gifkar;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.smartsense.gifkar.adapter.CountryCodeAdapter;

import org.json.JSONObject;

public class SignupFragment extends Fragment implements View.OnClickListener {
    EditText etFirstName, etLastName, etMobileNo, etEmail, etPass, etConPass, etCountryCode;
    Button btSignUp;
    TextView tvSkip, tvTerms;
    CheckBox cbTerms;
    AlertDialog alert;

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
        etCountryCode.setOnClickListener(this);
        btSignUp = (Button) view.findViewById(R.id.btnSignup);
        btSignUp.setOnClickListener(this);
        tvSkip = (TextView) view.findViewById(R.id.tvSignupSkip);
        tvTerms = (TextView) view.findViewById(R.id.tvSignupTerms);
        tvTerms.setOnClickListener(this);
        cbTerms = (CheckBox) view.findViewById(R.id.rbSignupTerms);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSignup:
                startActivity(new Intent(getActivity(), OTPActivity.class));
                break;
            case R.id.etSignUpCountryCode:
                openCountryPopup();
                break;
            case R.id.tvSignupTerms:
                startActivity(new Intent(getActivity(), TermsandCondtionsActivity.class));
                break;
            default:
        }
    }

    public void openCountryPopup() {
        String tempary = "{\n" +
                "\t\"eventId\": 123,\n" +
                "\t\"errorCode\": 0,\n" +
                "\t\"status\": 200,\n" +
                "\t\"message\": \"country list.\",\n" +
                "\t\"data\": {\n" +
                "\t\t\"countries\": [{\n" +
                "\t\t\t\"id\": \"18\",\n" +
                "\t\t\t\"name\": \"India\",\n" +
                "\t\t\t\"code\": \"+92\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"id\": \"19\",\n" +
                "\t\t\t\"name\": \"Pakistan\",\n" +
                "\t\t\t\"code\": \"+93\"\n" +
                "\t\t}]\n" +
                "\t}\n" +
                "}";
        try {
            JSONObject response = new JSONObject(tempary);
            final AlertDialog.Builder alertDialogs = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View dialog = inflater.inflate(R.layout.list_view, null);
            ListView list_view = (ListView) dialog.findViewById(R.id.list_view);

            CountryCodeAdapter countryCodeAdapter = new CountryCodeAdapter(getActivity(), response.getJSONObject("data").getJSONArray("countries"), true);
            list_view.setAdapter(countryCodeAdapter);

            list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(final AdapterView<?> adapterView, View view, final int position, long index) {

                    JSONObject getCodeObj = (JSONObject) adapterView.getItemAtPosition(position);
                    etCountryCode.setText(getCodeObj.optString("code"));
                    alert.dismiss();

                }
            });
            alertDialogs.setView(dialog);
            alertDialogs.setCancelable(false);
            alert = alertDialogs.create();
            alert.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
