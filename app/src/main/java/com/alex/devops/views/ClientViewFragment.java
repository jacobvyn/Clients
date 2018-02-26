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
import com.alex.devops.db.Child;
import com.alex.devops.db.Client;
import com.alex.devops.db.Parent;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ClientViewFragment extends Fragment implements
        View.OnClickListener,
        DatePickerDialog.OnDateSetListener {
    public static final String TAG = ClientViewFragment.class.getName();

    private TextView mChildBirthDateTextView;
    private EditText mChildNameEditText;
    private long mChildBirthDate;
    private ParentViewFragment mSecondaryParent;
    private ParentViewFragment mPrimaryParent;
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
        mPrimaryParent = ParentViewFragment.newInstance();
        getChildFragmentManager()
                .beginTransaction()
                .add(R.id.primary_parent_root_container, mPrimaryParent)
                .commit();

        addFreeSpace(true);
    }

    private void addFreeSpace(final boolean add) {
        mAddParentButton.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (add) {
                    mPrimaryParent.addFreeSpace();
                } else {
                    mPrimaryParent.removeFreeSpace();
                }
            }
        }, 100);
    }

    private void initChildViews(View view) {
        mChildNameEditText = (EditText) view.findViewById(R.id.child_name_edit_text);
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
            addSecondaryParentFragment();
            addFreeSpace(false);
        } else {
            removeSecondParentFragment();
            addFreeSpace(true);
        }
    }

    private void addSecondaryParentFragment() {
        mSecondaryParent = ParentViewFragment.newInstance();
        getChildFragmentManager()
                .beginTransaction()
                .add(R.id.secondary_parent_root_container, mSecondaryParent)
                .commit();
        mChildNameEditText.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSecondaryParent.enableSeparator();
            }
        }, 50);
        Drawable remove = getActivity().getDrawable(R.drawable.remove_parent_icon);
        mAddParentButton.setBackground(remove);
        mIsAddParent = false;
    }

    private void removeSecondParentFragment() {
        getChildFragmentManager()
                .beginTransaction()
                .remove(mSecondaryParent)
                .commit();
        mAddParentButton.setBackground(getActivity().getDrawable(R.drawable.add_parent_icon));
        mSecondaryParent = null;
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

        String childFirstName = getText(mChildNameEditText);
        Child child = new Child();
        child.setName(childFirstName);
        child.setBirthDay(mChildBirthDate);

        Parent primaryParent = mPrimaryParent.getParent();

// TODO: 2/26/18
        Client client = new Client();
        client.setPrimaryParentId(primaryParent);

        if (mSecondaryParent != null) {
            Parent secondaryParent = mSecondaryParent.getParent();
            client.setSecondParent(secondaryParent);
        }

        return client;
    }

    public boolean checkInputData() {
        boolean childName = validateTextField(mChildNameEditText, 3);
        if (!childName) {
            Snackbar.make(getView(), R.string.child_first_name_is_mandatory_to_fill_in, Snackbar.LENGTH_LONG).show();
            return false;
        }

        if (mPrimaryParent != null && !mPrimaryParent.checkInputData()) {
            return false;
        }

        if (mSecondaryParent != null && !mSecondaryParent.checkInputData()) {
            return false;
        }
        return true;
    }
}
