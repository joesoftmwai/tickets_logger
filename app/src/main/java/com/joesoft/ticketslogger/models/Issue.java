package com.joesoft.ticketslogger.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Issue implements Parcelable {
    public static final String TASK = "Task";
    public static final String BUG = "Bug";
    public static final String NEW = "New";
    public static final String IN_PROGRESS = "In Progress";
    public static final String TESTING = "Testing";
    public static final String CLOSED = "Closed";
    public static final String HIGH = "High";
    public static final String MEDIUM = "Medium";
    public static final String LOW = "Low";


    private String issue_type; // Task, Bug
    private String title;
    private String description;
    private String status; // "In Progress", "Done", "Idle"
    private int priority; // "High" = 3, "Medium" = 2, "Low" = 1
    private @ServerTimestamp Date time_reported;
    private String reporter;
    private String assignee;
    private String issue_id;
    private String comments;

    public Issue() {
    }

    public Issue(String issue_type, String title, String description, String status, int priority,
                 Date time_reported, String reporter, String assignee, String issue_id, String comments) {
        this.issue_type = issue_type;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.time_reported = time_reported;
        this.reporter = reporter;
        this.assignee = assignee;
        this.issue_id = issue_id;
        this.comments = comments;
    }

    protected Issue(Parcel in) {
        issue_type = in.readString();
        title = in.readString();
        description = in.readString();
        status = in.readString();
        priority = in.readInt();
        reporter = in.readString();
        assignee = in.readString();
        issue_id = in.readString();
        comments = in.readString();
        time_reported = (Date) in.readSerializable();
    }

    public static final Creator<Issue> CREATOR = new Creator<Issue>() {
        @Override
        public Issue createFromParcel(Parcel in) {
            return new Issue(in);
        }

        @Override
        public Issue[] newArray(int size) {
            return new Issue[size];
        }
    };

    public String getPriorityString() {
        if (priority == 1) {
            return LOW;
        } else if (priority == 2) {
            return MEDIUM;
        } else {
            return HIGH;
        }
    }

    public int getPriorityInteger(String priority) {
        if (priority.equals(LOW)) {
            return 1;
        } else if (priority.equals(MEDIUM)) {
            return 2;
        } else {
            return 3;
        }
    }


    public String getIssue_type() {
        return issue_type;
    }

    public void setIssue_type(String issue_type) {
        this.issue_type = issue_type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Date getTime_reported() {
        return time_reported;
    }

    public void setTime_reported(Date time_reported) {
        this.time_reported = time_reported;
    }

    public String getReporter() {
        return reporter;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getIssue_id() {
        return issue_id;
    }

    public void setIssue_id(String issue_id) {
        this.issue_id = issue_id;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(issue_type);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(status);
        dest.writeInt(priority);
        dest.writeString(reporter);
        dest.writeString(assignee);
        dest.writeString(issue_id);
        dest.writeString(comments);
        dest.writeSerializable(time_reported);
    }

    @Override
    public String toString() {
        return "Issue{" +
                "issue_type='" + issue_type + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", priority=" + priority +
                ", time_reported=" + time_reported +
                ", reporter='" + reporter + '\'' +
                ", assignee='" + assignee + '\'' +
                ", issue_id='" + issue_id + '\'' +
                ", comments='" + comments + '\'' +
                '}';
    }
}
