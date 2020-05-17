package com.ellen.musicplayer.bean;

import com.ellen.dhcsqlitelibrary.table.reflection.Primarykey;

public class NearMusic {

    @Primarykey
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
    /**
     * 属于哪个播放列表
     */
    private long playListName;

    public long getPlayListName() {
        return playListName;
    }

    public void setPlayListName(long playListName) {
        this.playListName = playListName;
    }

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
