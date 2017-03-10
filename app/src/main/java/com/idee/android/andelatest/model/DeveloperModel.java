package com.idee.android.andelatest.model;

import android.os.Parcel;
import android.os.Parcelable;

public class DeveloperModel implements Parcelable{

    private String githubUrl, userId, username, imgUrl;

    protected DeveloperModel(Parcel in) {
        githubUrl = in.readString();
        userId = in.readString();
        username = in.readString();
        imgUrl = in.readString();
    }

    public DeveloperModel() {}

    public static final Creator<DeveloperModel> CREATOR = new Creator<DeveloperModel>() {
        @Override
        public DeveloperModel createFromParcel(Parcel in) {
            return new DeveloperModel(in);
        }

        @Override
        public DeveloperModel[] newArray(int size) {
            return new DeveloperModel[size];
        }
    };

    public String getGithubUrl() {
        return githubUrl;
    }

    public void setGithubUrl(String githubUrl) {
        this.githubUrl = githubUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(githubUrl);
        parcel.writeString(userId);
        parcel.writeString(username);
        parcel.writeString(imgUrl);
    }
}
