package edu.northeastern.teamprojectgroup16.model;

public class PostRec {
    private String imageUrl;
    private String title;

    public PostRec(String imageUrl, String title) {
        this.imageUrl = imageUrl;
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getTitle() {
        return title;
    }
}
