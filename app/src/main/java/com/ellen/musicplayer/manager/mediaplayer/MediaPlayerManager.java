package com.ellen.musicplayer.manager.mediaplayer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.media.MediaPlayer;

import com.ellen.musicplayer.MessageTag;
import com.ellen.musicplayer.bean.Music;
import com.ellen.musicplayer.message.MusicPlay;
import com.ellen.musicplayer.utils.GaoShiUtils;
import com.ellen.musicplayer.utils.MusicBitmap;
import com.ellen.supermessagelibrary.MessageManager;
import com.ellen.supermessagelibrary.SuperMessage;

import java.io.IOException;
import java.util.List;

public class MediaPlayerManager implements MediaPlayerInterface {

    private static MediaPlayerManager mediaPlayerManager;
    private MediaPlayer mediaPlayer;
    private PlayMode playMode = PlayMode.XUN_HUAN;
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
        sendMessage(false);
    }

    @Override
    public void stop() {
        mediaPlayer.stop();
    }

    @Override
    public void start() {
        mediaPlayer.start();
        sendMessage(true);
    }

    private void sendMessage(boolean isQieHuan){
        MusicPlay musicPlay = new MusicPlay();
        musicPlay.setQieHuan(isQieHuan);
        musicPlay.setMusic(currentOpenMusic());
        SuperMessage superMessage = new SuperMessage(MessageTag.OPEN_MUSIC_ID);
        superMessage.object = musicPlay;
        MessageManager.getInstance().sendMainThreadMessage(superMessage);
    }

    @Override
    public boolean checkCanPlay() {
        if (playList != null && playPosition >= 0) {
            return true;
        }
        return false;
    }

    @Override
    public void next() {
        playPosition = getNextPostion(playPosition);
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

    /**
     * 根据播放模式产生下一曲位置
     *
     * @param playPosition
     * @return
     */
    private int getNextPostion(int playPosition) {
        int resultPlayPosition = 0;
        if (playMode == PlayMode.XUN_HUAN) {
            resultPlayPosition = playPosition + 1;
            if (resultPlayPosition >= playList.size()) {
                resultPlayPosition = 0;
            }
        } else if (playMode == PlayMode.SUI_JI) {
            resultPlayPosition = (int) ((Math.random() * playList.size()));
        } else {
            resultPlayPosition = playPosition;
        }
        return resultPlayPosition;
    }

    @SuppressLint("NewApi")
    @Override
    public Bitmap getCurrentOpenMusicBitmap(Activity activity) {
        if (bitmapPosition == playPosition) {
            return bitmap;
        } else {
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
            if (gaoShiBitmap != null && !gaoShiBitmap.isRecycled()) {
                gaoShiBitmap.recycle();
            }
            gaoShiBitmap = GaoShiUtils.blurBitmap(activity, bitmap, 25f);
            return bitmap;
        }
    }

    @SuppressLint("NewApi")
    @Override
    public Bitmap getGaoShiBitmap(Activity activity) {
        if (bitmapPosition == playPosition) {
            return gaoShiBitmap;
        } else {
            if (gaoShiBitmap != null && !gaoShiBitmap.isRecycled()) {
                gaoShiBitmap.recycle();
            }
            if (bitmap == null) {
                return null;
            } else {
                return GaoShiUtils.blurBitmap(activity, bitmap, 25f);
            }
        }
    }

    @Override
    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    @Override
    public PlayMode getPlayMode() {
        return playMode;
    }

    @Override
    public void adjustPlayMode() {
        if (playMode == PlayMode.XUN_HUAN) {
            playMode = PlayMode.SUI_JI;
        } else if (playMode == PlayMode.SUI_JI) {
            playMode = PlayMode.DAN_QU;
        } else {
            playMode = PlayMode.XUN_HUAN;
        }
    }

    public int getAllTime() {
        return mediaPlayer.getDuration() / 1000;
    }

    public int getCurrentTime() {
        return mediaPlayer.getCurrentPosition() / 1000;
    }
}