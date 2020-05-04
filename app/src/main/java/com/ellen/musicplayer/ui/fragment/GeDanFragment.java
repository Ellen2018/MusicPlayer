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

public class GeDanFragment extends BaseFragment {

    private List<GeDan> geDanList;
    private RecyclerView recyclerView;
    private GeDanManagerAdapter geDanManagerAdapter;
    private BaseEvent baseEvent;
    private String serachString = "";

    public List<GeDan> getGeDanList() {
        return geDanList;
    }

    public GeDanFragment(List<GeDan> geDanList) {
        this.geDanList = geDanList;
    }

    public void setGeDanList(String serachString) {
        this.serachString = serachString;
        String whereSql = Where.getInstance(false).addAndWhereValue("geDanName",
                WhereSymbolEnum.LIKE,"%"+serachString+"%").createSQL();

        List<GeDan> geDans = SQLManager.getInstance()
                .getGeDanTable()
                .serach(whereSql,Order.getInstance(false)
                        .setFirstOrderFieldName("geDanSqlTableName")
                        .setIsDesc(true).createSQL());

        this.geDanList.clear();
        this.geDanList.addAll(geDans);
        if(geDanManagerAdapter != null)
            geDanManagerAdapter.notifyDataSetChanged();
    }

    @Override
    protected void initData() {
        baseEvent = new MessageEventTrigger() {
            @Override
            public void handleMessage(SuperMessage message) {
                 setGeDanList(serachString);
            }
        };
        MessageManager.getInstance().registerMessageEvent(MessageTag.GE_DAN_ID,baseEvent);
          new Sender<List<GeDan>>(){
              @Override
              protected void handlerInstruction(SenderController<List<GeDan>> senderController) {
                  setGeDanList(serachString);
                  senderController.sendMessageToNext(geDanList);
              }
          }.runOn(RunMode.NEW_THREAD)
                  .setReceiver(new Receiver<List<GeDan>>() {
                      @Override
                      protected void handleMessage(List<GeDan> message) {
                          recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                          recyclerView.setAdapter(geDanManagerAdapter = new GeDanManagerAdapter(getActivity(),recyclerView,message));
                          geDanManagerAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
                              @Override
                              public void onItemClick(BaseViewHolder baseViewHolder, int position) {
                                  List<GeDanMusic> geDanMusicList = SQLManager.getInstance().getGeDanMusicListByName(geDanList.get(position));
                                  List<Music> musicList = new ArrayList<>();
                                  for(GeDanMusic geDanMusic:geDanMusicList){
                                      musicList.add(geDanMusic.getMusic());
                                  }
                                  JumpSortUtils.jumpToSort(getActivity(),"歌单",geDanList.get(position).getGeDanName(),musicList);

                              }
                          });
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
        MessageManager.getInstance().unRegisterMessageEvent(MessageTag.GE_DAN_ID,baseEvent);
    }
}
