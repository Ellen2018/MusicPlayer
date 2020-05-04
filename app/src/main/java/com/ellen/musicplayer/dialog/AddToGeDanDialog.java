package com.ellen.musicplayer.dialog;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ellen.musicplayer.MessageTag;
import com.ellen.musicplayer.R;
import com.ellen.musicplayer.SQLTag;
import com.ellen.musicplayer.adapter.GeDanAdapter;
import com.ellen.musicplayer.base.BasePopwindow;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseRecyclerViewAdapter;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseViewHolder;
import com.ellen.musicplayer.bean.GeDan;
import com.ellen.musicplayer.bean.GeDanMusic;
import com.ellen.musicplayer.bean.Music;
import com.ellen.musicplayer.manager.sql.SQLManager;
import com.ellen.musicplayer.message.GeDanMessage;
import com.ellen.musicplayer.utils.ToastUtils;
import com.ellen.sqlitecreate.createsql.order.Order;
import com.ellen.supermessagelibrary.MessageManager;
import com.ellen.supermessagelibrary.SuperMessage;

import java.util.ArrayList;
import java.util.List;

public class AddToGeDanDialog extends BaseBottomPopWindow {

    private RecyclerView recyclerView;
    private List<GeDan> geDanList;
    private GeDanAdapter geDanAdapter;
    private List<Music> musicList;

    public AddToGeDanDialog(Activity activity, List<Music> musicList) {
        super(activity);
        this.musicList = musicList;
    }

    public AddToGeDanDialog(Activity activity) {
        super(activity);
    }

    @Override
    protected View onCreateView() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dilaog_add_to_ge_dan, null);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        geDanList = SQLManager.getInstance().getGeDanTable()
                .getAllDatas(Order.getInstance(false)
                        .setFirstOrderFieldName("geDanSqlTableName")
                        .setIsDesc(true).createSQL());

        GeDan geDan = new GeDan();
        geDan.setGeDanName("我喜欢");
        geDanList.add(0, geDan);

        recyclerView.setAdapter(geDanAdapter = new GeDanAdapter(getActivity(), geDanList));
        geDanAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseViewHolder baseViewHolder, int position) {
                if (position == 0) {
                    //我喜欢
                    List<GeDanMusic> geDanMusicList = new ArrayList<>();
                    for(Music music:musicList){
                        GeDanMusic geDanMusic = new GeDanMusic();
                        geDanMusic.setLikeTag(music.getWeiOneTag());
                        geDanMusic.setMusic(music);
                        geDanMusic.setLikeTime(System.currentTimeMillis());
                        geDanMusicList.add(geDanMusic);
                    }

                    SQLManager.getInstance().getLikeGeDanMusicTable().saveData(geDanMusicList);
                    ToastUtils.toast(getActivity(), "添加歌曲到<我喜欢>成功!");
                    MessageManager.getInstance().sendMainThreadMessage(MessageTag.LIKE_ID);
                } else {
                    //添加数据到歌单
                    SQLManager.getInstance().addMusicsToGeDan(musicList, geDanList.get(position));
                    //发送消息刷新
                    ToastUtils.toast(getActivity(), "添加歌曲到歌单成功!");
                    //发送歌单创建成功的消息
                    SuperMessage superMessage = new SuperMessage(MessageTag.GE_DAN_ID);
                    GeDanMessage geDanMessage = new GeDanMessage();
                    superMessage.object = geDanMessage;
                    MessageManager.getInstance().sendMainThreadMessage(superMessage);
                }
                dismiss();
            }
        });
        View createGeDanView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_create_ge_dan, null);
        geDanAdapter.addHeaderView(createGeDanView);
        createGeDanView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateGeDanDialog createGeDanDialog = new CreateGeDanDialog(musicList);
                FragmentActivity activity = (FragmentActivity) getActivity();
                createGeDanDialog.show(activity.getSupportFragmentManager(), "");
                dismiss();
            }
        });
        return view;
    }
}
