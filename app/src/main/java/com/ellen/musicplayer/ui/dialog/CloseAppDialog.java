package com.ellen.musicplayer.ui.dialog;

import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.ellen.musicplayer.R;
import com.ellen.musicplayer.base.BaseDialogFragment;
import com.ellen.musicplayer.utils.ZhuoSeUtils;

import java.util.Timer;
import java.util.TimerTask;

public class CloseAppDialog extends BaseDialogFragment {

    private final int TIME = 10;
    private long closeTime = 0;
    private long startTime = 0;
    private Handler handler = new Handler();
    private Timer timer = new Timer();

    private TextView tvTitle,tvContent,tvOk,tvCancel;

    private Callback callback;

    public CloseAppDialog(long startTime,Callback callback) {
        this.callback = callback;
        this.startTime = startTime;
        closeTime = startTime + TIME * 1000;
    }

    @Override
    protected void initData() {

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
               if(System.currentTimeMillis() >= closeTime){
                   handler.post(new Runnable() {
                       @Override
                       public void run() {
                           callback.stop();
                           timer.cancel();
                           CloseAppDialog.this.dismiss();
                       }
                   });
               }else {
                   handler.post(new Runnable() {
                       @Override
                       public void run() {
                           //更新时间
                           updateTimeUi();
                       }
                   });
               }
            }
        };
        //793063 -> 856064
        // 定义每次执行的间隔时间
        long intevalPeriod = 50;
        // schedules the task to be run in an interval
        // 安排任务在一段时间内运行
        timer.scheduleAtFixedRate(task, 0, intevalPeriod);
    }

    private void updateTimeUi(){
        int s = TIME - (int) ((System.currentTimeMillis() - startTime)/1000);
        String content = "还有:"+s+"秒后停止播放";
        tvContent.setText(ZhuoSeUtils.getSpannable(content,String.valueOf(s)));
    }

    @Override
    protected void initView() {
        tvTitle = findViewById(R.id.tv_title);
        tvContent = findViewById(R.id.tv_content);
        tvOk = findViewById(R.id.tv_ok);
        tvCancel = findViewById(R.id.tv_cancel);
        updateTimeUi();
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.ok();
                CloseAppDialog.this.dismiss();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
                callback.cancel();
                CloseAppDialog.this.dismiss();
            }
        });
    }

    @Override
    protected int setLayout() {
        return R.layout.dialog_close_app;
    }

    @Override
    protected Boolean setCancelable() {
        return false;
    }

    @Override
    protected Boolean setCanceledOnTouchOutside() {
        return false;
    }

    @Override
    protected Boolean setWinowTransparent() {
        return true;
    }

    public interface Callback {
        void ok();
        void stop();
        void cancel();
    }
}
