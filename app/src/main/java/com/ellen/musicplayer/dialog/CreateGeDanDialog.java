package com.ellen.musicplayer.dialog;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ellen.musicplayer.MessageTag;
import com.ellen.musicplayer.R;
import com.ellen.musicplayer.base.BaseDialogFragment;
import com.ellen.musicplayer.manager.sql.SQLManager;
import com.ellen.musicplayer.message.GeDanMessage;
import com.ellen.musicplayer.utils.ToastUtils;
import com.ellen.supermessagelibrary.MessageManager;
import com.ellen.supermessagelibrary.SuperMessage;

public class CreateGeDanDialog extends BaseDialogFragment {

    private TextView tvOk,tvCancel;
    private EditText etGeDanName;

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
       etGeDanName = findViewById(R.id.et_ge_dan_name);
       tvOk = findViewById(R.id.tv_ok);
       tvCancel = findViewById(R.id.tv_cancel);

       tvOk.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String geDanName = etGeDanName.getText().toString();
               if(TextUtils.isEmpty(geDanName)){
                   ToastUtils.toast(getActivity(),"歌单名不能为空!");
               }else {
                   boolean isCreateSuccess = SQLManager.getInstance().createGeDan(geDanName);
                   if(isCreateSuccess){
                       ToastUtils.toast(getActivity(),"歌单《"+geDanName+"》创建成功!");
                       //发送歌单创建成功的消息
                       SuperMessage superMessage = new SuperMessage(MessageTag.GE_DAN_ID);
                       GeDanMessage geDanMessage = new GeDanMessage();
                       superMessage.object = geDanMessage;
                       MessageManager.getInstance().sendMainThreadMessage(superMessage);
                       dismiss();
                   }else {
                       ToastUtils.toast(getActivity(),"已存在歌单"+geDanName+",创建失败!");
                   }
               }
           }
       });

       tvCancel.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               dismiss();
           }
       });
    }

    @Override
    protected int setLayout() {
        return R.layout.dialog_create_ge_dan;
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
        return false;
    }
}
