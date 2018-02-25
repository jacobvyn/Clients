package com.alex.devops.views;

import android.app.DatePickerDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.alex.devops.R;
import com.alex.devops.db.Client;
import com.alex.devops.db.Parent;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ClientViewFragment extends Fragment implements
        View.OnClickListener,
        DatePickerDialog.OnDateSetListener {
    public static final String TAG = ClientViewFragment.class.getName();

    private TextView mChildBirthDateTextView;
    private EditText mChildFirstNameEditText;
    private long mChildBirthDate;
    private ParentViewFragment mSecondParent;
    private ParentViewFragment mMainParent;
    private FloatingActionButton mAddParentButton;
    private boolean mIsAddParent = true;

    public static ClientViewFragment newInstance() {
        return new ClientViewFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.client_view_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mAddParentButton = (FloatingActionButton) view.findViewById(R.id.add_second_parent_fab);
        mAddParentButton.setOnClickListener(this);
        initChildViews(view);
        mMainParent = ParentViewFragment.newInstance();
        getChildFragmentManager()
                .beginTransaction()
                .add(R.id.main_parent_root_container, mMainParent)
                .commit();

        addFreeSpace(true);
    }

    private void addFreeSpace(final boolean add) {
        mAddParentButton.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (add) {
                    mMainParent.addFreeSpace();
                } else {
                    mMainParent.removeFreeSpace();
                }
            }
        }, 100);
    }

    private void initChildViews(View view) {
        mChildFirstNameEditText = (EditText) view.findViewById(R.id.child_first_name_edit_text);
        mChildBirthDateTextView = (TextView) view.findViewById(R.id.birth_day_text_view);
        mChildBirthDateTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.birth_day_text_view: {
                callDataPicker();
                break;
            }
            case R.id.add_second_parent_fab: {
                onAddParentClicked();
                break;
            }
        }
    }

    private void onAddParentClicked() {
        if (mIsAddParent) {
            addSecondParentFragment();
            addFreeSpace(false);
        } else {
            removeSecondParentFragment();
            addFreeSpace(true);
        }
    }

    private void addSecondParentFragment() {
        mSecondParent = ParentViewFragment.newInstance();
        getChildFragmentManager()
                .beginTransaction()
                .add(R.id.second_parent_root_container, mSecondParent)
                .commit();
        mChildFirstNameEditText.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSecondParent.enableSeparator();
            }
        }, 50);
        Drawable remove = getActivity().getDrawable(R.drawable.remove_parent_icon);
        mAddParentButton.setBackground(remove);
        mIsAddParent = false;
    }

    private void removeSecondParentFragment() {
        getChildFragmentManager()
                .beginTransaction()
                .remove(mSecondParent)
                .commit();
        mAddParentButton.setBackground(getActivity().getDrawable(R.drawable.add_parent_icon));
        mSecondParent = null;
        mIsAddParent = true;
    }

    private boolean validateTextField(EditText editText, int length) {
        return editText != null && editText.getText().toString().length() >= length;
    }

    private String getText(EditText editText) {
        if (editText != null) {
            return editText.getText().toString();
        } else {
            return "";
        }
    }

    private void callDataPicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), this, 2014, 1, 1);
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        mChildBirthDate = calendar.getTime().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        mChildBirthDateTextView.setText(simpleDateFormat.format(mChildBirthDate));
    }

    public Client getClient() {
        Parent mainParent = mMainParent.getParent();

        String childFirstName = getText(mChildFirstNameEditText);

        Client client = new Client();
        client.setChildName(childFirstName);
        client.setChildBirthDay(mChildBirthDate);
        client.setMainParent(mainParent);

        if (mSecondParent != null) {
            Parent secondParent = mSecondParent.getParent();
            client.setSecondParent(secondParent);
        }

        return client;
    }

    public boolean checkInputData() {
        boolean childName = validateTextField(mChildFirstNameEditText, 3);
        if (!childName) {
            Snackbar.make(getView(), R.string.child_first_name_is_mandatory_to_fill_in, Snackbar.LENGTH_LONG).show();
            return false;
        }

        if (mMainParent != null && !mMainParent.checkInputData()) {
            return false;
        }

        if (mSecondParent != null && !mSecondParent.checkInputData()) {
            return false;
        }
        return true;
    }
}
