package com.ellen.musicplayer.ui.activity;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ellen.musicplayer.MessageTag;
import com.ellen.musicplayer.R;
import com.ellen.musicplayer.base.BaseActivity;
import com.ellen.musicplayer.bean.Music;
import com.ellen.musicplayer.dialog.MusicMessageDialog;
import com.ellen.musicplayer.manager.mediaplayer.MediaPlayerManager;
import com.ellen.musicplayer.manager.mediaplayer.PlayMode;
import com.ellen.musicplayer.message.MusicPlay;
import com.ellen.musicplayer.manager.sql.SQLManager;
import com.ellen.musicplayer.utils.PermissionUtils;
import com.ellen.musicplayer.utils.TimeUtils;
import com.ellen.musicplayer.utils.ToastUtils;
import com.ellen.musicplayer.utils.UriUtils;
import com.ellen.musicplayer.utils.statusutil.StatusUtils;
import com.ellen.supermessagelibrary.BaseEvent;
import com.ellen.supermessagelibrary.MessageEventTrigger;
import com.ellen.supermessagelibrary.MessageManager;
import com.ellen.supermessagelibrary.SuperMessage;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

import java.io.File;
import java.lang.ref.WeakReference;

import gdut.bsx.share2.Share2;
import gdut.bsx.share2.ShareContentType;

public class PlayActivity extends BaseActivity implements View.OnClickListener {

    private TextView tvMusicName, tvSingerName, tvMusicName1, tvSingerName1, tvAlbumName, tvAllTime, tvCurrentTime;
    private ImageView ivBack, ivShare, ivBg, ivMusicIcon, ivPre, ivNext, ivPause, ivPlayMode, ivLike, ivLinShen, ivMessage;
    private BaseEvent baseEvent;
    private IndicatorSeekBar indicatorSeekBar;
    private TimeHandler timeHandler;
    private static final int UPDATE_TIME = 42;
    private PermissionUtils permissionUtils;
    private RelativeLayout rl;

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
        ivLinShen = findViewById(R.id.iv_lin_shen);
        ivLike = findViewById(R.id.iv_like);
        ivBg = findViewById(R.id.iv_bg);
        ivMessage = findViewById(R.id.iv_message);
        rl = findViewById(R.id.rl);
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
        ivLinShen.setOnClickListener(this);
        ivMessage.setOnClickListener(this);

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
        MusicPlay musicPlay = null;
        if (MediaPlayerManager.getInstance().checkCanPlay()) {
            musicPlay = new MusicPlay();
            musicPlay.setMusic(MediaPlayerManager.getInstance().currentOpenMusic());
            musicPlay.setQieHuan(true);
        }
        updateUi(musicPlay);
        if (MediaPlayerManager.getInstance().checkCanPlay()) {
            timeHandler.sendEmptyMessage(0);
        }
        baseEvent = new MessageEventTrigger() {
            @Override
            public void handleMessage(SuperMessage message) {
                updateUi((MusicPlay) message.object);
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

    private void updateUi(MusicPlay musicPlay) {
        if (musicPlay != null && musicPlay.isQieHuan()) {
            timeHandler.setJiaoDu(-2);
            Music music = musicPlay.getMusic();
            tvMusicName.setText(music.getName());
            tvSingerName.setText(music.getArtist());
            tvMusicName1.setText(music.getName());
            tvSingerName1.setText(music.getArtist());
            tvAlbumName.setText(music.getAlbum());
            Bitmap bitmap = MediaPlayerManager.getInstance().getCurrentOpenMusicBitmap(this);
            if (bitmap == null) {
                //显示默认
                ivMusicIcon.setImageResource(R.mipmap.default_music_icon);
                ivBg.setImageResource(R.mipmap.play_default_bg);
            } else {
                ivMusicIcon.setImageBitmap(bitmap);
                ivBg.setImageBitmap(MediaPlayerManager.getInstance().getGaoShiBitmap(this));
            }

            //设置是否喜欢的ui
            boolean isLike = SQLManager.getInstance().isLikeMusic(music);
            if (isLike) {
                //设置为不喜欢
                ivLike.setImageResource(R.mipmap.like);
            } else {
                //设置为喜欢
                ivLike.setImageResource(R.mipmap.not_like);
            }

            //设置进度条最大时间
            indicatorSeekBar.setMax(MediaPlayerManager.getInstance().getAllTime());
            indicatorSeekBar.setProgress(MediaPlayerManager.getInstance().getCurrentTime());

            tvCurrentTime.setText(TimeUtils.format(MediaPlayerManager.getInstance().getMediaPlayer().getCurrentPosition()));
            tvAllTime.setText(TimeUtils.format(MediaPlayerManager.getInstance().getMediaPlayer().getDuration()));
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_share:
                //分享音乐
                if (MediaPlayerManager.getInstance().checkCanPlay()) {
                    Music music = MediaPlayerManager.getInstance().currentOpenMusic();
                    File musicShareFile = new File(music.getPath());
                    new Share2.Builder(this)
                            .setContentType(ShareContentType.AUDIO)
                            .setShareFileUri(UriUtils.getFileUri(this, ShareContentType.AUDIO, musicShareFile))
                            .setTextContent(music.getName() + "-" + music.getArtist())
                            .setTitle("Share Music")
                            .build()
                            .shareBySystem();
                } else {
                    ToastUtils.toast(this, "当前没有播放歌曲，分享失败!");
                }
                break;
            case R.id.iv_lin_shen:
                //检测权限(铃声问题没有解决2020-04-28)
                ToastUtils.toast(this, "抱歉,铃声功能暂未开通!");
                break;
            case R.id.iv_play_pre:
                if (MediaPlayerManager.getInstance().checkCanPlay()) {
                    MediaPlayerManager.getInstance().pre();
                } else {
                    ToastUtils.toast(this, "播放列表没有任何歌曲!");
                }
                break;
            case R.id.iv_play_pause:
                if (MediaPlayerManager.getInstance().checkCanPlay()) {
                    MediaPlayerManager.getInstance().pauseAndPlay();
                } else {
                    ToastUtils.toast(this, "播放列表没有任何歌曲!");
                }
                break;
            case R.id.iv_play_next:
                if (MediaPlayerManager.getInstance().checkCanPlay()) {
                    MediaPlayerManager.getInstance().nextByUser();
                } else {
                    ToastUtils.toast(this, "播放列表没有任何歌曲!");
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
                if (MediaPlayerManager.getInstance().checkCanPlay()) {
                    Music music = MediaPlayerManager.getInstance().currentOpenMusic();
                    //先判断是否喜欢
                    boolean isLike = SQLManager.getInstance().isLikeMusic(music);
                    if (isLike) {
                        //设置为不喜欢
                        SQLManager.getInstance().removeLikeMusic(music);
                        ivLike.setImageResource(R.mipmap.not_like);
                    } else {
                        //设置为喜欢
                        SQLManager.getInstance().addLikeMusic(music);
                        ivLike.setImageResource(R.mipmap.like);
                    }
                } else {
                    ToastUtils.toast(this, "当前没有播放歌曲，喜欢失败!");
                }
                break;
            case R.id.iv_message:
                if (MediaPlayerManager.getInstance().checkCanPlay()) {
                    MusicMessageDialog musicMessageDialog = new MusicMessageDialog(this, MediaPlayerManager.getInstance().currentOpenMusic());
                    musicMessageDialog.showAtLocation(rl, Gravity.BOTTOM, 0, 0);
                } else {
                    ToastUtils.toast(this, "当前没有播放歌曲!");
                }
                break;
        }
    }

    private static class TimeHandler extends Handler {

        private boolean isCanUdpateTime = true;
        private float jiaoDu = -2;

        public float getJiaoDu() {
            return jiaoDu;
        }

        public void setJiaoDu(float jiaoDu) {
            this.jiaoDu = jiaoDu;
            rotate();

        }

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

                //进行旋转
                rotate();
            }
            this.sendEmptyMessageDelayed(0, UPDATE_TIME);
        }

        private void rotate(){
            if(MediaPlayerManager.getInstance().checkCanPlay() && MediaPlayerManager.getInstance().getMediaPlayer().isPlaying()) {
                jiaoDu = jiaoDu + 0.3f;
                getPlayActivity().ivMusicIcon.setPivotX(getPlayActivity().ivMusicIcon.getWidth() / 2);
                getPlayActivity().ivMusicIcon.setPivotY(getPlayActivity().ivMusicIcon.getHeight() / 2);//支点在图片中心
                getPlayActivity().ivMusicIcon.setRotation(jiaoDu);
                if (jiaoDu % 360 == 0) {
                    jiaoDu = 360;
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
