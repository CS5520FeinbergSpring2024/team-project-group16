package edu.northeastern.teamprojectgroup16;

public class PostRec {
    private int imageResId;
    private String title;

    public PostRec(int imageResId, String title) {
        this.imageResId = imageResId;
        this.title = title;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getTitle() {
        return title;
    }
}
