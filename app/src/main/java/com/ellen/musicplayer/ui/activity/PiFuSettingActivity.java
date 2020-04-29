package com.ellen.musicplayer.ui.activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ellen.musicplayer.R;
import com.ellen.musicplayer.adapter.PiFuSelectorAdapter;
import com.ellen.musicplayer.base.BaseActivity;
import com.ellen.musicplayer.bean.PiFu;
import com.ellen.musicplayer.manager.sql.SQLManager;
import com.ellen.musicplayer.utils.statusutil.StatusUtils;

import java.util.ArrayList;
import java.util.List;

public class PiFuSettingActivity extends BaseActivity {

    private RecyclerView recyclerViewPiFu;
    private List<PiFu> piFuList = null;

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

        //获取皮肤数据
        piFuList = SQLManager.getInstance().getPiFuTable().getAllDatas(null);
        if (piFuList == null) {
            piFuList = new ArrayList<>();
        }
        piFuList.add(0, new PiFu(null, false));
        piFuList.add(piFuList.size() - 1, new PiFu(null, true));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewPiFu.setLayoutManager(linearLayoutManager);
        PiFuSelectorAdapter piFuSelectorAdapter = new PiFuSelectorAdapter(this, piFuList);
        recyclerViewPiFu.setAdapter(piFuSelectorAdapter);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void destory() {

    }

    @Override
    protected Boolean isSetVerticalScreen() {
        return null;
    }
}
