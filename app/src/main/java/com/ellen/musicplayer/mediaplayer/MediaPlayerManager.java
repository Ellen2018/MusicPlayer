package com.ellen.musicplayer.mediaplayer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.media.MediaPlayer;

import com.ellen.musicplayer.bean.Music;
import com.ellen.musicplayer.notification.MusicNotification;
import com.ellen.musicplayer.utils.GaoShiUtils;
import com.ellen.musicplayer.utils.MusicBitmap;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.List;

public class MediaPlayerManager implements MediaPlayerInterface {

    private static MediaPlayerManager mediaPlayerManager;
    private MediaPlayer mediaPlayer;
    /**
     * 播放列表
     */
    private List<Music> playList = null;
    /**
     * 记录播放的位置
     */
    private int playPosition = -1;

    /**
     * 当前播放歌曲的专辑图片
     */
    private Bitmap bitmap = null;
    private int bitmapPosition = -1;
    private Bitmap gaoShiBitmap = null;

    private MediaPlayerManager() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                start();
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                next();
            }
        });
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                next();
                return false;
            }
        });
    }


    public static MediaPlayerManager getInstance() {
        if (mediaPlayerManager == null) {
            synchronized (MediaPlayerManager.class) {
                if (mediaPlayerManager == null) {
                    mediaPlayerManager = new MediaPlayerManager();
                }
            }
        }
        return mediaPlayerManager;
    }

    @Override
    public void open(int position, List<Music> musicList) {
        this.playList = musicList;
        this.playPosition = position;
        Music music = playList.get(playPosition);
        //重置资源
        mediaPlayer.reset();

        try {
            mediaPlayer.setDataSource(music.getPath());
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void pauseAndPlay() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        } else {
            mediaPlayer.start();
        }
    }

    @Override
    public void stop() {
        mediaPlayer.stop();
    }

    @Override
    public void start() {
        EventBus.getDefault().post(playList.get(playPosition));
        mediaPlayer.start();
    }

    @Override
    public void next() {
        playPosition++;
        if (playPosition >= playList.size()) {
            playPosition = 0;
        }
        open(playPosition, playList);
    }

    @Override
    public void pre() {
        playPosition--;
        if (playPosition < 0) {
            playPosition = playList.size() - 1;
        }
        open(playPosition, playList);
    }

    @Override
    public Music currentOpenMusic() {
        if (playPosition >= 0) {
            return playList.get(playPosition);
        }
        return null;
    }

    @SuppressLint("NewApi")
    @Override
    public Bitmap getCurrentOpenMusicBitmap(Activity activity) {
        if(bitmapPosition == playPosition){
            return bitmap;
        }else {
            bitmapPosition = playPosition;
            Music music = currentOpenMusic();
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
            if (music != null) {
                bitmap = MusicBitmap.getArtwork(activity, music.getMusicId(), music.getAlbumId());
            } else {
                bitmap = null;
            }
            //获取高斯模糊图片
            if(gaoShiBitmap != null && !gaoShiBitmap.isRecycled()){
                gaoShiBitmap.recycle();
            }
            gaoShiBitmap = GaoShiUtils.blurBitmap(activity,bitmap,15f);
            return bitmap;
        }
    }

    @SuppressLint("NewApi")
    @Override
    public Bitmap getGaoShiBitmap(Activity activity) {
        if(bitmapPosition == playPosition){
            return gaoShiBitmap;
        }else {
            if(gaoShiBitmap != null && !gaoShiBitmap.isRecycled()){
                gaoShiBitmap.recycle();
            }
            if(bitmap == null){
                return null;
            }else {
                return GaoShiUtils.blurBitmap(activity,bitmap,15f);
            }
        }
    }

    @Override
    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }
}
