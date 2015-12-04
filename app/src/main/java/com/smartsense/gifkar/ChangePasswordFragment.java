package com.smartsense.gifkar;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class ChangePasswordFragment extends Fragment implements View.OnClickListener {
    EditText etOldPass, etConPass, etNewPass;
    Button btChangePass;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.activity_change_password, container, false);


        etOldPass = (EditText) view.findViewById(R.id.etChangePassoldPassword);
        etConPass = (EditText) view.findViewById(R.id.etChangePassConfirmPassword);
        etNewPass = (EditText) view.findViewById(R.id.etChangePassNewPassword);
        btChangePass = (Button) view.findViewById(R.id.btnChangePass);
        btChangePass.setOnClickListener(this);

        return  view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnChangePass:

                break;
            default:
        }
    }
}
