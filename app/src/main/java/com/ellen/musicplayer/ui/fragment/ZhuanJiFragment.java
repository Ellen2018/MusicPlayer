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
import com.ellen.musicplayer.bean.ZhuanJi;
import com.ellen.musicplayer.utils.LocalSDMusicUtils;

import java.util.List;

public class ZhuanJiFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private List<ZhuanJi> zhuanJiList;
    private ZhuanJiAdapter zhuanJiAdapter;

    @Override
    protected void initData() {
        new Sender<List<ZhuanJi>>(){
            @Override
            protected void handlerInstruction(SenderController<List<ZhuanJi>> senderController) {
                zhuanJiList = LocalSDMusicUtils.getAlbum(getContext());
                senderController.sendMessageToNext(zhuanJiList);
            }
        }.runOn(RunMode.NEW_THREAD)
                .setReceiver(new Receiver<List<ZhuanJi>>() {
            @Override
            protected void handleMessage(List<ZhuanJi> message) {
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(zhuanJiAdapter = new ZhuanJiAdapter(getActivity(),message));
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
}
