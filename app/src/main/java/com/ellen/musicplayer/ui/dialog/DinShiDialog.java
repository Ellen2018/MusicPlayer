package com.ellen.musicplayer.ui.dialog;

import com.ellen.musicplayer.R;
import com.ellen.musicplayer.base.BaseDialogFragment;

public class DinShiDialog extends BaseDialogFragment {
    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected int setLayout() {
        return R.layout.dialog_din_shi;
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
}
