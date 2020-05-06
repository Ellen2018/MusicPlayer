package com.ellen.musicplayer.ui.fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ellen.library.library.runmode.RunMode;
import com.ellen.library.library.serial.Receiver;
import com.ellen.library.library.serial.Sender;
import com.ellen.library.library.serial.commoninterface.sender.SenderController;
import com.ellen.musicplayer.MessageTag;
import com.ellen.musicplayer.R;
import com.ellen.musicplayer.adapter.GeDanManagerAdapter;
import com.ellen.musicplayer.base.BaseFragment;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseRecyclerViewAdapter;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseViewHolder;
import com.ellen.musicplayer.bean.GeDan;
import com.ellen.musicplayer.bean.GeDanMusic;
import com.ellen.musicplayer.bean.Music;
import com.ellen.musicplayer.manager.sql.SQLManager;
import com.ellen.musicplayer.ui.activity.GeDanManagerActivity;
import com.ellen.musicplayer.utils.JumpSortUtils;
import com.ellen.sqlitecreate.createsql.helper.WhereSymbolEnum;
import com.ellen.sqlitecreate.createsql.order.Order;
import com.ellen.sqlitecreate.createsql.where.Where;
import com.ellen.supermessagelibrary.BaseEvent;
import com.ellen.supermessagelibrary.MessageEventTrigger;
import com.ellen.supermessagelibrary.MessageManager;
import com.ellen.supermessagelibrary.SuperMessage;

import java.util.ArrayList;
import java.util.List;

public class SerachGeDanFragment extends BaseFragment implements BaseFragment.LazyLoadInterface {

    private List<GeDan> allGeDanList;
    private List<GeDan> geDanList;
    private RecyclerView recyclerView;
    private GeDanManagerAdapter geDanManagerAdapter;
    private BaseEvent baseEvent;
    private String currentSerachTag = null, newSerachTag = null;

    public void setNewSerachTag(String newSerachTag) {
        if(isVisibleToUser) {
            this.newSerachTag = newSerachTag;
            updateUi(newSerachTag);
        }else {
            this.newSerachTag = newSerachTag;
        }
    }

    private void updateUi(String newSerachTag) {
        new Sender<List<GeDan>>() {
            @Override
            protected void handlerInstruction(SenderController<List<GeDan>> senderController) {
                if (allGeDanList == null) {
                    allGeDanList = SQLManager.getInstance().getGeDanTable()
                            .getAllDatas(Order.getInstance(false)
                                    .setFirstOrderFieldName("geDanSqlTableName")
                                    .setIsDesc(true).createSQL());
                }
                if (newSerachTag == null) {
                    currentSerachTag = "";
                    List<GeDan> geDanList = serachGeDanListFromSql(currentSerachTag);
                    senderController.sendMessageToNext(geDanList);
                } else {
                    if(!newSerachTag.equals(currentSerachTag)){
                        currentSerachTag = newSerachTag;
                        List<GeDan> geDanList = serachGeDanListFromSql(currentSerachTag);
                        senderController.sendMessageToNext(geDanList);
                    }
                }

            }
        }.runOn(RunMode.NEW_THREAD)
                .setReceiver(new Receiver<List<GeDan>>() {
                    @Override
                    protected void handleMessage(List<GeDan> message) {
                        if (geDanManagerAdapter == null) {
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            recyclerView.setAdapter(geDanManagerAdapter = new GeDanManagerAdapter(getActivity(), recyclerView, message));
                            geDanManagerAdapter.setSerachTag(currentSerachTag);
                            geDanManagerAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(BaseViewHolder baseViewHolder, int position) {
                                    List<GeDanMusic> geDanMusicList = SQLManager.getInstance().getGeDanMusicListByName(geDanManagerAdapter.getDataList().get(position));
                                    List<Music> musicList = new ArrayList<>();
                                    for (GeDanMusic geDanMusic : geDanMusicList) {
                                        musicList.add(geDanMusic.getMusic());
                                    }
                                    JumpSortUtils.jumpToSort(getActivity(), "歌单", geDanManagerAdapter.getDataList().get(position).getGeDanName(), musicList);
                                }
                            });
                        } else {
                           geDanManagerAdapter.getDataList().clear();
                           geDanManagerAdapter.getDataList().addAll(message);
                           geDanManagerAdapter.setSerachTag(currentSerachTag);
                           geDanManagerAdapter.notifyDataSetChanged();
                        }
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
    protected void initData() {
        baseEvent = new MessageEventTrigger() {
            @Override
            public void handleMessage(SuperMessage message) {
                //搜索源变了
                allGeDanList = null;
                newSerachTag = currentSerachTag;
                currentSerachTag = null;
                updateUi(newSerachTag);
            }
        };
        MessageManager.getInstance().registerMessageEvent(MessageTag.GE_DAN_ID, baseEvent);
    }

    @Override
    protected void initView() {
        recyclerView = findViewById(R.id.recycler_view);
    }

    @Override
    protected int setLayout() {
        return R.layout.fragment_ge_dan;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MessageManager.getInstance().unRegisterMessageEvent(MessageTag.GE_DAN_ID, baseEvent);
    }

    @Override
    public void lazyLoad() {
        updateUi(newSerachTag);
    }

    private List<GeDan> serachGeDanListFromSql(String currentSerachTag) {
        List<GeDan> geDanList = new ArrayList<>();
        for (GeDan geDan : allGeDanList) {
            if (geDan.getGeDanName().contains(currentSerachTag)) {
                geDanList.add(geDan);
            }
        }
        return geDanList;
    }
}
