package com.ellen.musicplayer.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.ellen.musicplayer.MessageTag;
import com.ellen.musicplayer.R;
import com.ellen.musicplayer.base.BaseActivity;
import com.ellen.musicplayer.base.adapter.viewpager.BaseFragmentPagerAdapter;
import com.ellen.musicplayer.bean.GeDan;
import com.ellen.musicplayer.bean.Music;
import com.ellen.musicplayer.bean.PiFu;
import com.ellen.musicplayer.bean.Singer;
import com.ellen.musicplayer.bean.ZhuanJi;
import com.ellen.musicplayer.ui.dialog.PlayListDialog;
import com.ellen.musicplayer.manager.mediaplayer.MediaPlayerManager;
import com.ellen.musicplayer.manager.pifu.PiFuManager;
import com.ellen.musicplayer.manager.sql.SQLManager;
import com.ellen.musicplayer.message.MusicPlay;
import com.ellen.musicplayer.ui.fragment.DanQuFragment;
import com.ellen.musicplayer.ui.fragment.GeDanFragment;
import com.ellen.musicplayer.ui.fragment.SingerFragment;
import com.ellen.musicplayer.ui.fragment.ZhuanJiFragment;
import com.ellen.musicplayer.utils.LocalSDMusicUtils;
import com.ellen.musicplayer.utils.statusutil.StatusUtils;
import com.ellen.sqlitecreate.createsql.order.Order;
import com.ellen.supermessagelibrary.BaseEvent;
import com.ellen.supermessagelibrary.MessageEventTrigger;
import com.ellen.supermessagelibrary.MessageManager;
import com.ellen.supermessagelibrary.SuperMessage;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class SerachActivity extends BaseMediaPlayerActivity implements View.OnClickListener {


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
    private BaseEvent geDanEvent = null;

    @Override
    protected void setStatus() {
        StatusUtils.setNoActionBar(this);
        StatusUtils.setTranslucentStatus(this);
    }

    @Override
    protected void update() {

    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_serach;
    }

    @Override
    protected void initView() {
        rl = findViewById(R.id.rl);
        ivBack = findViewById(R.id.iv_back);
        ivBack.setOnClickListener(this);
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);
        editText = findViewById(R.id.edit_text);
        ivCancel = findViewById(R.id.iv_cancel);
        ivCancel.setOnClickListener(this);
        tvSerachNull = findViewById(R.id.tv_serach_null);
        fragmentList = new ArrayList<>();
        fragmentList.add(danQuFragment = new DanQuFragment(serachMusicList = LocalSDMusicUtils.serachMusics(SerachActivity.this,"")));
        fragmentList.add(singerFragment = new SingerFragment(serachSingerList = LocalSDMusicUtils.serachSigers(SerachActivity.this,"")));
        fragmentList.add(zhuanJiFragment = new ZhuanJiFragment(serachZhuanJiList = LocalSDMusicUtils.serachZhuanJis(SerachActivity.this,"")));
        fragmentList.add(geDanFragment = new GeDanFragment(serachGeDanList = SQLManager.getInstance().getGeDanTable()
                .getAllDatas(Order.getInstance(false)
                        .setFirstOrderFieldName("geDanSqlTableName")
                        .setIsDesc(true).createSQL())));

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
                        resultString = titles[position]+"("+geDanFragment.getGeDanList().size()+")";
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
                danQuFragment.setMusicList(serachString,serachMusicList);

                serachSingerList = LocalSDMusicUtils.serachSigers(SerachActivity.this,serachString);
                singerFragment.setSingerList(serachString,serachSingerList);

                serachZhuanJiList = LocalSDMusicUtils.serachZhuanJis(SerachActivity.this,serachString);
                zhuanJiFragment.setZhuanJiList(serachString,serachZhuanJiList);

                geDanFragment.setGeDanList(serachString);
                baseFragmentPagerAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void initData() {
        geDanEvent = new MessageEventTrigger() {
            @Override
            public void handleMessage(SuperMessage message) {
                //这里无法及时更新数目，咋解决啊
                baseFragmentPagerAdapter.notifyDataSetChanged();
            }

            @Override
            public FragmentActivity bindActivity() {
                return SerachActivity.this;
            }
        };
        MessageManager.getInstance().registerMessageEvent(MessageTag.GE_DAN_ID,geDanEvent);
    }

    @Override
    protected void destory() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_cancel:
                editText.setText("");
                break;
        }
    }

}
