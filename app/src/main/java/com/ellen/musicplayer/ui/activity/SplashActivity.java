package com.ellen.musicplayer.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.ellen.library.library.runmode.RunMode;
import com.ellen.library.library.serial.Receiver;
import com.ellen.library.library.serial.Sender;
import com.ellen.library.library.serial.commoninterface.sender.SenderController;
import com.ellen.musicplayer.MessageTag;
import com.ellen.musicplayer.R;
import com.ellen.musicplayer.base.BaseActivity;
import com.ellen.musicplayer.bean.DinShiBean;
import com.ellen.musicplayer.utils.statusutil.StatusUtils;
import com.ellen.supermessagelibrary.MessageManager;
import com.ellen.supermessagelibrary.SuperMessage;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends BaseActivity {

    private TextView tvJumpToMain;
    private boolean isClickJumpByUser = false;
    private final static int JUMP_ALL_TIME = 1;
    private long startTime;

    @Override
    protected void setStatus() {
        StatusUtils.setNoActionBar(this);
        StatusUtils.setFullScreen(this);
        //StatusUtils.setTranslucentStatus(this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {
        tvJumpToMain = findViewById(R.id.tv_jump_to_main);
        tvJumpToMain.setVisibility(View.GONE);
        tvJumpToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClickJumpByUser = true;
                jumpToMain();
            }
        });
    }

    private void jumpToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
               if(isClickJumpByUser){
                   timer.cancel();
               }else {
                   handler.post(new Runnable() {
                       @Override
                       public void run() {
                           if(System.currentTimeMillis() - startTime >= JUMP_ALL_TIME * 1000){
                               timer.cancel();
                               jumpToMain();
                           }else {
                               int miao = JUMP_ALL_TIME - (int)((System.currentTimeMillis() - startTime)/1000);
                               tvJumpToMain.setText("跳过"+miao);
                           }
                       }
                   });
               }
            }
        };
        //793063 -> 856064
        // 定义每次执行的间隔时间
        long intevalPeriod = 500;
        // schedules the task to be run in an interval
        // 安排任务在一段时间内运行
        startTime = System.currentTimeMillis();
        timer.scheduleAtFixedRate(task, 0, intevalPeriod);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void destory() {

    }

    @Override
    protected Boolean isSetVerticalScreen() {
        return true;
    }
}
