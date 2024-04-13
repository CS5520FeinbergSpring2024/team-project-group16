package edu.northeastern.teamprojectgroup16.model;

import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.List;

public class UserModel {
    String userID;
    String userName;
    String userEmail;
    String userPassword;
    List<String> serverIds; // List of server IDs that the user is a member of
    List<DocumentReference> saved;

    public UserModel() {
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public void setSaved(List<DocumentReference> saved) {
        this.saved = saved;
    }

    public UserModel(String userID, String userName, String userEmail, String userPassword, List<String> serverIds) {
        this.userID = userID;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.serverIds = serverIds;
        this.saved = new ArrayList<>();
    }

    public List<DocumentReference> getSaved() {
        return saved;
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
