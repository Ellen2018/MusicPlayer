package com.ellen.musicplayer.bean;

public class SerachBean {

    /**
     * 0->音乐
     * 1->歌手
     * 2->专辑
     */
    private int type = 0;
    private Music music;
    private Singer singer;
    private ZhuanJi zhuanJi;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Music getMusic() {
        return music;
    }

    public void setMusic(Music music) {
        this.music = music;
    }

    public Singer getSinger() {
        return singer;
    }

    public void setSinger(Singer singer) {
        this.singer = singer;
    }

    public ZhuanJi getZhuanJi() {
        return zhuanJi;
    }

    public void setZhuanJi(ZhuanJi zhuanJi) {
        this.zhuanJi = zhuanJi;
    }
}
