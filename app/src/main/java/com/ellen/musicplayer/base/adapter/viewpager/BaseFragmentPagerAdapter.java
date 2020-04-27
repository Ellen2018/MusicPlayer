package com.ellen.musicplayer.base.adapter.viewpager;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public abstract class BaseFragmentPagerAdapter extends FragmentPagerAdapter {

    public BaseFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return getFragment(position);
    }

    @Override
    public int getCount() {
        return getFragmentPagerSize();
    }

    protected abstract int getFragmentPagerSize();
    protected abstract Fragment getFragment(int position);
}
