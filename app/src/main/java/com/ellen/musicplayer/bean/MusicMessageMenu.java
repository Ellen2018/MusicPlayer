package com.ellen.musicplayer.bean;

public class MusicMessageMenu {

    private int menuIconId;
    private String startString;
    private String content;

    public MusicMessageMenu(int menuIconId, String startString, String content) {
        this.menuIconId = menuIconId;
        this.startString = startString;
        this.content = content;
    }

    public int getMenuIconId() {
        return menuIconId;
    }

    public void setMenuIconId(int menuIconId) {
        this.menuIconId = menuIconId;
    }

    public String getStartString() {
        return startString;
    }

    public void setStartString(String startString) {
        this.startString = startString;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
