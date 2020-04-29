package com.ellen.musicplayer.message;

import com.ellen.musicplayer.bean.Music;

public class MusicPlay {

    /**
     * 是否切换
     */
    private boolean isQieHuan = false;
    /**
     * 记录当前播放的音乐
     */
    private Music music;

    public boolean isQieHuan() {
        return isQieHuan;
    }

    public void setQieHuan(boolean qieHuan) {
        isQieHuan = qieHuan;
    }

    public Music getMusic() {
        return music;
    }

    public void setMusic(Music music) {
        this.music = music;
    }
}
