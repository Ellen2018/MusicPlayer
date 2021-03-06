package com.ellen.musicplayer.manager.mediaplayer;

import android.app.Activity;
import android.graphics.Bitmap;
import android.media.MediaPlayer;

import com.ellen.musicplayer.bean.Music;

import java.util.List;

public interface MediaPlayerInterface {

    void open(int position, List<Music> musicList);
    void pauseAndPlay();
    void stop();
    void start();
    boolean checkCanPlay();

    /**
     * 下一曲
     */
    void next();

    /**
     * 上一曲
     */
    void pre();

    Music currentOpenMusic();
    Bitmap getCurrentOpenMusicBitmap(Activity activity);
    Bitmap getGaoShiBitmap(Activity activity);
    MediaPlayer getMediaPlayer();
    PlayMode getPlayMode();
    void adjustPlayMode();

    void addNextPlayMusic(Music music);
    void addNextPlayMusics(List<Music> musicList);
    List<Music> getPlayList();
    void deletePlayList(int position);
    void clearPlayList();
    int getCurrentPlayPosition();
    void pause();
    boolean isContainsMusic(Music music);
    public long getPlayListName();
}
