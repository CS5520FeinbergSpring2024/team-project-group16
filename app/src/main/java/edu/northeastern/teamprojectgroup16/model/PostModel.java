package edu.northeastern.teamprojectgroup16.model;

import android.util.Log;

import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private String userName;
    private List<DocumentReference> likes; // users who like this post

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
        this.likes = new ArrayList<>(); // required
    }

    public void addLike(DocumentReference userReference) {
        if (likes == null) {
            likes = new ArrayList<>();
        }
        likes.add(userReference);
    }

    public void removeLike(DocumentReference userReference) {
        if (likes != null) {
            likes.remove(userReference);
        }
    }

    public List<DocumentReference> getLikes() {
        return likes;
    }

    public void setLikes(List<DocumentReference> likes) {
        if (likes == null) {
            Log.e("PostModel", "Attempting to set likes to null");
            this.likes = new ArrayList<>();
        } else {
            this.likes = likes;
        }
    }

    public PostModel(String postId, String title, String imageUrl, String userId, String serverId, String text, int likeCount, String comment, Date timestamp, String userName) {
        this.postId = postId;
        this.title = title;
        this.imageUrl = imageUrl;
        this.userId = userId;
        this.serverId = serverId;
        this.text = text;
        this.likeCount = likeCount;
        this.comment = comment;
        this.timestamp = timestamp;
        this.userName = userName;
    }



    public String getImageUrl() {
        return imageUrl;
    }

    public String getTitle() {
        return title;
    }

//    public String getUserName() {
//        return userName;
//    }

    public String getUserId() {
        return userId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }
//    public void setUserName(String userName) {this.userName = userName;}

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

    @Override
    public String toString() {
        return "PostModel{" +
                "postId='" + postId + '\'' +
                ", title='" + title + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", userId='" + userId + '\'' +
                ", serverId='" + serverId + '\'' +
                ", text='" + text + '\'' +
                ", likeCount=" + likeCount +
                ", comment='" + comment + '\'' +
                ", timestamp=" + timestamp +
                ", userName='" + userName + '\'' +
                ", likes=" + likes +
                '}';
    }
}
