package com.ellen.musicplayer.base;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public abstract class AppLifeListener implements Application.ActivityLifecycleCallbacks {

    private int lifeActivityCount = 0;
    private boolean isAppBack = false;

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        if(lifeActivityCount == 0){
            onFirstStartUp();
        }
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        lifeActivityCount++;
        if(isAppBack){
            //从后台切换至前台
            onSwitchLife();
            isAppBack = false;
        }
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
      lifeActivityCount--;
      if(lifeActivityCount == 0){
          //切换至后台了
          isAppBack = true;
          onSwitchBack();
      }
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {

    }

    /**
     * 应用首次启动的时候调用
     */
    protected abstract void onFirstStartUp();

    /**
     * 切换至后台时调用
     */
    protected abstract void onSwitchBack();

    /**
     * 从后台切换至前台时调用
     */
    protected abstract void onSwitchLife();
}
