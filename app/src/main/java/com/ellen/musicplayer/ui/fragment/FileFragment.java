package com.ellen.musicplayer.ui.fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ellen.library.library.runmode.RunMode;
import com.ellen.library.library.serial.Receiver;
import com.ellen.library.library.serial.Sender;
import com.ellen.library.library.serial.commoninterface.sender.SenderController;
import com.ellen.musicplayer.R;
import com.ellen.musicplayer.adapter.FileMusicAdapter;
import com.ellen.musicplayer.base.BaseFragment;
import com.ellen.musicplayer.bean.FileMusic;
import com.ellen.musicplayer.utils.LocalSDMusicUtils;

import java.util.List;

public class FileFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private List<FileMusic> fileMusicList;

    @Override
    protected void initData() {
        new Sender<List<FileMusic>>(){
            @Override
            protected void handlerInstruction(SenderController<List<FileMusic>> senderController) {
                fileMusicList = LocalSDMusicUtils.getFileMusics(getActivity());
                senderController.sendMessageToNext(fileMusicList);
            }
        }.runOn(RunMode.NEW_THREAD)
                .setReceiver(new Receiver<List<FileMusic>>() {
                    @Override
                    protected void handleMessage(List<FileMusic> message) {
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerView.setAdapter(new FileMusicAdapter(getActivity(),recyclerView,message));
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
        return R.layout.fragment_file;
    }
}
