package edu.northeastern.teamprojectgroup16.model;

import java.util.Date;

public class PostModel {
    private String postId;
    private String title;
    private String imageUrl;
    private String userId;
    private String serverId;
    private String text;
    private int likeCount; // the count of likes
    private String comment; // comments
    private Date timestamp;

    public PostModel() {
    }

    public PostModel(String postId, String title, String imageUrl, String userId, String serverId, String text, int likeCount, String comment, Date timestamp) {
        this.postId = postId;
        this.title = title;
        this.imageUrl = imageUrl;
        this.userId = userId;
        this.serverId = serverId;
        this.text = text;
        this.likeCount = likeCount;
        this.comment = comment;
        this.timestamp = timestamp;
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

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
