package com.joesoft.ticketslogger.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class User implements Parcelable{
    private String name;
    private String phone;
    private String profile_image;
    private String role;
    private String user_id;
    private String email;
    private @ServerTimestamp Date last_login;
    private @ServerTimestamp Date date_created;

    public User() {
    }

    public User(String name, String phone, String profile_image, String role, String user_id, String email, Date last_login, Date date_created) {
        this.name = name;
        this.phone = phone;
        this.profile_image = profile_image;
        this.role = role;
        this.user_id = user_id;
        this.email = email;
        this.last_login = last_login;
        this.date_created = date_created;
    }

    protected User(Parcel in) {
        name = in.readString();
        phone = in.readString();
        profile_image = in.readString();
        role = in.readString();
        user_id = in.readString();
        email = in.readString();
        last_login = (Date) in.readSerializable();
        date_created = (Date) in.readSerializable();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getLast_login() {
        return last_login;
    }

    public void setLast_login(Date last_login) {
        this.last_login = last_login;
    }

    public Date getDate_created() {
        return date_created;
    }

    public void setDate_created(Date date_created) {
        this.date_created = date_created;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(phone);
        dest.writeString(profile_image);
        dest.writeString(role);
        dest.writeString(user_id);
        dest.writeString(email);
        dest.writeSerializable(last_login);
        dest.writeSerializable(date_created);
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", profile_image='" + profile_image + '\'' +
                ", role='" + role + '\'' +
                ", user_id='" + user_id + '\'' +
                ", email='" + email + '\'' +
                ", last_login='" + last_login + '\'' +
                ", date_created=" + date_created +
                '}';
    }
}


