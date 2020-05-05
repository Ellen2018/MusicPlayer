package com.ellen.musicplayer.service;

public interface DinShiInterface {

    /**
     * 开始定时任务
     */
    void startDinShiTask(int m);

    /**
     * 退出定时任务
     */
    void cancelDinShiTask();

    /**
     * 完成定时任务
     */
    void competeDinShiTask();

    /**
     * 重新开启任务
     * @param m
     */
    void resetTask(int m);

}
