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
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

//@JsonDeserialize(using = ClientDeserializer.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity(tableName = "clients")
public class Client implements Parcelable {
    @JsonProperty("id")
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int mId;

    @JsonProperty("createDate")
    @ColumnInfo(name = "create_date")
    private long mCreateDate;

    @JsonProperty("childName")
    @ColumnInfo(name = "child_first_name")
    private String mChildName;

    @JsonProperty("childBirthDay")
    @ColumnInfo(name = "child_birth_day")
    private long mChildBirthDay;

    @JsonProperty("mainParentFirstName")
    @ColumnInfo(name = "main_parent_first_name")
    @NonNull
    private String mMainParentFirstName = "";

    @JsonProperty("mainSecondName")
    @ColumnInfo(name = "main_second_name")
    @NonNull
    private String mMainSecondName = "";

    @JsonProperty("mainPatronymicName")
    @ColumnInfo(name = "main_patronymic_name")
    private String mMainPatronymicName;

    @JsonProperty("mainPhoneNumber")
    @ColumnInfo(name = "main_phone_number")
    @NonNull
    private String mMainPhoneNumber;

    @JsonProperty("mainBlobPhoto")
    @ColumnInfo(name = "main_photo_blob")
    @NonNull
    private byte[] mMainBlobPhoto = new byte[0];

    @JsonProperty("secondParentFirstName")
    @ColumnInfo(name = "second_parent_first_name")
    private String mSecondParentFirstName = "";

    @JsonProperty("secondParentSecondName")
    @ColumnInfo(name = "second_parent_second_name")
    private String mSecondParentSecondName = "";

    @JsonProperty("secondPatronymicName")
    @ColumnInfo(name = "second_patronymic_name")
    private String mSecondPatronymicName;

    @JsonProperty("secondPhoneNumber")
    @ColumnInfo(name = "second_phone_number")
    private String mSecondPhoneNumber;

    @JsonProperty("secondPhotoBlob")
    @ColumnInfo(name = "second_photo_blob")
    private byte[] mSecondPhotoBlob = new byte[0];

    @JsonProperty("lastVisit")
    @ColumnInfo(name = "last_visit")
    private long mLastVisit;

    @JsonProperty("visitCounter")
    @ColumnInfo(name = "visit_counter")
    private int mVisitCounter;

    public Client() {
        mCreateDate = System.currentTimeMillis();
        mChildBirthDay = mLastVisit = mCreateDate;
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

    @JsonSetter("mainParentFirstName")
    public void setMainParentFirstName(String firstName) {
        this.mMainParentFirstName = firstName;
    }

    @JsonGetter("mainSecondName")
    public String getMainSecondName() {
        return mMainSecondName;
    }

    @JsonGetter("mainParentFirstName")
    public String getMainParentFirstName() {
        return mMainParentFirstName;
    }

    @JsonSetter("mainSecondName")
    public void setMainSecondName(String secondName) {
        this.mMainSecondName = secondName;
    }

    @JsonGetter("mainBlobPhoto")
    public byte[] getMainBlobPhoto() {
        return mMainBlobPhoto;
    }

    @JsonSetter("mainBlobPhoto")
    public void setMainBlobPhoto(byte[] blobPhoto) {
        this.mMainBlobPhoto = blobPhoto;
    }

    @JsonGetter("mainPhoneNumber")
    public String getMainPhoneNumber() {
        return mMainPhoneNumber;
    }

    @JsonSetter("mainPhoneNumber")
    public void setMainPhoneNumber(String phoneNumber) {
        this.mMainPhoneNumber = phoneNumber;
    }

    @JsonGetter("createDate")
    public long getCreateDate() {
        return mCreateDate;
    }

    @JsonSetter("createDate")
    public void setCreateDate(long createDate) {
        this.mCreateDate = createDate;
    }

    @JsonGetter("id")
    @NonNull
    public int getId() {
        return mId;
    }

    @JsonSetter("id")
    public void setId(@NonNull int mId) {
        this.mId = mId;
    }

    @JsonSetter("mainPatronymicName")
    public void setMainPatronymicName(String patronymicName) {
        this.mMainPatronymicName = patronymicName;
    }

    @JsonGetter("mainPatronymicName")
    public String getMainPatronymicName() {
        return mMainPatronymicName;
    }

    @JsonSetter("childBirthDay")
    public void setChildBirthDay(long birthDay) {
        this.mChildBirthDay = birthDay;
    }

    @JsonGetter("childBirthDay")
    public long getChildBirthDay() {
        return mChildBirthDay;
    }

    @JsonSetter("childName")
    public void setChildName(String childFirstName) {
        this.mChildName = childFirstName;
    }

    @JsonGetter("childName")
    public String getChildName() {
        return mChildName;
    }

    @JsonGetter("secondParentFirstName")
    public String getSecondParentFirstName() {
        return mSecondParentFirstName;
    }

    @JsonSetter("secondParentFirstName")
    public void setSecondParentFirstName(String mParentFirstName) {
        this.mSecondParentFirstName = mParentFirstName;
    }

    @Override
    public int describeContents() {
        return 0;
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

    @JsonGetter("secondParentSecondName")
    public String getSecondParentSecondName() {
        return mSecondParentSecondName;
    }

    @JsonSetter("secondParentSecondName")
    public void setSecondParentSecondName(String secondParentSecondName) {
        this.mSecondParentSecondName = secondParentSecondName;
    }

    @JsonGetter("secondPatronymicName")
    public String getSecondPatronymicName() {
        return mSecondPatronymicName;
    }

    @JsonSetter("secondPatronymicName")
    public void setSecondPatronymicName(String secondPatronymicName) {
        this.mSecondPatronymicName = secondPatronymicName;
    }

    @JsonGetter("secondPhoneNumber")
    public String getSecondPhoneNumber() {
        return mSecondPhoneNumber;
    }

    @JsonSetter("secondPhoneNumber")
    public void setSecondPhoneNumber(String secondPhoneNumber) {
        this.mSecondPhoneNumber = secondPhoneNumber;
    }

    @JsonGetter("secondPhotoBlob")
    public byte[] getSecondPhotoBlob() {
        return mSecondPhotoBlob;
    }

    @JsonSetter("secondPhotoBlob")
    public void setSecondPhotoBlob(byte[] secondPhotoBlob) {
        this.mSecondPhotoBlob = secondPhotoBlob;
    }

    @JsonGetter("lastVisit")
    public long getLastVisit() {
        return mLastVisit;
    }

    @JsonSetter("lastVisit")
    public void setLastVisit(long lastVisit) {
        this.mLastVisit = lastVisit;
    }

    @JsonGetter("visitCounter")
    public int getVisitCounter() {
        return mVisitCounter;
    }

    @JsonSetter("visitCounter")
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
