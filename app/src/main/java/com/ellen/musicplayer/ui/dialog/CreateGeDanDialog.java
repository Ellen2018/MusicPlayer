package com.ellen.musicplayer.ui.dialog;

import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.ellen.musicplayer.MessageTag;
import com.ellen.musicplayer.R;
import com.ellen.musicplayer.base.BaseDialogFragment;
import com.ellen.musicplayer.bean.GeDan;
import com.ellen.musicplayer.bean.Music;
import com.ellen.musicplayer.manager.sql.SQLManager;
import com.ellen.musicplayer.message.GeDanMessage;
import com.ellen.musicplayer.utils.ToastUtils;
import com.ellen.sqlitecreate.createsql.helper.WhereSymbolEnum;
import com.ellen.sqlitecreate.createsql.where.Where;
import com.ellen.supermessagelibrary.MessageManager;
import com.ellen.supermessagelibrary.SuperMessage;

import java.util.List;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class CreateGeDanDialog extends BaseDialogFragment {

    private TextView tvOk, tvCancel, tvTitle;
    private EditText etGeDanName;
    private List<Music> musicList;
    private boolean isEditGeDan = false;
    private GeDan geDan;

    public CreateGeDanDialog() {

    }

    public CreateGeDanDialog(boolean isEditGeDan, GeDan geDan) {
        this.isEditGeDan = isEditGeDan;
        this.geDan = geDan;
    }

    public CreateGeDanDialog(List<Music> musicList) {
        this.musicList = musicList;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        etGeDanName = findViewById(R.id.et_ge_dan_name);
        tvOk = findViewById(R.id.tv_ok);
        tvCancel = findViewById(R.id.tv_cancel);
        tvTitle = findViewById(R.id.tv_title);
        if(isEditGeDan){
            tvTitle.setText("编辑歌单名");
            etGeDanName.setText(geDan.getGeDanName());
        }
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEditGeDan) {
                    editGeDan();
                } else {
                    createGeDan();
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

    private void editGeDan() {
        String geDanName = etGeDanName.getText().toString();
        if (geDanName.equals("我喜欢")) {
            ToastUtils.toast(getActivity(), "已存在歌单<" + geDanName + ">,修改失败!");
            return;
        }
        if (TextUtils.isEmpty(geDanName)) {
            ToastUtils.toast(getActivity(), "歌单名不能为空!");
        } else {
            //查询歌单是否存在
            boolean isUpdate = SQLManager.getInstance().isContansGeDanName(geDanName);
            if (isUpdate) {
                //不能修改
                ToastUtils.toast(getActivity(), "已存在歌单" + geDanName + ",创建失败!");
            } else {
                //进行修改
                geDan.setGeDanName(geDanName);
                SQLManager.getInstance().getGeDanTable().update(geDan, Where
                        .getInstance(false)
                        .addAndWhereValue("geDanSqlTableName", WhereSymbolEnum.EQUAL, geDan.getGeDanSqlTableName())
                        .createSQL());
                MessageManager.getInstance().sendEmptyMainThreadMessage(MessageTag.GE_DAN_ID);
                ToastUtils.toast(getActivity(),"歌单修改成功!");
                dismiss();
            }
        }
    }

    private void createGeDan() {
        String geDanName = etGeDanName.getText().toString();
        if (geDanName.equals("我喜欢")) {
            ToastUtils.toast(getActivity(), "已存在歌单" + geDanName + ",创建失败!");
            return;
        }
        if (TextUtils.isEmpty(geDanName)) {
            ToastUtils.toast(getActivity(), "歌单名不能为空!");
        } else {
            boolean isCreateSuccess = SQLManager.getInstance().createGeDan(geDanName, musicList);
            if (isCreateSuccess) {
                ToastUtils.toast(getActivity(), "歌单《" + geDanName + "》创建成功!");
                //发送歌单创建成功的消息
                SuperMessage superMessage = new SuperMessage(MessageTag.GE_DAN_ID);
                GeDanMessage geDanMessage = new GeDanMessage();
                superMessage.object = geDanMessage;
                MessageManager.getInstance().sendMainThreadMessage(superMessage);
                dismiss();
            } else {
                ToastUtils.toast(getActivity(), "已存在歌单" + geDanName + ",创建失败!");
            }
        }
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
        return true;
    }

    @Override
    public void dismiss() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
        if(imm != null){
            imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);
        }
        super.dismiss();
    }
}
