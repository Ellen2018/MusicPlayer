package com.ellen.musicplayer.ui.fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ellen.library.library.runmode.RunMode;
import com.ellen.library.library.serial.Receiver;
import com.ellen.library.library.serial.Sender;
import com.ellen.library.library.serial.commoninterface.sender.SenderController;
import com.ellen.musicplayer.R;
import com.ellen.musicplayer.adapter.SingerAdapter;
import com.ellen.musicplayer.base.BaseFragment;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseRecyclerViewAdapter;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseViewHolder;
import com.ellen.musicplayer.bean.Singer;
import com.ellen.musicplayer.utils.JumpSortUtils;
import com.ellen.musicplayer.utils.LocalSDMusicUtils;

import java.util.List;

public class SerachSingerFragment extends BaseFragment implements BaseFragment.LazyLoadInterface {

    private List<Singer> singerList;
    private RecyclerView recyclerView;
    private SingerAdapter singerAdapter;
    private String currentSerachTag = null,newSerachTag = null;

    public void setNewSerachTag(String serachTag) {
        if(isVisibleToUser) {
            this.newSerachTag = serachTag;
           updateUi(serachTag);
        }else {
            this.newSerachTag = serachTag;
        }
    }

    @Override
    protected void initData() {

    }

    public void updateUi(String newSerachTag){
        new Sender<List<Singer>>(){
            @Override
            protected void handlerInstruction(SenderController<List<Singer>> senderController) {
               if(newSerachTag == null){
                   currentSerachTag = "";
                   List<Singer> singerList = LocalSDMusicUtils.serachSigers(getActivity(),currentSerachTag);
                   senderController.sendMessageToNext(singerList);
               }else {
                   if(!newSerachTag.equals(currentSerachTag)){
                       currentSerachTag = newSerachTag;
                       List<Singer> singerList = LocalSDMusicUtils.serachSigers(getActivity(),currentSerachTag);
                       senderController.sendMessageToNext(singerList);
                   }
               }
            }
        }.runOn(RunMode.NEW_THREAD)
                .setReceiver(new Receiver<List<Singer>>() {
                    @Override
                    protected void handleMessage(List<Singer> message) {
                        if(singerAdapter == null) {
                            singerList = message;
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            recyclerView.setAdapter(singerAdapter = new SingerAdapter(getActivity(), recyclerView, singerList));
                            singerAdapter.setSerachTag(currentSerachTag);
                            singerAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(BaseViewHolder baseViewHolder, int position) {
                                    JumpSortUtils.jumpToSort(
                                            getActivity(),
                                            "歌手",
                                            singerAdapter.getDataList().get(position).getName(),
                                            singerAdapter.getDataList().get(position).getMusicList());
                                }
                            });
                        }else {
                            singerAdapter.getDataList().clear();
                            singerAdapter.getDataList().addAll(message);
                            singerAdapter.setSerachTag(currentSerachTag);
                            singerAdapter.notifyDataSetChanged();
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
        return R.layout.fragment_singer;
    }

    @Override
    public void lazyLoad() {
        updateUi(newSerachTag);
    }
}
