package com.ellen.musicplayer.bean;

public class NearMusic {

    private String nearTag;
    private Music music;
    /**
     * 播放时间
     */
    private long playTime;
    /**
     * 播放次数
     */
    private int playTimes;

    public Music getMusic() {
        return music;
    }

    public void setMusic(Music music) {
        this.music = music;
    }

    public long getPlayTime() {
        return playTime;
    }

    public void setPlayTime(long playTime) {
        this.playTime = playTime;
    }

    public String getNearTag() {
        return nearTag;
    }

    public void setNearTag(String nearTag) {
        this.nearTag = nearTag;
    }

    public int getPlayTimes() {
        return playTimes;
    }

    public void setPlayTimes(int playTimes) {
        this.playTimes = playTimes;
    }
}
