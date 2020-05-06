package com.ellen.musicplayer.ui.activity;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.ellen.musicplayer.MessageTag;
import com.ellen.musicplayer.R;
import com.ellen.musicplayer.base.adapter.viewpager.SaveStatusFragmentPagerAdapter;
import com.ellen.musicplayer.bean.SerachBean;
import com.ellen.musicplayer.bean.SerachMessage;
import com.ellen.musicplayer.ui.fragment.SerachDanQuFragment;
import com.ellen.musicplayer.ui.fragment.SerachGeDanFragment;
import com.ellen.musicplayer.ui.fragment.SerachSingerFragment;
import com.ellen.musicplayer.ui.fragment.SerachZhuanJiFragment;
import com.ellen.musicplayer.utils.statusutil.StatusUtils;
import com.ellen.supermessagelibrary.MessageManager;
import com.ellen.supermessagelibrary.SuperMessage;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class SerachActivity extends BaseMediaPlayerActivity implements View.OnClickListener {


    private ImageView ivBack, ivCancel;
    private EditText editText;
    private TextView tvSerachNull;
    private TabLayout tabLayout;

    private ViewPager viewPager;
    private RelativeLayout rl;
    private List<Fragment> fragmentList;
    private String[] titles = {"单曲", "歌手", "专辑", "歌单"};
    private SerachDanQuFragment serachDanQuFragment;
    private SerachSingerFragment serachSingerFragment;
    private SerachZhuanJiFragment serachZhuanJiFragment;
    private SerachGeDanFragment serachGeDanFragment;

    @Override
    protected void setStatus() {
        StatusUtils.setNoActionBar(this);
        StatusUtils.setTranslucentStatus(this);
    }

    @Override
    protected void update() {

    }

    @Override
    protected int layoutId() {
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
        fragmentList.add(serachDanQuFragment = new SerachDanQuFragment());
        fragmentList.add(serachSingerFragment = new SerachSingerFragment());
        fragmentList.add(serachZhuanJiFragment = new SerachZhuanJiFragment());
        fragmentList.add(serachGeDanFragment = new SerachGeDanFragment());

        SaveStatusFragmentPagerAdapter saveStatusFragmentPagerAdapter = new
                SaveStatusFragmentPagerAdapter(viewPager, this, fragmentList) {

                    @Nullable
                    @Override
                    public CharSequence getPageTitle(int position) {
                        return titles[position];
                    }
                };

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                String serachTag = editText.getText().toString();
                switch (position){
                    case 0:
                        serachDanQuFragment.setNewSerachTag(serachTag);
                        break;
                    case 1:
                        serachSingerFragment.setNewSerachTag(serachTag);
                        break;
                    case 2:
                        serachZhuanJiFragment.setNewSerachTag(serachTag);
                        break;
                    case 3:
                        serachGeDanFragment.setNewSerachTag(serachTag);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

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
                if (TextUtils.isEmpty(serachString)) {
                    ivCancel.setVisibility(View.GONE);
                } else {
                    ivCancel.setVisibility(View.VISIBLE);
                }
                switch (viewPager.getCurrentItem()){
                    case 0:
                        serachDanQuFragment.setNewSerachTag(serachString);
                        break;
                    case 1:
                        serachSingerFragment.setNewSerachTag(serachString);
                        break;
                    case 2:
                        serachZhuanJiFragment.setNewSerachTag(serachString);
                        break;
                    case 3:
                        serachGeDanFragment.setNewSerachTag(serachString);
                        break;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void destory() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_cancel:
                editText.setText("");
                break;
        }
    }

}
