package com.alex.devops.db;

public class Credentials {
    private String mLogin = "";
    private String mDomain = "";
    private String mSyncURL;
    private String mCreateURL;
    private String mPassword = "";

    public Credentials() {
    }

    public Credentials(String login, String password, String domain, String syncURL, String createURL) {
        this(login, password, domain);
        mSyncURL = syncURL;
        mCreateURL = createURL;
    }

    public Credentials(String login, String password, String domain) {
        mLogin = login;
        mPassword = password;
        mDomain = domain;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public String getDomain() {
        return mDomain;
    }

    public void setDomain(String domain) {
        this.mDomain = domain;
    }

    public String getLogin() {
        return mLogin;
    }

    public void setLogin(String mLogin) {
        this.mLogin = mLogin;
    }

    public boolean isValid() {
        return mLogin.length() > 5 && mPassword.length() > 5 && mDomain.length() > 13;
    }

    public String getSyncURL() {
        return mSyncURL;
    }

    public void setSyncURL(String mSyncURL) {
        this.mSyncURL = mSyncURL;
    }

    public String getCreateURL() {
        return mCreateURL;
    }

    public void setCreateURL(String mCreateURL) {
        this.mCreateURL = mCreateURL;
    }
}
