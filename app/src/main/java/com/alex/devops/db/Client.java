package com.alex.devops.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

@Entity(tableName = "clients")
public class Client implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int mId;

    @ColumnInfo(name = "child_id")
    private int mChildId;

    @ColumnInfo(name = "primary_parent_id")
    private int mPrimaryParentId;

    @ColumnInfo(name = "secondary_parent_id")
    private int mSecondaryParentId;

    @ColumnInfo(name = "time_stamp")
    private long mTimeStamp;

    public Client() {
        mTimeStamp = System.currentTimeMillis();
    }

    protected Client(Parcel in) {
        mId = in.readInt();
        mChildId = in.readInt();
        mPrimaryParentId = in.readInt();
        mSecondaryParentId = in.readInt();
        mTimeStamp = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeInt(mChildId);
        dest.writeInt(mPrimaryParentId);
        dest.writeInt(mSecondaryParentId);
        dest.writeLong(mTimeStamp);
    }

    @Override
    public int describeContents() {
        return 0;
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

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public int getChildId() {
        return mChildId;
    }

    public void setChildId(int mChildId) {
        this.mChildId = mChildId;
    }

    public int getPrimaryParentId() {
        return mPrimaryParentId;
    }

    public void setPrimaryParentId(int mPrimaryParentId) {
        this.mPrimaryParentId = mPrimaryParentId;
    }

    public int getSecondaryParentId() {
        return mSecondaryParentId;
    }

    public void setSecondaryParentId(int mSecondaryParentId) {
        this.mSecondaryParentId = mSecondaryParentId;
    }

    public long getTimeStamp() {
        return mTimeStamp;
    }

    public void setTimeStamp(long mTimeStamp) {
        this.mTimeStamp = mTimeStamp;
    }
}
