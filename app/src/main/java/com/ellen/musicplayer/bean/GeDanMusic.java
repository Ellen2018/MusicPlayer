package com.ellen.musicplayer.bean;

import com.ellen.dhcsqlitelibrary.table.reflection.Primarykey;

public class GeDanMusic {

    /**
     * 喜欢的唯一标记，方便查询
     * 标记:MusicId_AlbumId
     */
    @Primarykey
    private String likeTag;
    private Music music;
    private long likeTime;

    public String getLikeTag() {
        return likeTag;
    }

    public void setLikeTag(String likeTag) {
        this.likeTag = likeTag;
    }

    public Music getMusic() {
        return music;
    }

    public void setMusic(Music music) {
        this.music = music;
        setLikeTag(music.getMusicId()+"_"+music.getAlbumId());
    }

    public long getLikeTime() {
        return likeTime;
    }

    public void setLikeTime(long likeTime) {
        this.likeTime = likeTime;
    }
}
