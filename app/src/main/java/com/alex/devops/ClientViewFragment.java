package com.alex.devops;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alex.devops.db.Client;
import com.alex.devops.utils.Utils;
import com.example.test.app.jacob.mygalleryapp.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ClientViewFragment extends Fragment implements
        View.OnClickListener,
        DatePickerDialog.OnDateSetListener {
    private static final int CLIENT_PHOTO_REQUEST_CODE = 777;
    public static final String TAG = ClientViewFragment.class.getName();

    private ImageView mPhotoImageView;
    private EditText mFirstNameEditText;
    private EditText mSecondNameEditText;
    private EditText mPhoneNumberEditText;
    private EditText mPatronymicNameEditText;
    private boolean mIsPhotoSet;
    private Bitmap mBitmap;
    private TextView mBirthDateTextView;
    private long mBirthDate;
    private File mPhotoPath;

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
        initViews(view);
    }


    public void initViews(View view) {
        mPhotoImageView = (ImageView) view.findViewById(R.id.client_photo_image_view);
        mFirstNameEditText = (EditText) view.findViewById(R.id.first_name_edit_text);
        mSecondNameEditText = (EditText) view.findViewById(R.id.second_name_edit_text);
        mPhoneNumberEditText = (EditText) view.findViewById(R.id.phone_number_edit_text);
        mPatronymicNameEditText = (EditText) view.findViewById(R.id.patronymic_name_edit_text);
        mBirthDateTextView = (TextView) view.findViewById(R.id.birth_day_text_view);
        mBirthDateTextView.setOnClickListener(this);
        mPhotoImageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.client_photo_image_view: {
                takePhoto();
                break;
            }
            case R.id.birth_day_text_view: {
                callDataPicker();
                break;
            }
        }
    }

    @Override
    public void onDetach() {
        if (mPhotoImageView != null) {
            mPhotoImageView.setImageDrawable(null);
        }
        super.onDetach();
    }

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mPhotoPath = Utils.createImageFile(getContext());
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPhotoPath));
        if (Utils.isResolved(intent, getActivity())) {
            startActivityForResult(intent, CLIENT_PHOTO_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CLIENT_PHOTO_REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK) {
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
        mBirthDate = calendar.getTime().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        mBirthDateTextView.setText(simpleDateFormat.format(mBirthDate));
    }

    public Client getClient() {
        String firstName = getText(mFirstNameEditText);
        String secondName = getText(mSecondNameEditText);
        String phoneNumber = getText(mPhoneNumberEditText);
        String patronymicName = getText(mPatronymicNameEditText);

        Client client = new Client();
        client.setFirstName(firstName);
        client.setSecondName(secondName);
        client.setPatronymicName(patronymicName);
        client.setPhoneNumber(phoneNumber);
        client.setBirthDay(mBirthDate);
        client.setPhotoPath(mPhotoPath.getAbsolutePath());
        client.setBitmap(mBitmap);
        return client;
    }

    public boolean isDataValid() {
        boolean sName = validateTextField(mSecondNameEditText, 3);
        if (!sName) {
            Snackbar.make(getView(), R.string.second_name_is_mandatory_to_fill_in, Snackbar.LENGTH_LONG).show();
            return false;
        }

        boolean fName = validateTextField(mFirstNameEditText, 3);
        if (!fName) {
            Snackbar.make(getView(), R.string.first_name_is_mandatory_to_fill_in, Snackbar.LENGTH_LONG).show();
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
}
