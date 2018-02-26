package com.alex.devops.views;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.alex.devops.R;
import com.alex.devops.db.Parent;
import com.alex.devops.utils.Utils;

/**
 * Created by vynnykiakiv on 2/25/18.
 */

public class ParentViewFragment extends Fragment implements View.OnClickListener {
    private static final int PARENT_PHOTO_REQUEST_CODE = 777;

    private ImageView mPhotoImageView;
    private EditText mNameEditText;
    private EditText mSurnameEditText;
    private EditText mPhoneNumberEditText;
    private EditText mPatronymicNameEditText;
    private Bitmap mBitmap;
    private boolean mIsPhotoSet;
    private View mRootView;

    public static ParentViewFragment newInstance() {
        return new ParentViewFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.parent_view_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mRootView = view;
        mPhotoImageView = (ImageView) view.findViewById(R.id.parent_photo_image_view);
        mNameEditText = (EditText) view.findViewById(R.id.parent_name_edit_text);
        mSurnameEditText = (EditText) view.findViewById(R.id.surname_edit_text);
        mPhoneNumberEditText = (EditText) view.findViewById(R.id.phone_number_edit_text);
        mPatronymicNameEditText = (EditText) view.findViewById(R.id.patronymic_name_edit_text);
        mPhotoImageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.parent_photo_image_view: {
                takePhoto();
                break;
            }
        }
    }

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Utils.isResolved(intent, getActivity())) {
            startActivityForResult(intent, PARENT_PHOTO_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PARENT_PHOTO_REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK) {
            handlePhoto(data.getExtras());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void handlePhoto(Bundle extras) {
        mBitmap = (Bitmap) extras.get("data");
        mIsPhotoSet = true;
        if (mPhotoImageView != null) {
            mPhotoImageView.setImageBitmap(mBitmap);
        }
    }

    @Override
    public void onDetach() {
        if (mPhotoImageView != null) {
            mPhotoImageView.setImageDrawable(null);
        }
        super.onDetach();
    }

    private String getText(EditText editText) {
        if (editText != null) {
            return editText.getText().toString();
        } else {
            return "";
        }
    }

    public boolean checkInputData() {
        boolean parentName = validateTextField(mNameEditText, 3);
        if (!parentName) {
            Snackbar.make(getView(), R.string.name_is_mandatory_to_fill_in, Snackbar.LENGTH_LONG).show();
            return false;
        }

        boolean sName = validateTextField(mSurnameEditText, 3);
        if (!sName) {
            Snackbar.make(getView(), R.string.surname_is_mandatory_to_fill_in, Snackbar.LENGTH_LONG).show();
            return false;
        }

        boolean phone = validateTextField(mPhoneNumberEditText, 10);
        if (!phone) {
            Snackbar.make(getView(), R.string.phone_number_is_to_short, Snackbar.LENGTH_LONG).show();
            return false;
        }

        if (!mIsPhotoSet) {
            Snackbar.make(getView(), R.string.photo_is_mandatory, Snackbar.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean validateTextField(EditText editText, int length) {
        return editText != null && editText.getText().toString().length() >= length;
    }

    public void enableSeparator() {
        if (mRootView != null) {
            mRootView.findViewById(R.id.separator).setVisibility(View.VISIBLE);
        }
        addFreeSpace();
    }

    public void addFreeSpace() {
        if (mRootView != null) {
            mRootView.findViewById(R.id.free_space_view).setVisibility(View.VISIBLE);
        }
    }

    public Parent getParent() {
        String name = getText(mNameEditText);
        String surname = getText(mSurnameEditText);
        String patronymicName = getText(mPatronymicNameEditText);
        String phoneNumber = getText(mPhoneNumberEditText);

        Parent parent = new Parent();
        parent.setName(name);
        parent.setSurname(surname);
        parent.setPatronymicName(patronymicName);
        parent.setPhoneNumber(phoneNumber);
        parent.setPhotoBlob(Utils.getBytes(mBitmap));

        return parent;
    }

    public void removeFreeSpace() {
        if (mRootView != null) {
            mRootView.findViewById(R.id.free_space_view).setVisibility(View.GONE);
        }
    }
}
