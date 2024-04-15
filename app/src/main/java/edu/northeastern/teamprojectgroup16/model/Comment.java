package edu.northeastern.teamprojectgroup16.model;

public class Comment {
    private String publisherId, text, commentId, publisherName;

    public Comment() {
    }

    public Comment(String publisherId, String text, String commentId, String publisherName) {
        this.publisherId = publisherId;
        this.text = text;
        this.commentId = commentId;
        this.publisherName = publisherName;
    }

    public Comment(String publisherId, String text, String commentId) {
        this.publisherId = publisherId;
        this.text = text;
        this.commentId = commentId;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public String getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(String publisherId) {
        this.publisherId = publisherId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }
}
