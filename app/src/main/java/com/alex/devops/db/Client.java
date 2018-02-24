package com.alex.devops.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.alex.devops.utils.Utils;

@Entity(tableName = "clients")
public class Client implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    private int mId;

    @ColumnInfo(name = "time_stamp")
    private long mTimeStamp;

    @ColumnInfo(name = "first_name")
    @NonNull
    private String mFirstName = "";

    @ColumnInfo(name = "second_name")
    @NonNull
    private String mSecondName = "";

    @ColumnInfo(name = "phone_number")
    @NonNull
    private String mPhoneNumber;

    @ColumnInfo(name = "photo_blob")
    @NonNull
    private byte[] mBlobPhoto = new byte[0];
    @ColumnInfo(name = "photo_path")
    private String mPhotoPath;
    @Ignore
    private Bitmap mBitmap;

    @ColumnInfo(name = "patronymic_name")
    private String mPatronymicName;

    @ColumnInfo(name = "birth_day")
    private long mBirthDay;

    public Client() {
        mTimeStamp = System.currentTimeMillis();
    }

    protected Client(Parcel in) {
        mId = in.readInt();
        mTimeStamp = in.readLong();
        mBirthDay = in.readLong();
        mFirstName = in.readString();
        mSecondName = in.readString();
        mPhoneNumber = in.readString();
        mBlobPhoto = in.createByteArray();
        mPhotoPath = in.readString();
        mBitmap = in.readParcelable(Bitmap.class.getClassLoader());
        mPatronymicName = in.readString();
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

    public void setFirstName(String firstName) {
        this.mFirstName = firstName;
    }

    public String getSecondName() {
        return mSecondName;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setSecondName(String secondName) {
        this.mSecondName = secondName;
    }

    public byte[] getBlobPhoto() {
        return mBlobPhoto;
    }

    public void setBlobPhoto(byte[] blobPhoto) {
        this.mBlobPhoto = blobPhoto;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.mPhoneNumber = phoneNumber;
    }

    public long getTimeStamp() {
        return mTimeStamp;
    }

    public void setTimeStamp(long mTimerStamp) {
        this.mTimeStamp = mTimerStamp;
    }

    @NonNull
    public int getId() {
        return mId;
    }

    public void setId(@NonNull int mId) {
        this.mId = mId;
    }

    @Override
    public String toString() {
        return "Client{" +
                "mId=" + mId +
                ", mFirstName='" + mFirstName + '\'' +
                ", mSecondName='" + mSecondName + '\'' +
                ", mPhoneNumber='" + mPhoneNumber + '\'' +
                ", mPhotoPath='" + mPhotoPath + '\'' +
                '}';
    }

    public void prepare() {
        byte[] blobPhoto = Utils.getBytes(mBitmap);
        Utils.writeToFileAsync(mPhotoPath, blobPhoto);
        setBlobPhoto(blobPhoto);
        mBitmap.recycle();
    }

    public String getPhotoPath() {
        return mPhotoPath;
    }

    public void setPhotoPath(String photoPath) {
        mPhotoPath = photoPath;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setBitmap(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
    }

    public void setPatronymicName(String patronymicName) {
        this.mPatronymicName = patronymicName;
    }

    public String getPatronymicName() {
        return mPatronymicName;
    }

    public void setBirthDay(long birthDay) {
        this.mBirthDay = birthDay;
    }

    public long getBirthDay() {
        return mBirthDay;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mId);
        parcel.writeLong(mTimeStamp);
        parcel.writeLong(mBirthDay);
        parcel.writeString(mFirstName);
        parcel.writeString(mSecondName);
        parcel.writeString(mPhoneNumber);
        parcel.writeByteArray(mBlobPhoto);
        parcel.writeString(mPhotoPath);
        parcel.writeParcelable(mBitmap, i);
        parcel.writeString(mPatronymicName);
    }
}
