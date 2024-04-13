package edu.northeastern.teamprojectgroup16.model;

import java.util.ArrayList;
import java.util.List;

public class ServerModel {
    String serverID;
    String serverName;
    List<String> memberIDs; // List of user IDs that are members of this server
    List<String> postIDs; // List of post IDs in this server
    String code;

    public ServerModel() {
        this.memberIDs = new ArrayList<>();
        this.postIDs = new ArrayList<>();
    }

    public ServerModel(String serverID, String serverName, String code) {
        this.serverID = serverID;
        this.serverName = serverName;
        this.memberIDs = new ArrayList<>();
        this.postIDs = new ArrayList<>();
        this.code = code;
    }

    // Getters and Setters for the ServerModel fields
    public String getServerID() {
        return serverID;
    }

    public String getServerName() {
        return serverName;
    }

    public List<String> getMemberIDs() {
        return memberIDs;
    }

    public List<String> getPostIDs() {
        return postIDs;
    }

    public void setServerID(String serverID) {
        this.serverID = serverID;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }


    public void addMember(String memberId){
        this.memberIDs.add(memberId);
    }

    public void setCode(String code){this.code = code;}

    public void addPosts(String postId){
        this.postIDs.add(postId);
    }
}
