package com.ellen.musicplayer.bean;

public class NearMusic {

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
}
