package com.alex.devops.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

@Entity(tableName = "parents")
public class Parent implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int mId;

    @ColumnInfo(name = "name")
    @NonNull
    private String mName;

    @ColumnInfo(name = "sure_name")
    private String mSurname;

    @ColumnInfo(name = "patronymic_name")
    private String mPatronymicName;

    @ColumnInfo(name = "phone_number")
    private String mPhoneNumber;

    @ColumnInfo(name = "photo_blob")
    private byte[] mPhotoBlob;

    @ColumnInfo(name = "time_stamp")
    private long mTimeStamp;

    public Parent() {
        mTimeStamp = System.currentTimeMillis();
    }

    protected Parent(Parcel in) {
        mId = in.readInt();
        mName = in.readString();
        mSurname = in.readString();
        mPatronymicName = in.readString();
        mPhoneNumber = in.readString();
        mPhotoBlob = in.createByteArray();
        mTimeStamp = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mName);
        dest.writeString(mSurname);
        dest.writeString(mPatronymicName);
        dest.writeString(mPhoneNumber);
        dest.writeByteArray(mPhotoBlob);
        dest.writeLong(mTimeStamp);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Parent> CREATOR = new Creator<Parent>() {
        @Override
        public Parent createFromParcel(Parcel in) {
            return new Parent(in);
        }

        @Override
        public Parent[] newArray(int size) {
            return new Parent[size];
        }
    };

    public String getName() {
        return mName;
    }

    public void setName(String mFirstName) {
        this.mName = mFirstName;
    }

    public String getSurname() {
        return mSurname;
    }

    public void setSurname(String mSecondName) {
        this.mSurname = mSecondName;
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

    public long getTimeStamp() {
        return mTimeStamp;
    }

    public void setTimeStamp(long mTimeStamp) {
        this.mTimeStamp = mTimeStamp;
    }

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }
}
