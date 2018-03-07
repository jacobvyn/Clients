package com.alex.devops.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.alex.devops.utils.Utils;

@Entity(tableName = "clients")
public class Client implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int mId;

    @ColumnInfo(name = "create_date")
    private long mCreateDate;

    @ColumnInfo(name = "last_visit")
    private long mLastVisit;

    @ColumnInfo(name = "visit_counter")
    private int mVisitCounter;

    @ColumnInfo(name = "main_parent_first_name")
    @NonNull
    private String mMainParentFirstName = "";

    @ColumnInfo(name = "main_second_name")
    @NonNull
    private String mMainSecondName = "";

    @ColumnInfo(name = "main_patronymic_name")
    private String mMainPatronymicName;

    @ColumnInfo(name = "main_phone_number")
    @NonNull
    private String mMainPhoneNumber;

    @ColumnInfo(name = "main_photo_blob")
    @NonNull
    private byte[] mMainBlobPhoto = new byte[0];

    //--------
    @ColumnInfo(name = "child_birth_day")
    private long mChildBirthDay;

    @ColumnInfo(name = "child_first_name")
    private String mChildName;

    //--------

    @ColumnInfo(name = "second_parent_first_name")
    private String mSecondParentFirstName = "";

    @ColumnInfo(name = "second_parent_second_name")
    private String mSecondParentSecondName = "";

    @ColumnInfo(name = "second_patronymic_name")
    private String mSecondPatronymicName;

    @ColumnInfo(name = "second_phone_number")
    private String mSecondPhoneNumber;

    @ColumnInfo(name = "second_photo_blob")
    private byte[] mSecondPhotoBlob = new byte[0];

    public Client() {
        mCreateDate = System.currentTimeMillis();
        mLastVisit = mCreateDate;
    }

    protected Client(Parcel in) {
        mId = in.readInt();
        mCreateDate = in.readLong();
        mLastVisit = in.readLong();
        mVisitCounter = in.readInt();

        mChildName = in.readString();
        mChildBirthDay = in.readLong();

        mMainParentFirstName = in.readString();
        mMainSecondName = in.readString();
        mMainPatronymicName = in.readString();
        mMainPhoneNumber = in.readString();
        mMainBlobPhoto = in.createByteArray();

        mSecondParentFirstName = in.readString();
        mSecondParentSecondName = in.readString();
        mSecondPatronymicName = in.readString();
        mSecondPhoneNumber = in.readString();
        mSecondPhotoBlob = in.createByteArray();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mId);
        parcel.writeLong(mCreateDate);
        parcel.writeLong(mLastVisit);
        parcel.writeInt(mVisitCounter);
        parcel.writeString(mChildName);
        parcel.writeLong(mChildBirthDay);

        parcel.writeString(mMainParentFirstName);
        parcel.writeString(mMainSecondName);
        parcel.writeString(mMainPatronymicName);
        parcel.writeString(mMainPhoneNumber);
        parcel.writeByteArray(mMainBlobPhoto);

        parcel.writeString(mSecondParentFirstName);
        parcel.writeString(mSecondParentSecondName);
        parcel.writeString(mSecondPatronymicName);
        parcel.writeString(mSecondPhoneNumber);
        parcel.writeByteArray(mSecondPhotoBlob);
    }

    @Ignore
    public static final Creator<Client> CREATOR = new Creator<Client>() {
        @Override
        public Client createFromParcel(Parcel in) {
            return new Client(in);
        }

        @Override
        public Client[] newArray(int size) {
            return new Client[size];
        }
    };

    public void setMainParentFirstName(String firstName) {
        this.mMainParentFirstName = firstName;
    }

    public String getMainSecondName() {
        return mMainSecondName;
    }

    public String getMainParentFirstName() {
        return mMainParentFirstName;
    }

    public void setMainSecondName(String secondName) {
        this.mMainSecondName = secondName;
    }

    public byte[] getMainBlobPhoto() {
        return mMainBlobPhoto;
    }

    public void setMainBlobPhoto(byte[] blobPhoto) {
        this.mMainBlobPhoto = blobPhoto;
    }

    public String getMainPhoneNumber() {
        return mMainPhoneNumber;
    }

    public void setMainPhoneNumber(String phoneNumber) {
        this.mMainPhoneNumber = phoneNumber;
    }

    public long getCreateDate() {
        return mCreateDate;
    }

    public void setCreateDate(long createDate) {
        this.mCreateDate = createDate;
    }

    @NonNull
    public int getId() {
        return mId;
    }

    public void setId(@NonNull int mId) {
        this.mId = mId;
    }

    public void setMainPatronymicName(String patronymicName) {
        this.mMainPatronymicName = patronymicName;
    }

    public String getMainPatronymicName() {
        return mMainPatronymicName;
    }

    public void setChildBirthDay(long birthDay) {
        this.mChildBirthDay = birthDay;
    }

    public long getChildBirthDay() {
        return mChildBirthDay;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void setChildName(String childFirstName) {
        this.mChildName = childFirstName;
    }

    public String getChildName() {
        return mChildName;
    }

    public String getSecondParentFirstName() {
        return mSecondParentFirstName;
    }

    public void setSecondParentFirstName(String mParentFirstName) {
        this.mSecondParentFirstName = mParentFirstName;
    }

    public void setMainParent(Parent parent) {
        setMainParentFirstName(parent.getFirstName());
        setMainSecondName(parent.getSecondName());
        setMainPatronymicName(parent.getPatronymicName());
        setMainPhoneNumber(parent.getPhoneNumber());
        setMainBlobPhoto(parent.getPhotoBlob());
    }

    public void setSecondParent(Parent parent) {
        setSecondParentFirstName(parent.getFirstName());
        setSecondParentSecondName(parent.getSecondName());
        setSecondPatronymicName(parent.getPatronymicName());
        setSecondPhoneNumber(parent.getPhoneNumber());
        setSecondPhotoBlob(parent.getPhotoBlob());
    }

    public String getSecondParentSecondName() {
        return mSecondParentSecondName;
    }

    public void setSecondParentSecondName(String secondParentSecondName) {
        this.mSecondParentSecondName = secondParentSecondName;
    }

    public String getSecondPatronymicName() {
        return mSecondPatronymicName;
    }

    public void setSecondPatronymicName(String secondPatronymicName) {
        this.mSecondPatronymicName = secondPatronymicName;
    }

    public String getSecondPhoneNumber() {
        return mSecondPhoneNumber;
    }

    public void setSecondPhoneNumber(String secondPhoneNumber) {
        this.mSecondPhoneNumber = secondPhoneNumber;
    }

    public byte[] getSecondPhotoBlob() {
        return mSecondPhotoBlob;
    }

    public void setSecondPhotoBlob(byte[] secondPhotoBlob) {
        this.mSecondPhotoBlob = secondPhotoBlob;
    }

    public long getLastVisit() {
        return mLastVisit;
    }

    public void setLastVisit(long lastVisit) {
        this.mLastVisit = lastVisit;
    }

    public int getVisitCounter() {
        return mVisitCounter;
    }

    public void setVisitCounter(int mVisitCounter) {
        this.mVisitCounter = mVisitCounter;
    }

    public void incVisitCounter() {
        mVisitCounter++;
        setLastVisit(System.currentTimeMillis());
    }

    public boolean hasVisitedToday() {
        return Utils.isItToday(mLastVisit);
    }

    public boolean hasSecondParent() {
        return !TextUtils.isEmpty(mSecondParentFirstName);
    }

    public String toJsonString() {
        return Utils.toJson(this);
    }
}
