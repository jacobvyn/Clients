package com.alex.devops.views;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.alex.devops.R;
import com.alex.devops.db.Human;
import com.alex.devops.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ChildFragment extends BaseFragment implements
        DatePickerDialog.OnDateSetListener {
    private Unbinder mUnBinder;
    @BindView(R.id.child_first_name_edit_text)
    protected EditText mChildFirstNameEditText;
    @BindView((R.id.birth_day_text_view))
    protected TextView mChildBirthDateTextView;
    private long mChildBirthDate;


    public static ChildFragment newInstance() {
        return new ChildFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_child_view_layout, container, false);
        mUnBinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @OnClick(R.id.birth_day_text_view)
    public void callDataPicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), this, 2014, 1, 1);
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        mChildBirthDate = Utils.getTime(year, month, dayOfMonth);
        mChildBirthDateTextView.setText(Utils.getFormattedDate(mChildBirthDate));
    }

    public Human getChild() {
        String childFirstName = getTextFrom(mChildFirstNameEditText);
        Human child = new Human();
        child.setFirstName(childFirstName);
        child.setBirthDay(mChildBirthDate);
        return child;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnBinder.unbind();
    }

    @Override
    public boolean checkInputData() {
        boolean name = validateTextField(mChildFirstNameEditText, 3);
        if (!name) {
            Snackbar.make(getView(), R.string.child_first_name_is_mandatory_to_fill_in, Snackbar.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}
