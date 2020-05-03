package com.ellen.musicplayer.ui.activity;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.ellen.musicplayer.R;
import com.ellen.musicplayer.base.adapter.viewpager.BaseFragmentPagerAdapter;
import com.ellen.musicplayer.ui.fragment.AllNearFragment;
import com.ellen.musicplayer.ui.fragment.OneZhouNearFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class NearMusicActivity extends BaseMediaPlayerActivity {

    private TabLayout tabLayout;
    private String[] titles = {"本周","所有"};
    private ViewPager viewPager;
    private List<Fragment> fragmentList;
    private ImageView ivBack;

    @Override
    protected void update() {

    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_near_music;
    }

    @Override
    protected void initView() {
       viewPager = findViewById(R.id.viewpager);
       tabLayout = findViewById(R.id.tab_layout);
       ivBack = findViewById(R.id.iv_back);
       ivBack.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               finish();
           }
       });
       fragmentList = new ArrayList<>();
       fragmentList.add(new OneZhouNearFragment());
       fragmentList.add(new AllNearFragment());
       viewPager.setAdapter(new BaseFragmentPagerAdapter(getSupportFragmentManager()) {
           @Override
           protected int getFragmentPagerSize() {
               return titles.length;
           }

           @Override
           protected Fragment getFragment(int position) {
               return fragmentList.get(position);
           }

           @Nullable
           @Override
           public CharSequence getPageTitle(int position) {
               return titles[position];
           }
       });

       tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void destory() {

    }

    @Override
    protected Boolean isSetVerticalScreen() {
        return null;
    }
}
