package com.alex.devops.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

@Entity(tableName = "children")
public class Child implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int mId;

    @ColumnInfo(name = "name")
    @NonNull
    private String mName;

    @ColumnInfo(name = "birth_day")
    @NonNull
    private long mBirthDay;

    @ColumnInfo(name = "time_stamp")
    private long mTimeStamp;


    public Child() {
        mTimeStamp = System.currentTimeMillis();
    }

    protected Child(Parcel in) {
        mId = in.readInt();
        mName = in.readString();
        mBirthDay = in.readLong();
        mTimeStamp = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mName);
        dest.writeLong(mBirthDay);
        dest.writeLong(mTimeStamp);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Ignore
    public static final Creator<Child> CREATOR = new Creator<Child>() {
        @Override
        public Child createFromParcel(Parcel in) {
            return new Child(in);
        }

        @Override
        public Child[] newArray(int size) {
            return new Child[size];
        }
    };

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public long getBirthDay() {
        return mBirthDay;
    }

    public void setBirthDay(long mBirthDay) {
        this.mBirthDay = mBirthDay;
    }

    public long getTimeStamp() {
        return mTimeStamp;
    }

    public void setTimeStamp(long mTimeStamp) {
        this.mTimeStamp = mTimeStamp;
    }
}
