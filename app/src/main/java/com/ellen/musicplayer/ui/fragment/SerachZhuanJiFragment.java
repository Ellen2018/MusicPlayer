package com.ellen.musicplayer.ui.fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ellen.library.library.runmode.RunMode;
import com.ellen.library.library.serial.Receiver;
import com.ellen.library.library.serial.Sender;
import com.ellen.library.library.serial.commoninterface.sender.SenderController;
import com.ellen.musicplayer.R;
import com.ellen.musicplayer.adapter.ZhuanJiAdapter;
import com.ellen.musicplayer.base.BaseFragment;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseRecyclerViewAdapter;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseViewHolder;
import com.ellen.musicplayer.bean.ZhuanJi;
import com.ellen.musicplayer.utils.JumpSortUtils;
import com.ellen.musicplayer.utils.LocalSDMusicUtils;

import java.util.List;

public class SerachZhuanJiFragment extends BaseFragment implements BaseFragment.LazyLoadInterface {

    private RecyclerView recyclerView;
    private List<ZhuanJi> zhuanJiList;
    private ZhuanJiAdapter zhuanJiAdapter;
    private String newSerachTag = null,currentSerachTag = null;

    public void setNewSerachTag(String newSerachTag) {
        if(isVisibleToUser){
            this.newSerachTag = newSerachTag;
            updateUi(newSerachTag);
        }else {
            this.newSerachTag = newSerachTag;
        }
    }

    @Override
    protected void initData() {
    }

    public void updateUi(String newSerachTag){
        new Sender<List<ZhuanJi>>(){
            @Override
            protected void handlerInstruction(SenderController<List<ZhuanJi>> senderController) {
                if(newSerachTag == null){
                    currentSerachTag = "";
                    List<ZhuanJi> zhuanJiList = LocalSDMusicUtils.serachZhuanJis(getActivity(),currentSerachTag);
                    senderController.sendMessageToNext(zhuanJiList);
                }else {
                    if(!newSerachTag.equals(currentSerachTag)){
                        currentSerachTag = newSerachTag;
                        List<ZhuanJi> zhuanJiList = LocalSDMusicUtils.serachZhuanJis(getActivity(),currentSerachTag);
                        senderController.sendMessageToNext(zhuanJiList);
                    }
                }
            }
        }.runOn(RunMode.NEW_THREAD)
                .setReceiver(new Receiver<List<ZhuanJi>>() {
                    @Override
                    protected void handleMessage(List<ZhuanJi> message) {
                        if(zhuanJiAdapter == null) {
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            recyclerView.setAdapter(zhuanJiAdapter = new ZhuanJiAdapter(getActivity(), recyclerView, message));
                            zhuanJiAdapter.setSerachTag(currentSerachTag);
                            zhuanJiAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(BaseViewHolder baseViewHolder, int position) {
                                    ZhuanJi zhuanJi = zhuanJiAdapter.getDataList().get(position);
                                    JumpSortUtils.jumpToSort(getActivity(), "专辑", zhuanJi.getName(), zhuanJi.getMusicList());
                                }
                            });
                        }else {
                            zhuanJiAdapter.getDataList().clear();
                            zhuanJiAdapter.getDataList().addAll(message);
                            zhuanJiAdapter.setSerachTag(currentSerachTag);
                            zhuanJiAdapter.notifyDataSetChanged();
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
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void initView() {
        recyclerView = findViewById(R.id.recycler_view);
    }

    @Override
    protected int setLayout() {
        return R.layout.fragment_zhuan_ji;
    }

    @Override
    public void lazyLoad() {
        updateUi(newSerachTag);
    }
}
