package com.ellen.musicplayer.ui.activity;

import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ellen.library.library.runmode.RunMode;
import com.ellen.library.library.serial.Receiver;
import com.ellen.library.library.serial.Sender;
import com.ellen.library.library.serial.commoninterface.sender.SenderController;
import com.ellen.musicplayer.MessageTag;
import com.ellen.musicplayer.R;
import com.ellen.musicplayer.adapter.ManyChooseAdapter;
import com.ellen.musicplayer.base.BasePopwindow;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseRecyclerViewAdapter;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseViewHolder;
import com.ellen.musicplayer.bean.GeDan;
import com.ellen.musicplayer.bean.Music;
import com.ellen.musicplayer.manager.sql.SQLManager;
import com.ellen.musicplayer.ui.dialog.AddToGeDanDialog;
import com.ellen.musicplayer.manager.mediaplayer.MediaPlayerManager;
import com.ellen.musicplayer.ui.dialog.CommonOkCancelDialog;
import com.ellen.musicplayer.ui.dialog.LoadingDialog;
import com.ellen.musicplayer.utils.ToastUtils;
import com.ellen.supermessagelibrary.MessageManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ManyChooseActivity extends BaseMediaPlayerActivity implements View.OnClickListener {

    public static String MUSIC_ACTIVITY_MUSIC_LIST = "music_list";
    public static String MUSIC_ACTIVITY_IS_DELTE = "is_delete";
    public static String MUSCI_ACTIVITY_GE_DAN_JSON = "ge_dan_json";

    private ImageView ivBack;
    private List<Music> musicList;
    private RecyclerView recyclerView;
    private RelativeLayout rl;
    private ManyChooseAdapter manyChooseAdapter;
    private TextView tvAllChoose, tvAddTo,tvChooseCount,tvNextPlay,tvFanXuan,tvDelete;
    private boolean isAllChoose = false;
    private GeDan geDan;

    @Override
    protected void update() {

    }

    @Override
    protected int layoutId() {
        return R.layout.activity_music_list;
    }

    @Override
    protected void initView() {
        ivBack = findViewById(R.id.iv_back);
        ivBack.setOnClickListener(this);
        recyclerView = findViewById(R.id.recycler_view);
        tvAllChoose = findViewById(R.id.tv_all_choose);
        tvChooseCount = findViewById(R.id.tv_choose_count);
        tvNextPlay = findViewById(R.id.tv_next_play);
        tvFanXuan = findViewById(R.id.tv_fan_choose);
        tvDelete = findViewById(R.id.tv_delete);
        tvAddTo = findViewById(R.id.tv_add_to);
        rl = findViewById(R.id.rl);
        tvAllChoose.setOnClickListener(this);
        tvNextPlay.setOnClickListener(this);
        tvFanXuan.setOnClickListener(this);
        tvAddTo.setOnClickListener(this);
        tvDelete.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        musicList = (List<Music>) getIntent().getSerializableExtra(MUSIC_ACTIVITY_MUSIC_LIST);
        boolean isDelete = getIntent().getBooleanExtra(MUSIC_ACTIVITY_IS_DELTE,false);
        if(isDelete){
            tvDelete.setVisibility(View.VISIBLE);
            String json = getIntent().getStringExtra(MUSCI_ACTIVITY_GE_DAN_JSON);
            if(json != null){
                geDan = new Gson().fromJson(json,GeDan.class);
            }
        }else {
            tvDelete.setVisibility(View.GONE);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(manyChooseAdapter = new ManyChooseAdapter(this, musicList));

        manyChooseAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseViewHolder baseViewHolder, int position) {
                if(manyChooseAdapter.getMusicTreeMap().containsKey(position)){
                    //移除
                    manyChooseAdapter.getMusicTreeMap().remove(position);
                }else {
                    //添加
                    manyChooseAdapter.getMusicTreeMap().put(position, musicList.get(position));
                }
                isAllChoose = false;
                tvAllChoose.setText("全选");
                manyChooseAdapter.notifyDataSetChanged();
                updateCount();
            }
        });
        manyChooseAdapter.setChooseListener(new ManyChooseAdapter.ChooseListener() {
            @Override
            public void choose(int size) {
                updateCount();
            }
        });
    }

    private void updateCount(){
        if(manyChooseAdapter.getMusicTreeMap().size() > 0){
            tvChooseCount.setText("已经选择"+manyChooseAdapter.getMusicTreeMap().size()+"项");
        }else {
            tvChooseCount.setText("多重选择");
        }
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
                updateCount();
                break;
            case R.id.tv_next_play:
                List<Music> chooseMusicList = new ArrayList<>();
                for(Map.Entry<Integer, Music> entry : manyChooseAdapter.getMusicTreeMap().entrySet()){
                    Integer mapKey = entry.getKey();
                    Music mapValue = entry.getValue();
                    chooseMusicList.add(mapValue);
                }
                if(chooseMusicList.size() > 0) {
                    MediaPlayerManager.getInstance().addNextPlayMusics(chooseMusicList);
                    finish();
                }else {
                    ToastUtils.toast(this,"当前没有选择任何歌曲!");
                }
                break;
            case R.id.tv_add_to:
                chooseMusicList = new ArrayList<>();
                for(Map.Entry<Integer, Music> entry : manyChooseAdapter.getMusicTreeMap().entrySet()){
                    Integer mapKey = entry.getKey();
                    Music mapValue = entry.getValue();
                    chooseMusicList.add(mapValue);
                }
                if(chooseMusicList.size() > 0) {
                    AddToGeDanDialog addToGeDanDialog = new AddToGeDanDialog(ManyChooseActivity.this, chooseMusicList);
                    addToGeDanDialog.setOnDismissListener(new BasePopwindow.OnDismissListener() {
                        @Override
                        public void dissmiss() {

                        }
                    });
                    addToGeDanDialog.showAtLocation(rl, Gravity.BOTTOM, 0, 0);
                }else {
                    ToastUtils.toast(this,"当前没有选择任何歌曲!");
                }
                break;
            case R.id.tv_fan_choose:
                Map<Integer,Music> musicMap = manyChooseAdapter.getMusicTreeMap();
                Map<Integer,Music> newMusicMap = new TreeMap<>();
                //反选
                for(int i=0;i<musicList.size();i++){
                    if(!musicMap.containsKey(i)){
                        newMusicMap.put(i,musicList.get(i));
                    }
                }
                manyChooseAdapter.setMusicTreeMap(newMusicMap);
                manyChooseAdapter.notifyDataSetChanged();
                isAllChoose = false;
                tvAllChoose.setText("全选");
                updateCount();
                break;
            case R.id.tv_delete:
                chooseMusicList = new ArrayList<>();
                for(Map.Entry<Integer, Music> entry : manyChooseAdapter.getMusicTreeMap().entrySet()){
                    Integer mapKey = entry.getKey();
                    Music mapValue = entry.getValue();
                    chooseMusicList.add(mapValue);
                }
                if(chooseMusicList.size() > 0) {
                    String title = "移除歌曲";
                    //开始删除
                    if(geDan == null){
                        //从我喜欢里进行批量删除
                        String content = "确定从<我喜欢>中移除这"+chooseMusicList.size()+"首歌曲?";
                        CommonOkCancelDialog commonOkCancelDialog = new CommonOkCancelDialog(title, content, new CommonOkCancelDialog.Callback() {
                            @Override
                            public void ok() {
                                LoadingDialog loadingDialog = new LoadingDialog();
                                loadingDialog.show(getSupportFragmentManager(),"");
                                new Sender<String>(){
                                    @Override
                                    protected void handlerInstruction(SenderController<String> senderController) {
                                        for(Music music:chooseMusicList) {
                                            SQLManager.getInstance().removeLikeMusic(music);
                                        }
                                        senderController.sendMessageToNext("");
                                    }
                                }.runOn(RunMode.NEW_THREAD)
                                        .setReceiver(new Receiver() {
                                            @Override
                                            protected void handleMessage(Object message) {
                                                MessageManager.getInstance().sendEmptyMainThreadMessage(MessageTag.LIKE_ID);
                                                loadingDialog.dismiss();
                                                finish();
                                            }

                                            @Override
                                            protected void handleErrMessage(Throwable throwable) {

                                            }

                                            @Override
                                            protected void complete() {

                                            }
                                        }).runOn(RunMode.MAIN_THREAD).start();
                            }

                            @Override
                            public void cancel() {

                            }
                        });

                        commonOkCancelDialog.show(getSupportFragmentManager(),"");
                    }else {
                        //从歌单里进行删除
                        String content = "确定从<"+geDan.getGeDanName()+">中移除这"+chooseMusicList.size()+"首歌曲?";
                        CommonOkCancelDialog commonOkCancelDialog = new CommonOkCancelDialog(title, content, new CommonOkCancelDialog.Callback() {
                            @Override
                            public void ok() {
                                LoadingDialog loadingDialog = new LoadingDialog();
                                loadingDialog.show(getSupportFragmentManager(),"");
                                //此处需要弹出Loading进度条
                                new Sender<String>(){
                                    @Override
                                    protected void handlerInstruction(SenderController<String> senderController) {
                                        for(Music music:chooseMusicList) {
                                            SQLManager.getInstance().deleteMusicFromGeDan(geDan,music);
                                        }
                                        senderController.sendMessageToNext("");
                                    }
                                }.runOn(RunMode.NEW_THREAD)
                                        .setReceiver(new Receiver() {
                                            @Override
                                            protected void handleMessage(Object message) {
                                                MessageManager.getInstance().sendEmptyMainThreadMessage(MessageTag.GE_DAN_ID);
                                                loadingDialog.dismiss();
                                                finish();
                                            }

                                            @Override
                                            protected void handleErrMessage(Throwable throwable) {

                                            }

                                            @Override
                                            protected void complete() {

                                            }
                                        }).runOn(RunMode.MAIN_THREAD).start();

                            }

                            @Override
                            public void cancel() {

                            }
                        });
                        commonOkCancelDialog.show(getSupportFragmentManager(),"");
                    }
                }else {
                    ToastUtils.toast(this,"当前没有选择任何歌曲!");
                }
                break;
        }
    }

}
