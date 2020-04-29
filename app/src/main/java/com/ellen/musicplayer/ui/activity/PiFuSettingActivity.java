package com.ellen.musicplayer.ui.activity;

import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.ellen.supermessagelibrary.MessageManager;
import com.ellen.supermessagelibrary.SuperMessage;

import java.util.ArrayList;
import java.util.List;

public class PiFuSettingActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView recyclerViewPiFu;
    private List<PiFu> piFuList = null;
    private PiFuSelectorAdapter piFuSelectorAdapter;
    private ImageView ivBack,ivSave,ivPiFuIcon;
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

        piFuList = new ArrayList<>();
        piFuList.add(currentPiFu = new PiFu(true,null, false,R.mipmap.pi_fu_0,0));
        piFuList.add(new PiFu(true,null, false,R.mipmap.pi_fu_1,1));
        piFuList.add(new PiFu(true,null, false,R.mipmap.pi_fu_2,2));
        piFuList.add(new PiFu(true,null, false,R.mipmap.pi_fu_3,3));

        //获取皮肤数据
        piFuList.addAll(SQLManager.getInstance().getPiFuTable().getAllDatas(null));
        if (piFuList == null) {
            piFuList = new ArrayList<>();
        }

        piFuList.add(piFuList.size(), new PiFu(false,null, true,R.mipmap.pi_fu_0,-1));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewPiFu.setLayoutManager(linearLayoutManager);
        piFuSelectorAdapter = new PiFuSelectorAdapter(this, piFuList);
        recyclerViewPiFu.setAdapter(piFuSelectorAdapter);

        piFuSelectorAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder viewHolder, int position) {
                if(position == piFuList.size() - 1){
                    ToastUtils.toast(PiFuSettingActivity.this,"点击了添加");
                }else {
                    currentPiFu = piFuList.get(position);
                    //切换当前皮肤
                    if(currentPiFu.isGuDinPiFu()){
                        ivPiFuIcon.setImageResource(currentPiFu.getPiFuIconId());
                    }else {
                        //使用Glide进行加载
                    }
                    piFuSelectorAdapter.setSelectorPiFu(currentPiFu);
                    piFuSelectorAdapter.notifyDataSetChanged();

                }
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
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_save:
                PiFuManager.getInstance().setPiFu(currentPiFu);
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
}
