package com.ellen.musicplayer.mediaplayer;

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
}
