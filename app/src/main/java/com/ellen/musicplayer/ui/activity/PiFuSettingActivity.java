package com.ellen.musicplayer.ui.activity;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ellen.musicplayer.MessageTag;
import com.ellen.musicplayer.R;
import com.ellen.musicplayer.adapter.PiFuSelectorAdapter;
import com.ellen.musicplayer.base.BaseActivity;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseRecyclerViewAdapter;
import com.ellen.musicplayer.bean.PiFu;
import com.ellen.musicplayer.manager.pifu.PiFuManager;
import com.ellen.musicplayer.manager.sql.SQLManager;
import com.ellen.musicplayer.message.PiFuMessage;
import com.ellen.musicplayer.utils.ToastUtils;
import com.ellen.musicplayer.utils.statusutil.StatusUtils;
import com.ellen.sqlitecreate.createsql.delete.DeleteTableDataRow;
import com.ellen.supermessagelibrary.MessageManager;
import com.ellen.supermessagelibrary.SuperMessage;

import java.util.ArrayList;
import java.util.List;

public class PiFuSettingActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView recyclerViewPiFu;
    private List<PiFu> piFuList = null;
    private PiFuSelectorAdapter piFuSelectorAdapter;
    private ImageView ivBack, ivSave, ivPiFuIcon;
    private PiFu currentPiFu;

    @Override
    protected void setStatus() {
        StatusUtils.setNoActionBar(this);
        StatusUtils.setTranslucentStatus(this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_pi_fu;
    }

    @Override
    protected void initView() {
        recyclerViewPiFu = findViewById(R.id.recycler_View_pi_fu);
        ivBack = findViewById(R.id.iv_back);
        ivSave = findViewById(R.id.iv_save);
        ivPiFuIcon = findViewById(R.id.iv_pi_fu_icon);

        ivBack.setOnClickListener(this);
        ivSave.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        currentPiFu = PiFuManager.getInstance().getPiFu();
        piFuList = new ArrayList<>();
        piFuList.add(new PiFu(true, null, false, R.mipmap.pi_fu_0, 0));
        piFuList.add(new PiFu(true, null, false, R.mipmap.pi_fu_1, 1));
        piFuList.add(new PiFu(true, null, false, R.mipmap.pi_fu_2, 2));
        piFuList.add(new PiFu(true, null, false, R.mipmap.pi_fu_3, 3));

        //获取皮肤数据
        piFuList.addAll(SQLManager.getInstance().getPiFuTable().getAllDatas(null));
        if (piFuList == null) {
            piFuList = new ArrayList<>();
        }

        piFuList.add(piFuList.size(), new PiFu(false, null, true, R.mipmap.pi_fu_0, -1));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewPiFu.setLayoutManager(linearLayoutManager);
        piFuSelectorAdapter = new PiFuSelectorAdapter(this, piFuList);
        piFuSelectorAdapter.setSelectorPiFu(currentPiFu);
        updatePiFu(currentPiFu);
        recyclerViewPiFu.setAdapter(piFuSelectorAdapter);

        piFuSelectorAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder viewHolder, int position) {
                if (position == piFuList.size() - 1) {
                    //选取相册图片
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("image/*");
                    startActivityForResult(intent, 0);
                } else {
                    currentPiFu = piFuList.get(position);
                    //切换当前皮肤
                    if (currentPiFu.isGuDinPiFu()) {
                        updatePiFu(currentPiFu);
                    } else {
                        //使用Glide进行加载
                        Glide.with(PiFuSettingActivity.this).load(currentPiFu.getImagePath()).into(ivPiFuIcon);
                    }
                    piFuSelectorAdapter.setSelectorPiFu(currentPiFu);
                    piFuSelectorAdapter.notifyDataSetChanged();

                }
            }
        });

        piFuSelectorAdapter.setOnItemLongClickListener(new BaseRecyclerViewAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLoncClick(RecyclerView.ViewHolder viewHolder, int position) {
                PiFu piFu = piFuSelectorAdapter.getDataList().get(position);
                if (!piFu.isGuDinPiFu()) {
                    if (!piFu.isAddIcon()) {
                        SQLManager.getInstance().deletePiFu(piFu);
                        piFuSelectorAdapter.getDataList().remove(piFu);
                        piFuSelectorAdapter.notifyDataSetChanged();
                        ToastUtils.toast(PiFuSettingActivity.this, "皮肤删除成功!");
                    }else {
                        //选取相册图片
                        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        intent.setType("image/*");
                        startActivityForResult(intent, 0);
                    }
                } else {
                    ToastUtils.toast(PiFuSettingActivity.this, "固定皮肤无法删除");
                }
                return true;
            }
        });
    }

    @Override
    protected void destory() {

    }

    @Override
    protected Boolean isSetVerticalScreen() {
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_save:
                PiFuManager.getInstance().setPiFu(currentPiFu);
                ToastUtils.toast(PiFuSettingActivity.this, "修改皮肤成功!");
                //发送消息
                PiFuMessage piFuMessage = new PiFuMessage();
                piFuMessage.setPiFu(currentPiFu);
                SuperMessage superMessage = new SuperMessage(MessageTag.PI_FU_ID);
                superMessage.object = piFuMessage;
                MessageManager.getInstance().sendMainThreadMessage(superMessage);
                finish();
                break;
        }
    }

    private void updatePiFu(PiFu piFu) {
        if (piFu != null) {
            if (piFu.isGuDinPiFu()) {
                ivPiFuIcon.setImageResource(piFu.getPiFuIconId());
            } else {
                //使用Glide加载本地图片

                Glide.with(PiFuSettingActivity.this).load(piFu.getImagePath()).into(ivPiFuIcon);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            String imagePath = null;
            if (resultCode == Activity.RESULT_OK && data != null) {
                Uri uri = data.getData();
                if (DocumentsContract.isDocumentUri(this, uri)) {
                    // 如果是document类型的Uri，则通过document id处理
                    String docId = DocumentsContract.getDocumentId(uri);
                    if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                        String id = docId.split(":")[1];
                        // 解析出数字格式的id
                        String selection = MediaStore.Images.Media._ID + "=" + id;
                        imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
                    } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                        Uri contentUri = ContentUris.withAppendedId(Uri.parse("content: //downloads/public_downloads"), Long.valueOf(docId));
                        imagePath = getImagePath(contentUri, null);
                    }
                } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                    // 如果是content类型的Uri，则使用普通方式处理
                    imagePath = getImagePath(uri, null);
                } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                    // 如果是file类型的Uri，直接获取图片路径即可
                    imagePath = uri.getPath();
                }
            }
            if (imagePath != null) {
                PiFu piFu = new PiFu(false, imagePath, false, 0, System.currentTimeMillis());
                piFuSelectorAdapter.getDataList().add(piFuList.size() - 1, piFu);
                piFuSelectorAdapter.notifyDataSetChanged();
                //记录皮肤数据
                SQLManager.getInstance().getPiFuTable().saveData(piFu);
            } else {
                ToastUtils.toast(this, "添加失败!");
            }
        }
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

}
