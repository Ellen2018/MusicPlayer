package com.ellen.musicplayer.dialog;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ellen.musicplayer.MessageTag;
import com.ellen.musicplayer.R;
import com.ellen.musicplayer.adapter.MusicAdapter;
import com.ellen.musicplayer.adapter.PlayMusicAdapter;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseRecyclerViewAdapter;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseViewHolder;
import com.ellen.musicplayer.manager.mediaplayer.MediaPlayerManager;
import com.ellen.supermessagelibrary.BaseEvent;
import com.ellen.supermessagelibrary.MessageEventTrigger;
import com.ellen.supermessagelibrary.MessageManager;
import com.ellen.supermessagelibrary.SuperMessage;

public class PlayListDialog extends BaseBottomPopWindow {

    private RecyclerView recyclerView;
    private PlayMusicAdapter playMusicAdapter;
    private BaseEvent baseEvent;
    private ImageView ivClear;

    public PlayListDialog(Activity activity) {
        super(activity);
    }

    @Override
    protected View onCreateView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_play_list,null);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(playMusicAdapter = new PlayMusicAdapter(getActivity(),recyclerView, MediaPlayerManager.getInstance().getPlayList()));
        playMusicAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseViewHolder baseViewHolder, int position) {
                MediaPlayerManager.getInstance().open(position,MediaPlayerManager.getInstance().getPlayList());
            }
        });
        baseEvent = new MessageEventTrigger() {
            @Override
            public void handleMessage(SuperMessage message) {
                playMusicAdapter.notifyDataSetChanged();
            }
        };
        MessageManager.getInstance().registerMessageEvent(MessageTag.OPEN_MUSIC_ID,baseEvent);

        ivClear = view.findViewById(R.id.iv_clear);
        ivClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayerManager.getInstance().clearPlayList();
                playMusicAdapter.notifyDataSetChanged();
            }
        });
        return view;
    }

    @Override
    protected void dismissBefore() {
        super.dismissBefore();
        MessageManager.getInstance().unRegisterMessageEvent(MessageTag.OPEN_MUSIC_ID,baseEvent);
    }
}
