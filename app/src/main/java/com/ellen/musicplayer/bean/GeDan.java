package com.ellen.musicplayer.bean;

import java.util.List;

public class GeDan {

    private String geDanName;
    private long createTime;
    private List<Music> musicList;

    public String getGeDanName() {
        return geDanName;
    }

    public void setGeDanName(String geDanName) {
        this.geDanName = geDanName;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public List<Music> getMusicList() {
        return musicList;
    }

    public void setMusicList(List<Music> musicList) {
        this.musicList = musicList;
    }
}
