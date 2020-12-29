package com.example.project2.Database;

import android.os.Parcel;
import android.os.Parcelable;

// implements Parcelable to be able to send this from an activity to another
public class UserInfo implements Parcelable {
    public String fullName;
    public String country ;
    public String email;
    public String role;
    public boolean hasImage;

    public UserInfo(String fullName, String country, String email, String role)  {
        this.fullName = fullName;
        this.country = country;
        this.email = email;
        this.role = role;
        this.hasImage= false;
    }

    public UserInfo() {

    }

    protected UserInfo(Parcel in) {
        fullName = in.readString();
        country = in.readString();
        email = in.readString();
        role = in.readString();
    }

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel in) {
            return new UserInfo(in);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };

    public String getFullName() {
        return fullName;
    }

    public String getCountry() {
        return country;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fullName);
        dest.writeString(country);
        dest.writeString(email);
        dest.writeString(role);
    }
}
