package com.ellen.musicplayer.ui.activity;

import android.widget.TextView;

import com.ellen.musicplayer.R;

public class DinShiActivity extends BaseMediaPlayerActivity {

    private TextView tvTest;

    @Override
    protected void update() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        tvTest = findViewById(R.id.tv_test);
        tvTest.setText("妈妈皮");
    }

    @Override
    protected void destory() {

    }

    @Override
    protected int layoutId() {
        return R.layout.activity_test;
    }
}
