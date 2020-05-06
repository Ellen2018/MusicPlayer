package com.ellen.musicplayer.base.adapter.viewpager;

import android.os.Bundle;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * 用于ViewPager的适配器，用于保存Fragment的状态，系统原生的做不到
 */
public class SaveStatusFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList;
    private WeakReference<AppCompatActivity> activityWeakReference;
    private ViewPager viewPager;

    public SaveStatusFragmentPagerAdapter(ViewPager viewPager, AppCompatActivity appCompatActivity, List<Fragment> fragmentPagerAdapterList){
        super(appCompatActivity.getSupportFragmentManager());
        this.activityWeakReference = new WeakReference<>(appCompatActivity);
        this.fragmentList = fragmentPagerAdapterList;
        this.viewPager = viewPager;
        this.viewPager.setAdapter(this);
    }

    private SaveStatusFragmentPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    private SaveStatusFragmentPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = fragmentList.get(position);
        Bundle bundle = new Bundle();
        bundle.putString("id","" + position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // 将实例化的fragment进行显示即可。
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        activityWeakReference.get().getSupportFragmentManager().beginTransaction().show(fragment).commit();
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return super.getPageTitle(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//            super.destroyItem(container, position, object);// 注释父类方法
        Fragment fragment = fragmentList.get(position);// 获取要销毁的fragment
        activityWeakReference.get().getSupportFragmentManager().beginTransaction().hide(fragment).commit();// 将其隐藏即可，并不需要真正销毁，这样fragment状态就得到了保存
    }

}
