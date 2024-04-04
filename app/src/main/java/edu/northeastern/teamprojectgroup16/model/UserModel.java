package edu.northeastern.teamprojectgroup16.model;

import java.util.List;

public class UserModel {
    String userID;
    String userName;
    String userEmail;
    String userPassword;
    List<String> serverIds; // List of server IDs that the user is a member of

    public UserModel() {
    }

    public UserModel(String userID, String userName, String userEmail, String userPassword, List<String> serverIds) {
        this.userID = userID;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.serverIds = serverIds;
    }

    // Getters and Setters for serverIds
    public List<String> getServerIds() {
        return serverIds;
    }

    public void setServerIds(List<String> serverIds) {
        this.serverIds = serverIds;
    }

    // Other getters and setters remain the same
}
