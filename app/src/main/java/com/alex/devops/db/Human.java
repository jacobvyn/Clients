package com.alex.devops.db;

public class Human {
    private String mFirstName;
    private long mBirthDay;

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        this.mFirstName = firstName;
    }

    public long getBirthDay() {
        return mBirthDay;
    }

    public void setBirthDay(long birthDay) {
        this.mBirthDay = birthDay;
    }
}
