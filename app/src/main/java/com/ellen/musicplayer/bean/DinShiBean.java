package com.ellen.musicplayer.bean;

public class DinShiBean {

    private long time;
    private boolean isComplete = false;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }
}
