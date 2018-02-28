package com.alex.devops.views;

import android.support.v4.app.Fragment;
import android.widget.EditText;


public abstract class BaseFragment extends Fragment {
    public abstract boolean checkInputData();

    public String getTextFrom(EditText editText) {
        if (editText != null) {
            return editText.getText().toString();
        } else {
            return "";
        }
    }

    public boolean validateTextField(EditText editText, int length) {
        return editText != null && editText.getText().toString().length() >= length;
    }
}
