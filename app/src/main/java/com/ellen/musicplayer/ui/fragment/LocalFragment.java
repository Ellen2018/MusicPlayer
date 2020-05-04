package com.ellen.musicplayer.ui.fragment;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.ellen.musicplayer.R;
import com.ellen.musicplayer.base.BaseFragment;
import com.ellen.musicplayer.base.adapter.viewpager.BaseFragmentPagerAdapter;
import com.ellen.musicplayer.utils.LocalSDMusicUtils;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class LocalFragment extends BaseFragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private List<String> titles;
    private List<Fragment> fragmentList;

    @Override
    protected void initData() {
        titles = new ArrayList<>();
        fragmentList = new ArrayList<>();
        titles.add("单曲");
        titles.add("歌手");
        titles.add("专辑");
        //titles.add("流派");
        titles.add("文件夹");
        fragmentList.add(new DanQuFragment(null));
        fragmentList.add(new SingerFragment(null));
        fragmentList.add(new ZhuanJiFragment(null));
        //fragmentList.add(new LiuPaiFragment());
        fragmentList.add(new FileFragment());

        viewPager.setAdapter(new BaseFragmentPagerAdapter(getFragmentManager()) {
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
                return titles.get(position);
            }
        });
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void initView() {
        tabLayout = findViewById(R.id.tl);
        viewPager = findViewById(R.id.vp_local);
    }

    @Override
    protected int setLayout() {
        return R.layout.fragment_local;
    }
}
