package com.smartsense.gifkar;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class ProfileFragment extends Fragment implements View.OnClickListener {
    EditText etLastName, etFirstName, etDob;
    Button btnProfileUpdate;
    private RadioButton rbMale,rbFemale;
    private RadioGroup rbGroup;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.fragment_profile, container, false);


        etLastName = (EditText) view.findViewById(R.id.etProfileArea);
        etFirstName = (EditText) view.findViewById(R.id.etProfileCity);
        etDob = (EditText) view.findViewById(R.id.etProfilePinCode);
        rbGroup = (RadioGroup) view.findViewById(R.id.rbProfileGroup);
        rbMale = (RadioButton) view.findViewById(R.id.rbProfileMale);
        rbFemale = (RadioButton) view.findViewById(R.id.rbProfileFemale);
        btnProfileUpdate = (Button) view.findViewById(R.id.btnProfileUpdate);
        btnProfileUpdate.setOnClickListener(this);

        return  view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnProfileUpdate:

                break;
            default:
        }
    }
}
