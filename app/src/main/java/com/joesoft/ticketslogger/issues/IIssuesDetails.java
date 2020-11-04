package com.joesoft.ticketslogger.issues;

import com.joesoft.ticketslogger.models.Comment;
import com.joesoft.ticketslogger.models.User;

import java.util.ArrayList;

public interface IIssuesDetails {
    ArrayList<Comment> getIssueComments();
    ArrayList<User> getUsers();
}
