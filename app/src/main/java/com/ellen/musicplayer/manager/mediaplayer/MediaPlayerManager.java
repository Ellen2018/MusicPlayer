package com.ellen.musicplayer.manager.mediaplayer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.util.Log;

import com.ellen.musicplayer.KeyValueTag;
import com.ellen.musicplayer.MessageTag;
import com.ellen.musicplayer.bean.Music;
import com.ellen.musicplayer.helper.MMKVHelper;
import com.ellen.musicplayer.message.MusicPlay;
import com.ellen.musicplayer.utils.GaoShiUtils;
import com.ellen.musicplayer.utils.MusicBitmap;
import com.ellen.supermessagelibrary.MessageManager;
import com.ellen.supermessagelibrary.SuperMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MediaPlayerManager implements MediaPlayerInterface {

    private static MediaPlayerManager mediaPlayerManager;
    private MediaPlayer mediaPlayer;
    private PlayMode playMode = null;
    private MMKVHelper musicMmkv;
    /**
     * 播放列表
     */
    private List<Music> playList = null;
    /**
     * 添加的歌曲列表
     */
    private List<Music> addMusicPlayList = null;

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
    private long playListName = -1;

    private MediaPlayerManager() {
        musicMmkv = new MMKVHelper(KeyValueTag.MUSIC_PLAY_NAME);
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
        if (playList == null) {
            playList = new ArrayList<>();
            this.playList.addAll(musicList);
            playListName = System.currentTimeMillis();
        } else {
            if (musicList != playList) {
                this.playList.clear();
                this.playList.addAll(musicList);
                playListName = System.currentTimeMillis();
            }
        }
        this.playPosition = position;
        reset();
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

    private void reset() {
        bitmapPosition = -1;
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
        //记录播放历史
        sendMessage(true);
    }

    private void sendMessage(boolean isQieHuan) {
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
        //先判断临时添加列表里是否有歌曲
        if (addMusicPlayList != null && addMusicPlayList.size() > 0) {
            playPosition++;
            open(playPosition, playList);
            addMusicPlayList.remove(0);
            return;
        }
        playPosition = getNextPostion(playPosition);
        open(playPosition, playList);
    }

    public void nextByUser() {
        if (playMode == PlayMode.DAN_QU) {
            playPosition++;
            if (playPosition >= playList.size()) {
                playPosition = 0;
            }
            open(playPosition, playList);
        } else {
            next();
        }
    }

    @Override
    public void pre() {
        //先判断上一曲列表是否有歌曲,有的话，将列表切换到record
        playPosition = getPrePosition(playPosition);
        open(playPosition, playList);
    }

    private int getPrePosition(int position) {
        position--;
        if (position < 0) {
            position = playList.size() - 1;
        }
        return position;
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
        if (getPlayMode() == PlayMode.XUN_HUAN) {
            resultPlayPosition = playPosition + 1;
            if (resultPlayPosition >= playList.size()) {
                resultPlayPosition = 0;
            }
        } else if (getPlayMode() == PlayMode.SUI_JI) {
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
            if (bitmap != null) {
                gaoShiBitmap = GaoShiUtils.blurBitmap(activity, bitmap, 25f);
            }
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
        if (playMode == null) {
            int playValue = musicMmkv.getValue(KeyValueTag.PLAY_MODE_KEY, 1);
            switch (playValue) {
                case 1:
                    playMode = PlayMode.XUN_HUAN;
                    break;
                case 2:
                    playMode = PlayMode.DAN_QU;
                    break;
                case 3:
                    playMode = PlayMode.SUI_JI;
                    break;
            }
        }
        return playMode;
    }

    @Override
    public void adjustPlayMode() {
        getPlayMode();
        if (playMode == PlayMode.XUN_HUAN) {
            playMode = PlayMode.SUI_JI;
        } else if (playMode == PlayMode.SUI_JI) {
            playMode = PlayMode.DAN_QU;
        } else {
            playMode = PlayMode.XUN_HUAN;
        }
        musicMmkv.save(KeyValueTag.PLAY_MODE_KEY, playMode.getValue());
    }

    @Override
    public void addNextPlayMusic(Music music) {
        if (playList == null || playList.size() == 0) {
            playList = new ArrayList<>();
            playList.add(music);
            open(0, playList);
        } else {
            if (!isContainsMusic(music)) {
                playList.add(playPosition + 1, music);
                if (addMusicPlayList == null || addMusicPlayList.size() == 0) {
                    addMusicPlayList = new ArrayList<>();
                }
                addMusicPlayList.clear();
                addMusicPlayList.add(music);
            }
        }
    }

    @Override
    public void addNextPlayMusics(List<Music> musicList) {
        if (playList == null || playList.size() == 0) {
            open(0, musicList);
        } else {
            List<Music> mlist = new ArrayList<>();
            for (Music music : musicList) {
                if (!isContainsMusic(music)) {
                    mlist.add(music);
                }
            }
            if (mlist != null && mlist.size() > 0) {
                playList.addAll(playPosition + 1, musicList);
                if (addMusicPlayList == null || addMusicPlayList.size() == 0) {
                    addMusicPlayList = new ArrayList<>();
                }
                addMusicPlayList.clear();
                addMusicPlayList.add(musicList.get(0));
            }
        }
    }

    @Override
    public List<Music> getPlayList() {
        return playList;
    }

    @Override
    public void deletePlayList(int position) {
        if (this.playPosition == position) {
            //切换到下一曲
            if (this.playPosition == playList.size() - 1) {
                this.playPosition = 0;
            }
            playList.remove(position);
            if (playList != null && playList.size() > 0) {
                open(playPosition, playList);
            }
        } else if (playPosition < position) {
            //不需要作任何调整
            playList.remove(position);
        } else {
            playPosition--;
            playList.remove(position);
        }
        if (playList == null || playList.size() == 0) {
            clearPlayList();
        }
    }

    @Override
    public void clearPlayList() {
        stop();
        mediaPlayer.reset();
        bitmapPosition = -1;
        playPosition = -1;
        playList.clear();
        playList = null;
        if (addMusicPlayList != null) {
            addMusicPlayList.clear();
        }
        addMusicPlayList = null;

        //发送消息清空了
        MusicPlay musicPlay = new MusicPlay();
        musicPlay.setClear(true);
        SuperMessage superMessage = new SuperMessage(MessageTag.OPEN_MUSIC_ID);
        superMessage.object = musicPlay;
        MessageManager.getInstance().sendMainThreadMessage(superMessage);
    }

    @Override
    public int getCurrentPlayPosition() {
        return playPosition;
    }

    @Override
    public void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            pauseAndPlay();
        }
    }

    @Override
    public boolean isContainsMusic(Music music) {
        if (playList == null || playList.size() == 0) return false;
        boolean falg = false;
        for (Music m : playList) {
            if (m.getPath().equals(music.getPath())) {
                falg = true;
                break;
            }
        }
        return falg;
    }

    @Override
    public long getPlayListName() {
        return playListName > 0 ? playListName : -1;
    }

    public int getAllTime() {
        return mediaPlayer.getDuration() / 1000;
    }

    public int getCurrentTime() {
        return mediaPlayer.getCurrentPosition() / 1000;
    }
}
