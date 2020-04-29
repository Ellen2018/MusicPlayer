package com.ellen.musicplayer.manager.mediaplayer;

public enum PlayMode {
    XUN_HUAN(1),
    DAN_QU(2),
    SUI_JI(3);

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    private int value;

    PlayMode(int vlaue) {
        this.value = vlaue;
    }
}
