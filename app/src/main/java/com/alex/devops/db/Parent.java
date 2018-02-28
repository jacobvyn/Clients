package com.alex.devops.db;

public class Parent extends Human {
    private String mSecondName;
    private String mPatronymicName;
    private String mPhoneNumber;
    private byte[] mPhotoBlob;

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

    public byte[] getPhotoBlob() {
        return mPhotoBlob;
    }

    public void setPhotoBlob(byte[] mPhotoBlob) {
        this.mPhotoBlob = mPhotoBlob;
    }
}
