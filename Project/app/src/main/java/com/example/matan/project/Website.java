package com.example.matan.project;

public class Website {
    private  String mWebsiteName;
    private  String mWebsiteURL;
    private  long mTime;

    public Website() {
    }

    public Website(String mWebsiteName, String mWebsiteURL, long mTime) {
        this.mWebsiteName = mWebsiteName;
        this.mWebsiteURL = mWebsiteURL;
        this.mTime = mTime;
    }

    public String getmWebsiteName() {
        return mWebsiteName;
    }

    public String getmWebsiteURL() {
        return mWebsiteURL;
    }

    public long getmTime() {
        return mTime;
    }
}