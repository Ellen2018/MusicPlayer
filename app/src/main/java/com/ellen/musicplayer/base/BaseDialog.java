package com.ellen.musicplayer.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.ref.WeakReference;

public abstract class BaseDialog {

    private AlertDialog dialog;
    private WeakReference<Activity> activityWeakReference;
    private OnDismissListener onDismissListener;

    public BaseDialog(Activity activity) {
        activityWeakReference = new WeakReference<>(activity);
        init();
    }

    protected String getTag() {
        return getClass().getSimpleName();
    }

    private void init() {
        dialog = new AlertDialog.Builder(activityWeakReference.get()).create();
        View view = onCreateView();
        //设置布局
        dialog.setView(view);
        if (this instanceof ButterKnifeInterface) {
            ButterKnifeInterface butterKnifeInterface = (ButterKnifeInterface) this;
            butterKnifeInterface.initButterKnife(view);
        }
        if (setCancelable() != null) {
            dialog.setCancelable(setCancelable());
        }
        if (setCanceledOnTouchOutside() != null) {
            dialog.setCanceledOnTouchOutside(setCanceledOnTouchOutside());
        }
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (onDismissListener != null) {
                    onDismissListener.dismiss();
                }
            }
        });
    }

    public void show() {
        showBefore();
        if (isTypeToast() != null && isTypeToast()) {
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
        }
        dialog.show();
        onResume();
    }

    public void dismiss() {
        dissmissBefore();
        if (this instanceof ButterKnifeInterface) {
            ButterKnifeInterface butterKnifeInterface = (ButterKnifeInterface) this;
            butterKnifeInterface.unBindButterKnife();
        }
        dialog.dismiss();
        destory();
    }

    public Activity getActivity() {
        return activityWeakReference.get();
    }

    public Context getContext() {
        return activityWeakReference.get();
    }

    public OnDismissListener getOnDismissListener() {
        return onDismissListener;
    }

    public void setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    protected abstract View onCreateView();

    protected abstract void showBefore();

    protected abstract void onResume();

    protected abstract void dissmissBefore();

    protected abstract void destory();

    protected abstract Boolean setCancelable();

    protected abstract Boolean setCanceledOnTouchOutside();

    public interface ButterKnifeInterface {
        void initButterKnife(View view);

        void unBindButterKnife();
    }

    public interface OnDismissListener {
        void dismiss();
    }

    protected Boolean isTypeToast() {
        return null;
    }
}
