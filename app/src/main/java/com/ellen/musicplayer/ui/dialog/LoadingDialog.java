package com.ellen.musicplayer.ui.dialog;

import com.ellen.musicplayer.R;
import com.ellen.musicplayer.base.BaseDialogFragment;

public class LoadingDialog extends BaseDialogFragment {
    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected int setLayout() {
        return R.layout.dialog_loading;
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
}
