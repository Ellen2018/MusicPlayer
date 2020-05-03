package com.ellen.musicplayer.ui.activity;

import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ellen.musicplayer.R;
import com.ellen.musicplayer.adapter.ManyChooseAdapter;
import com.ellen.musicplayer.adapter.MusicAdapter;
import com.ellen.musicplayer.bean.Music;
import com.ellen.musicplayer.dialog.LeiBieDialog;
import com.ellen.musicplayer.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MusicListActivity extends BaseMediaPlayerActivity implements View.OnClickListener {

    public static String MUSIC_ACTIVITY_MUSIC_LIST = "music_list";

    private ImageView ivBack;
    private List<Music> musicList;
    private RecyclerView recyclerView;
    private RelativeLayout rl;
    private ManyChooseAdapter manyChooseAdapter;
    private TextView tvAllChoose, tvChooseCount,tvChooseOk;
    private boolean isAllChoose = false;

    @Override
    protected void update() {

    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_music_list;
    }

    @Override
    protected void initView() {
        ivBack = findViewById(R.id.iv_back);
        ivBack.setOnClickListener(this);
        recyclerView = findViewById(R.id.recycler_view);
        tvAllChoose = findViewById(R.id.tv_all_choose);
        tvChooseCount = findViewById(R.id.tv_choose_count);
        tvChooseOk = findViewById(R.id.tv_choose_ok);
        rl = findViewById(R.id.rl);
        tvAllChoose.setOnClickListener(this);
        tvChooseOk.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        musicList = (List<Music>) getIntent().getSerializableExtra(MUSIC_ACTIVITY_MUSIC_LIST);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(manyChooseAdapter = new ManyChooseAdapter(this, musicList));
        manyChooseAdapter.setChooseListener(new ManyChooseAdapter.ChooseListener() {
            @Override
            public void choose(int size) {
                if (size == 0) {
                    tvChooseCount.setText("多重选择");
                } else {
                    tvChooseCount.setText("已经选择" + size + "项");
                }
                if(size < musicList.size()){
                    isAllChoose = false;
                    tvAllChoose.setText("全选");
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
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_all_choose:
                isAllChoose = !isAllChoose;
                if (isAllChoose) {
                    for (int i = 0; i < musicList.size(); i++) {
                        manyChooseAdapter.getMusicTreeMap().put(i, musicList.get(i));
                    }
                    tvAllChoose.setText("取消全选");
                } else {
                    manyChooseAdapter.getMusicTreeMap().clear();
                    tvAllChoose.setText("全选");
                }
                manyChooseAdapter.notifyDataSetChanged();
                break;
            case R.id.tv_choose_ok:
                List<Music> musicList = new ArrayList<>();
                for(Map.Entry<Integer, Music> entry : manyChooseAdapter.getMusicTreeMap().entrySet()){
                    Integer mapKey = entry.getKey();
                    Music mapValue = entry.getValue();
                    musicList.add(mapValue);
                }
                int size = manyChooseAdapter.getMusicTreeMap().size();
                LeiBieDialog leiBieDialog = new LeiBieDialog(this,rl,"多重选择"
                        ,"已经选择" + size + "项",musicList);
                leiBieDialog.showAtLocation(rl, Gravity.BOTTOM,0,0);
                break;
        }
    }
}
