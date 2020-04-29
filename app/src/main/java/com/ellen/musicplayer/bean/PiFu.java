package com.ellen.musicplayer.bean;

public class PiFu {

    private String imagePath;
    private boolean isAddIcon;
    private int piFuIconId;
    private boolean isGuDinPiFu;
    private long piFuId;

    public PiFu(boolean isGuDinPiFu,String imagePath, boolean isAddIcon, int piFuIconId,long piFuId) {
        this.imagePath = imagePath;
        this.isAddIcon = isAddIcon;
        this.piFuIconId = piFuIconId;
        this.isGuDinPiFu = isGuDinPiFu;
        this.piFuId = piFuId;
    }

    public long getPiFuId() {
        return piFuId;
    }

    public void setPiFuId(long piFuId) {
        this.piFuId = piFuId;
    }

    public int getPiFuIconId() {
        return piFuIconId;
    }

    public void setPiFuIconId(int piFuIconId) {
        this.piFuIconId = piFuIconId;
    }

    public boolean isGuDinPiFu() {
        return isGuDinPiFu;
    }

    public void setGuDinPiFu(boolean guDinPiFu) {
        isGuDinPiFu = guDinPiFu;
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
