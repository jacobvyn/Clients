package com.alex.devops.db;

import android.graphics.Bitmap;

/**
 * Created by vynnykiakiv on 2/25/18.
 */

public class Parent {
    private String mFirstName;
    private String mSecondName;
    private String mPatronymicName;
    private String mPhoneNumber;
    private String mPhotoPath;
    private byte[] mPhotoBlob;

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String mFirstName) {
        this.mFirstName = mFirstName;
    }

    public String getSecondName() {
        return mSecondName;
    }

    public void setSecondName(String mSecondName) {
        this.mSecondName = mSecondName;
    }

    public String getPatronymicName() {
        return mPatronymicName;
    }

    public void setPatronymicName(String mPatronymicName) {
        this.mPatronymicName = mPatronymicName;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneNumber(String mPhoneNumber) {
        this.mPhoneNumber = mPhoneNumber;
    }

    public String getPhotoPath() {
        return mPhotoPath;
    }

    public void setPhotoPath(String mPhotoPath) {
        this.mPhotoPath = mPhotoPath;
    }

    public byte[] getPhotoBlob() {
        return mPhotoBlob;
    }

    public void setPhotoBlob(byte[] mPhotoBlob) {
        this.mPhotoBlob = mPhotoBlob;
    }
}
