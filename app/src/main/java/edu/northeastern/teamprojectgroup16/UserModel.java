package edu.northeastern.teamprojectgroup16;

import java.util.ArrayList;
import java.util.List;

public class UserModel {
    String userID;
    String userName;
    String userEmail;
    String userPassword;
    List<String> serverId;
    public UserModel() {
        this.serverId = new ArrayList<>();
    }

    public UserModel(String userID, String userName, String userEmail, String userPassword) {
        this.userID = userID;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.serverId = new ArrayList<>();
    }

    public String getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public List<String> getServerIds() {
        return serverId;
    }
}
