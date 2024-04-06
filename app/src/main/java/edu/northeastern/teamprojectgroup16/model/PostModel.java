package edu.northeastern.teamprojectgroup16.model;

public class PostModel {
    private String postId;
    private String title;
    private String imageUrl;
    private String userId;
    private String serverId;
    private String text;

    public PostModel() {
    }

    public PostModel(String postId, String title, String imageUrl, String userId, String serverId, String text){
        this.postId = postId;
        this.title = title;
        this.imageUrl = imageUrl;
        this.userId = userId;
        this.text = text;
        this.serverId = serverId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getUserId() {
        return userId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}