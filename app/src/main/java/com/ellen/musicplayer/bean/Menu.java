package com.ellen.musicplayer.bean;

/**
 * 侧滑的Menu
 */
public class Menu {

    private int iconId;
    private String menuName;

    public Menu(int iconId, String menuName) {
        this.iconId = iconId;
        this.menuName = menuName;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }
}
