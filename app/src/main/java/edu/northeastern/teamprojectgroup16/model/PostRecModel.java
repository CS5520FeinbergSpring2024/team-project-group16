package edu.northeastern.teamprojectgroup16.model;

public class PostRecModel {

    private String title;
    private String imageUrl;

    public PostRecModel(String title, String imageUrl){
        this.title = title;
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getTitle() {
        return title;
    }
}
