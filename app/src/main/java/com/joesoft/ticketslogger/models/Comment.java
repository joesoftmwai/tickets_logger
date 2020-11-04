package com.joesoft.ticketslogger.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Comment implements Parcelable {
    private String comment;
    private String commented_by;
    private String comment_id;
    private String issue_id;
    private @ServerTimestamp Date time_created;

    public Comment() {
    }

    public Comment(String comment, String commented_by, String comment_id, String issue_id, Date time_created) {
        this.comment = comment;
        this.commented_by = commented_by;
        this.comment_id = comment_id;
        this.issue_id = issue_id;
        this.time_created = time_created;
    }

    protected Comment(Parcel in) {
        comment = in.readString();
        commented_by = in.readString();
        comment_id = in.readString();
        issue_id = in.readString();
        time_created = (Date) in.readSerializable();
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCommented_by() {
        return commented_by;
    }

    public void setCommented_by(String commented_by) {
        this.commented_by = commented_by;
    }

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getIssue_id() {
        return issue_id;
    }

    public void setIssue_id(String issue_id) {
        this.issue_id = issue_id;
    }

    public Date getTime_created() {
        return time_created;
    }

    public void setTime_created(Date time_created) {
        this.time_created = time_created;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(comment);
        dest.writeString(commented_by);
        dest.writeString(comment_id);
        dest.writeString(issue_id);
        dest.writeSerializable(time_created);
    }
}
