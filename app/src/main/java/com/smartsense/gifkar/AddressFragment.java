package com.smartsense.gifkar;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class AddressFragment extends Fragment implements View.OnClickListener {
    EditText etArea, etCity, etPincode,etStreet,etFlatNo,etCountry;
    Button btnProfileAddAddress;
    ImageView btBack;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.fragment_profile_address, container, false);


        etArea = (EditText) view.findViewById(R.id.etProfileArea);
        etCity = (EditText) view.findViewById(R.id.etProfileCity);
        etPincode = (EditText) view.findViewById(R.id.etProfilePinCode);
        etStreet = (EditText) view.findViewById(R.id.etProfileStreet);
        etFlatNo = (EditText) view.findViewById(R.id.etProfileFlatNo);
        etCountry = (EditText) view.findViewById(R.id.etProfileCountry);
        btnProfileAddAddress = (Button) view.findViewById(R.id.btnProfileAddAddress);
        btnProfileAddAddress.setOnClickListener(this);

        return  view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnProfileAddAddress:

                break;
            default:
        }
    }
}
