package com.ellen.musicplayer.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ellen.musicplayer.MessageTag;
import com.ellen.musicplayer.bean.DinShiBean;
import com.ellen.supermessagelibrary.MessageManager;
import com.ellen.supermessagelibrary.SuperMessage;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

public class DinShiService extends Service implements DinShiInterface {

    private Timer timer;
    private long closeTime = 0;
    private long startTime = 0;

    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new DinShiBinder(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void startDinShiTask(int m) {
        closeTime = System.currentTimeMillis() + m * 60 * 1000;
        if (timer == null) {
            timer = new Timer();
        }
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (System.currentTimeMillis() >= closeTime) {
                    competeDinShiTask();
                }else {
                    SuperMessage superMessage = new SuperMessage(MessageTag.DIN_SHI_COMPLETE);
                    DinShiBean dinShiBean = new DinShiBean();
                    dinShiBean.setComplete(false);
                    dinShiBean.setTime(closeTime - System.currentTimeMillis());
                    superMessage.object = dinShiBean;
                    MessageManager.getInstance().sendMainThreadMessage(superMessage);
                }
            }
        };
        //793063 -> 856064
        // 定义每次执行的间隔时间
        long intevalPeriod = 200;
        // schedules the task to be run in an interval
        // 安排任务在一段时间内运行
        startTime = System.currentTimeMillis();
        timer.scheduleAtFixedRate(task, 0, intevalPeriod);
    }

    @Override
    public void cancelDinShiTask() {
        if (timer != null) {
            timer.cancel();
        }
    }

    @Override
    public void competeDinShiTask() {
        SuperMessage superMessage = new SuperMessage(MessageTag.DIN_SHI_COMPLETE);
        DinShiBean dinShiBean = new DinShiBean();
        dinShiBean.setComplete(true);
        superMessage.object = dinShiBean;
        MessageManager.getInstance().sendMainThreadMessage(superMessage);
        cancelDinShiTask();
    }

    @Override
    public void resetTask(int m) {
        if(m == 0){
            timer.cancel();
            return;
        }
        closeTime = System.currentTimeMillis() + m * 60 * 1000;
    }


    public static class DinShiBinder extends Binder implements DinShiInterface {

        private WeakReference<DinShiService> dinShiServiceWeakReference;

        public DinShiBinder(DinShiService dinShiService) {
            dinShiServiceWeakReference = new WeakReference<>(dinShiService);
        }

        @Override
        public void startDinShiTask(int m) {
            dinShiServiceWeakReference.get().startDinShiTask(m);
        }

        @Override
        public void cancelDinShiTask() {
            dinShiServiceWeakReference.get().cancelDinShiTask();
        }

        @Override
        public void competeDinShiTask() {
            dinShiServiceWeakReference.get().competeDinShiTask();
        }

        @Override
        public void resetTask(int m) {
            dinShiServiceWeakReference.get().resetTask(m);
        }
    }

}
