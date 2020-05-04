package com.ellen.musicplayer.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.ellen.musicplayer.MessageTag;
import com.ellen.musicplayer.R;
import com.ellen.musicplayer.adapter.SerachMusicAdapter;
import com.ellen.musicplayer.base.BaseActivity;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseRecyclerViewAdapter;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseViewHolder;
import com.ellen.musicplayer.base.adapter.viewpager.BaseFragmentPagerAdapter;
import com.ellen.musicplayer.bean.GeDan;
import com.ellen.musicplayer.bean.Music;
import com.ellen.musicplayer.bean.PiFu;
import com.ellen.musicplayer.bean.SerachBean;
import com.ellen.musicplayer.bean.Singer;
import com.ellen.musicplayer.bean.ZhuanJi;
import com.ellen.musicplayer.dialog.PlayListDialog;
import com.ellen.musicplayer.manager.mediaplayer.MediaPlayerManager;
import com.ellen.musicplayer.manager.pifu.PiFuManager;
import com.ellen.musicplayer.message.MusicPlay;
import com.ellen.musicplayer.message.PiFuMessage;
import com.ellen.musicplayer.ui.fragment.DanQuFragment;
import com.ellen.musicplayer.ui.fragment.GeDanFragment;
import com.ellen.musicplayer.ui.fragment.SingerFragment;
import com.ellen.musicplayer.ui.fragment.ZhuanJiFragment;
import com.ellen.musicplayer.utils.LocalSDMusicUtils;
import com.ellen.musicplayer.utils.statusutil.StatusUtils;
import com.ellen.supermessagelibrary.BaseEvent;
import com.ellen.supermessagelibrary.MessageEventTrigger;
import com.ellen.supermessagelibrary.MessageManager;
import com.ellen.supermessagelibrary.SuperMessage;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class SerachActivity extends BaseActivity implements View.OnClickListener {

    //播放界面控件
    private TextView tvMusicName, tvSingerName;
    private ImageView ivPlayerBg, ivPlayerPause, ivPlayerList,ivPlayerIcon;
    private RelativeLayout rlPlayerMb;
    private BaseEvent baseEvent;

    private ImageView ivBack,ivCancel;
    private EditText editText;
    private TextView tvSerachNull;
    private TabLayout tabLayout;

    private ViewPager viewPager;
    private RelativeLayout rl;
    private List<Fragment> fragmentList;
    private String[] titles = {"单曲","歌手","专辑","歌单"};
    private BaseFragmentPagerAdapter baseFragmentPagerAdapter;
    private DanQuFragment danQuFragment;
    private SingerFragment singerFragment;
    private ZhuanJiFragment zhuanJiFragment;
    private GeDanFragment geDanFragment;
    private List<Music> serachMusicList;
    private List<Singer>  serachSingerList;
    private List<ZhuanJi>  serachZhuanJiList;
    private List<GeDan>  serachGeDanList;

    @Override
    protected void setStatus() {
        StatusUtils.setNoActionBar(this);
        StatusUtils.setTranslucentStatus(this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_serach;
    }

    @Override
    protected void initView() {
        tvMusicName = findViewById(R.id.tv_music_name);
        tvSingerName = findViewById(R.id.tv_singer_name);
        ivPlayerBg = findViewById(R.id.iv_player_bg);
        ivPlayerPause = findViewById(R.id.iv_player_pause);
        ivPlayerList = findViewById(R.id.iv_player_list);
        rlPlayerMb = findViewById(R.id.rl_player_mb);
        ivPlayerIcon = findViewById(R.id.iv_player_icon);
        rlPlayerMb.setOnClickListener(this);
        ivPlayerPause.setOnClickListener(this);
        rl = findViewById(R.id.rl);
        ivBack = findViewById(R.id.iv_back);
        ivBack.setOnClickListener(this);
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);
        editText = findViewById(R.id.edit_text);
        ivCancel = findViewById(R.id.iv_cancel);
        ivCancel.setOnClickListener(this);
        ivPlayerList.setOnClickListener(this);
        tvSerachNull = findViewById(R.id.tv_serach_null);
        fragmentList = new ArrayList<>();
        fragmentList.add(danQuFragment = new DanQuFragment(serachMusicList = LocalSDMusicUtils.serachMusics(SerachActivity.this,"")));
        fragmentList.add(singerFragment = new SingerFragment(serachSingerList = LocalSDMusicUtils.serachSigers(SerachActivity.this,"")));
        fragmentList.add(zhuanJiFragment = new ZhuanJiFragment(serachZhuanJiList = LocalSDMusicUtils.serachZhuanJis(SerachActivity.this,"")));
        fragmentList.add(geDanFragment = new GeDanFragment());

        viewPager.setAdapter(baseFragmentPagerAdapter = new BaseFragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            protected int getFragmentPagerSize() {
                return fragmentList.size();
            }

            @Override
            protected Fragment getFragment(int position) {
                return fragmentList.get(position);
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                String resultString = "";
                switch (position){
                    case 0:
                        resultString = titles[position]+"("+serachMusicList.size()+")";
                        break;
                    case 1:
                        resultString = titles[position]+"("+serachSingerList.size()+")";
                        break;
                    case 2:
                        resultString = titles[position]+"("+serachZhuanJiList.size()+")";
                        break;
                    case 3:
                        resultString = titles[position]+"("+serachMusicList.size()+")";
                        break;
                }
                return resultString;
            }
        });

        tabLayout.setupWithViewPager(viewPager);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String serachString = s.toString();
                if(TextUtils.isEmpty(serachString)){
                    ivCancel.setVisibility(View.GONE);
                }else {
                    ivCancel.setVisibility(View.VISIBLE);
                }

                serachMusicList = LocalSDMusicUtils.serachMusics(SerachActivity.this,serachString);
                danQuFragment.setMusicList(serachMusicList);

                serachSingerList = LocalSDMusicUtils.serachSigers(SerachActivity.this,serachString);
                singerFragment.setSingerList(serachSingerList);

                serachZhuanJiList = LocalSDMusicUtils.serachZhuanJis(SerachActivity.this,serachString);
                zhuanJiFragment.setZhuanJiList(serachZhuanJiList);
                baseFragmentPagerAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void initData() {
        if(MediaPlayerManager.getInstance().checkCanPlay()) {
            MusicPlay musicPlay = new MusicPlay();
            musicPlay.setMusic(MediaPlayerManager.getInstance().currentOpenMusic());
            musicPlay.setQieHuan(true);
            updateUi(musicPlay);
        }
        baseEvent = new MessageEventTrigger() {
            @Override
            public void handleMessage(SuperMessage message) {
                updateUi((MusicPlay) message.object);
            }
        };
        MessageManager.getInstance().registerMessageEvent(MessageTag.OPEN_MUSIC_ID,baseEvent);
        updatePiFu(PiFuManager.getInstance().getPiFu());
    }

    @Override
    protected void destory() {
        MessageManager.getInstance().unRegisterMessageEvent(MessageTag.OPEN_MUSIC_ID,baseEvent);
    }

    @Override
    protected Boolean isSetVerticalScreen() {
        return true;
    }

    private void updateUi(MusicPlay musicPlay){
        if(musicPlay.isClear()){
            tvMusicName.setText("歌曲名");
            tvSingerName.setText("歌手名");
            ivPlayerIcon.setImageResource(R.mipmap.default_music_icon);
            ivPlayerBg.setImageResource(R.mipmap.default_bg);
            ivPlayerPause.setImageResource(R.mipmap.pause);
        }else {
            if (musicPlay.isQieHuan()) {
                //设置歌曲名和歌手名
                tvMusicName.setText(MediaPlayerManager.getInstance().currentOpenMusic().getName());
                tvSingerName.setText(MediaPlayerManager.getInstance().currentOpenMusic().getArtist());

                //设置歌曲图片
                Bitmap bitmap = MediaPlayerManager.getInstance().getCurrentOpenMusicBitmap(this);
                if (bitmap == null) {
                    //设置默认图片
                    ivPlayerIcon.setImageResource(R.mipmap.default_music_icon);
                    ivPlayerBg.setImageResource(R.mipmap.default_bg);
                } else {
                    ivPlayerIcon.setImageBitmap(bitmap);
                    ivPlayerBg.setImageBitmap(MediaPlayerManager.getInstance().getGaoShiBitmap(this));
                }
            }

            //更新播放/暂停状态
            if (MediaPlayerManager.getInstance().getMediaPlayer().isPlaying()) {
                ivPlayerPause.setImageResource(R.mipmap.play);
            } else {
                ivPlayerPause.setImageResource(R.mipmap.pause);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_player_mb:
                Intent intent = new Intent(SerachActivity.this,PlayActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_player_pause:
                if (MediaPlayerManager.getInstance().checkCanPlay()) {
                    MediaPlayerManager.getInstance().pauseAndPlay();
                } else {

                }
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_cancel:
                editText.setText("");
                break;
            case R.id.iv_player_list:
                PlayListDialog playListDialog = new PlayListDialog(SerachActivity.this);
                playListDialog.showAtLocation(rl, Gravity.BOTTOM,0,0);
                break;
        }
    }

    private void updatePiFu(PiFu piFu){
        if(piFu != null) {
            ImageView imageView = findViewById(R.id.iv_pi_fu);
            if (piFu.isGuDinPiFu()) {
                imageView.setImageResource(piFu.getPiFuIconId());
            } else {
                //使用Glide加载本地图片
                Glide.with(SerachActivity.this).load(piFu.getImagePath()).into(imageView);
            }
        }
    }
}
