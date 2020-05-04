package com.ellen.musicplayer.dialog;

import android.view.View;
import android.widget.TextView;

import com.ellen.musicplayer.R;
import com.ellen.musicplayer.base.BaseDialogFragment;

public class CommonOkCancelDialog extends BaseDialogFragment {

    private TextView tvContent, tvOk, tvCancel, tvTitle;
    private String content;
    private Callback callback;
    private String title;
    private String okContent;
    private String cancelCotent;

    public void setOkContent(String okContent) {
        this.okContent = okContent;
    }

    public CommonOkCancelDialog(String title, String content, Callback callback) {
        this.title = title;
        this.content = content;
        this.callback = callback;
    }

    @Override
    protected void initData() {
        tvTitle = findViewById(R.id.tv_title);
        tvContent = findViewById(R.id.tv_content);
        tvOk = findViewById(R.id.tv_ok);
        tvCancel = findViewById(R.id.tv_cancel);
        tvTitle.setText(title);
        tvContent.setText(content);
        if(this.okContent != null){
            tvOk.setText(okContent);
        }
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.ok();
                CommonOkCancelDialog.this.dismiss();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.cancel();
                CommonOkCancelDialog.this.dismiss();
            }
        });
    }

    @Override
    protected void initView() {

    }

    @Override
    protected int setLayout() {
        return R.layout.dialog_common_ok_cancel;
    }

    @Override
    protected Boolean setCancelable() {
        return true;
    }

    @Override
    protected Boolean setCanceledOnTouchOutside() {
        return true;
    }

    @Override
    protected Boolean setWinowTransparent() {
        return true;
    }

    public interface Callback {
        void ok();

        void cancel();
    }
}
