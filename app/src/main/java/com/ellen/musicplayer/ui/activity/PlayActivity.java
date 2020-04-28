package com.ellen.musicplayer.ui.activity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ellen.musicplayer.MessageTag;
import com.ellen.musicplayer.R;
import com.ellen.musicplayer.base.BaseActivity;
import com.ellen.musicplayer.bean.Music;
import com.ellen.musicplayer.mediaplayer.MediaPlayerManager;
import com.ellen.musicplayer.mediaplayer.PlayMode;
import com.ellen.musicplayer.sql.SQLManager;
import com.ellen.musicplayer.utils.TimeUtils;
import com.ellen.musicplayer.utils.statusutil.StatusUtils;
import com.ellen.supermessagelibrary.BaseEvent;
import com.ellen.supermessagelibrary.MessageEventTrigger;
import com.ellen.supermessagelibrary.MessageManager;
import com.ellen.supermessagelibrary.SuperMessage;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.IndicatorType;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;
import com.warkiz.widget.TickMarkType;

import java.lang.ref.WeakReference;

public class PlayActivity extends BaseActivity implements View.OnClickListener {

    private TextView tvMusicName, tvSingerName, tvMusicName1, tvSingerName1, tvAlbumName, tvAllTime, tvCurrentTime;
    private ImageView ivBack, ivShare, ivBg, ivMusicIcon, ivPre, ivNext, ivPause, ivPlayMode,ivLike;
    private BaseEvent baseEvent;
    private IndicatorSeekBar indicatorSeekBar;
    private TimeHandler timeHandler;
    private static final int UPDATE_TIME = 500;

    @Override
    protected void setStatus() {
        StatusUtils.setNoActionBar(this);
        StatusUtils.setTranslucentStatus(this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_play;
    }

    @Override
    protected void initView() {
        tvMusicName = findViewById(R.id.tv_music_name);
        tvSingerName = findViewById(R.id.tv_singer_name);
        ivBack = findViewById(R.id.iv_back);
        ivShare = findViewById(R.id.iv_share);
        ivMusicIcon = findViewById(R.id.iv_music_icon);
        ivPre = findViewById(R.id.iv_play_pre);
        ivPause = findViewById(R.id.iv_play_pause);
        ivNext = findViewById(R.id.iv_play_next);
        tvMusicName1 = findViewById(R.id.tv_music_name_1);
        tvSingerName1 = findViewById(R.id.tv_singer_name_1);
        tvAlbumName = findViewById(R.id.tv_album_name);
        ivPlayMode = findViewById(R.id.iv_play_mode);
        indicatorSeekBar = findViewById(R.id.seek_bar);
        tvAllTime = findViewById(R.id.tv_all_time);
        tvCurrentTime = findViewById(R.id.tv_current_time);
        ivLike = findViewById(R.id.iv_like);
        ivBg = findViewById(R.id.iv_bg);
        tvMusicName.setSelected(true);
        tvSingerName.setSelected(true);
        tvMusicName1.setSelected(true);
        tvSingerName1.setSelected(true);
        tvAlbumName.setSelected(true);
        ivBack.setOnClickListener(this);
        ivShare.setOnClickListener(this);
        ivPre.setOnClickListener(this);
        ivPause.setOnClickListener(this);
        ivNext.setOnClickListener(this);
        ivPlayMode.setOnClickListener(this);
        ivLike.setOnClickListener(this);

        indicatorSeekBar.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
                tvCurrentTime.setText(TimeUtils.format(seekParams.progress * 1000));
            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {
                timeHandler.setCanUdpateTime(false);
            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
                MediaPlayerManager.getInstance().getMediaPlayer().seekTo(seekBar.getProgress() * 1000);
                timeHandler.setCanUdpateTime(true);
            }
        });

    }

    @Override
    protected void initData() {
        timeHandler = new TimeHandler(this);
        if (MediaPlayerManager.getInstance().checkCanPlay()) {
            updateUi(MediaPlayerManager.getInstance().currentOpenMusic());
            timeHandler.sendEmptyMessage(0);
        }
        baseEvent = new MessageEventTrigger() {
            @Override
            public void handleMessage(SuperMessage message) {
                updateUi((Music) message.object);
            }
        };
        MessageManager.getInstance().registerMessageEvent(MessageTag.OPEN_MUSIC_ID, baseEvent);
    }

    @Override
    protected void destory() {
        MessageManager.getInstance().unRegisterMessageEvent(MessageTag.OPEN_MUSIC_ID, baseEvent);
        timeHandler.removeMessages(0);
        timeHandler = null;
    }

    @Override
    protected Boolean isSetVerticalScreen() {
        return true;
    }

    private void updateUi(Music music) {
        tvMusicName.setText(music.getName());
        tvSingerName.setText(music.getArtist());
        tvMusicName1.setText(music.getName());
        tvSingerName1.setText(music.getArtist());
        tvAlbumName.setText(music.getAlbum());
        Bitmap bitmap = MediaPlayerManager.getInstance().getCurrentOpenMusicBitmap(this);
        if (bitmap == null) {
            //显示默认
            ivBg.setImageResource(R.mipmap.default_bg);
        } else {
            ivMusicIcon.setImageBitmap(bitmap);
            ivBg.setImageBitmap(MediaPlayerManager.getInstance().getGaoShiBitmap(this));
        }

        //更新播放/暂停状态
        if (MediaPlayerManager.getInstance().getMediaPlayer().isPlaying()) {
            ivPause.setImageResource(R.mipmap.play_start);
        } else {
            ivPause.setImageResource(R.mipmap.play_pause);
        }

        //更新播放模式
        PlayMode playMode = MediaPlayerManager.getInstance().getPlayMode();
        if (playMode == PlayMode.XUN_HUAN) {
            ivPlayMode.setImageResource(R.mipmap.playmode_xun_huan);
        } else if (playMode == PlayMode.SUI_JI) {
            ivPlayMode.setImageResource(R.mipmap.playmode_sui_ji);
        } else {
            ivPlayMode.setImageResource(R.mipmap.playmode_dan_qu);
        }

        //设置进度条最大时间
        indicatorSeekBar.setMax(MediaPlayerManager.getInstance().getAllTime());
        indicatorSeekBar.setProgress(MediaPlayerManager.getInstance().getCurrentTime());

        tvCurrentTime.setText(TimeUtils.format(MediaPlayerManager.getInstance().getMediaPlayer().getCurrentPosition()));
        tvAllTime.setText(TimeUtils.format(MediaPlayerManager.getInstance().getMediaPlayer().getDuration()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_share:
                break;
            case R.id.iv_play_pre:
                if (MediaPlayerManager.getInstance().checkCanPlay()) {
                    MediaPlayerManager.getInstance().pre();
                }
                break;
            case R.id.iv_play_pause:
                if (MediaPlayerManager.getInstance().checkCanPlay()) {
                    MediaPlayerManager.getInstance().pauseAndPlay();
                }
                break;
            case R.id.iv_play_next:
                if (MediaPlayerManager.getInstance().checkCanPlay()) {
                    MediaPlayerManager.getInstance().next();
                }
                break;
            case R.id.iv_play_mode:
                MediaPlayerManager.getInstance().adjustPlayMode();
                PlayMode playMode = MediaPlayerManager.getInstance().getPlayMode();
                if (playMode == PlayMode.XUN_HUAN) {
                    ivPlayMode.setImageResource(R.mipmap.playmode_xun_huan);
                } else if (playMode == PlayMode.SUI_JI) {
                    ivPlayMode.setImageResource(R.mipmap.playmode_sui_ji);
                } else {
                    ivPlayMode.setImageResource(R.mipmap.playmode_dan_qu);
                }
                break;
            case R.id.iv_like:
                //先判断是否喜欢
                SQLManager.getInstance().isLikeMusic(MediaPlayerManager.getInstance().currentOpenMusic());
                break;
        }
    }

    private static class TimeHandler extends Handler {

        private boolean isCanUdpateTime = true;

        public void setCanUdpateTime(boolean canUdpateTime) {
            isCanUdpateTime = canUdpateTime;
        }

        private WeakReference<PlayActivity> playActivityWeakReference;

        TimeHandler(PlayActivity playActivity) {
            playActivityWeakReference = new WeakReference<>(playActivity);
        }

        PlayActivity getPlayActivity() {
            return playActivityWeakReference.get();
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            //更新时间
            if (isCanUdpateTime) {
                getPlayActivity().indicatorSeekBar
                        .setProgress(MediaPlayerManager.getInstance().getCurrentTime());
                getPlayActivity().tvCurrentTime.setText(TimeUtils.format(MediaPlayerManager.getInstance().getMediaPlayer().getCurrentPosition()));
                getPlayActivity().tvAllTime.setText(TimeUtils.format(MediaPlayerManager.getInstance().getMediaPlayer().getDuration()));
            }
            this.sendEmptyMessageDelayed(0, UPDATE_TIME);
        }
    }
}
