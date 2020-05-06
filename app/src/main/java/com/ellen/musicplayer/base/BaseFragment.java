package com.ellen.musicplayer.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment {

    protected View mContentView;
    protected boolean isVisibleToUser = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(setLayout(), container, false);
        if(this instanceof ButterKnifeInterface){
            ButterKnifeInterface butterKnifeInterface = (ButterKnifeInterface) this;
            butterKnifeInterface.initButterKnife(mContentView);
        }
        initView();
        initData();
        return mContentView;
    }

    protected <T extends View> T findViewById(int id){
        View view = mContentView.findViewById(id);
        return (T) view;
    }

    protected String getTAG(){
        return getClass().getSimpleName();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(this instanceof ButterKnifeInterface){
            ButterKnifeInterface butterKnifeInterface = (ButterKnifeInterface) this;
            butterKnifeInterface.unBindButterKnife();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        this.isVisibleToUser = isVisibleToUser;
        if(isVisibleToUser){
            if(this instanceof LazyLoadInterface){
                LazyLoadInterface lazyLoadInterface = (LazyLoadInterface) this;
                lazyLoadInterface.lazyLoad();
            }
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    protected abstract void initData();
    protected abstract void initView();
    protected abstract int setLayout();

    //支持ButterKnife的接口
    public interface ButterKnifeInterface {
        void initButterKnife(View view);
        void unBindButterKnife();
    }

    public interface LazyLoadInterface{
        void lazyLoad();
    }
}
