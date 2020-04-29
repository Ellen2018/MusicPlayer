package com.ellen.musicplayer.bean;

public class PiFu {

    private String imagePath;
    private boolean isAddIcon;

    public PiFu(String imagePath,boolean isAddIcon) {
        this.imagePath = imagePath;
        this.isAddIcon = isAddIcon;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public boolean isAddIcon() {
        return isAddIcon;
    }

    public void setAddIcon(boolean addIcon) {
        isAddIcon = addIcon;
    }
}
