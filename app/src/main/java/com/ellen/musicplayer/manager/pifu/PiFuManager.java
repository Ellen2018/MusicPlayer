package com.ellen.musicplayer.manager.pifu;

import com.ellen.musicplayer.KeyValueTag;
import com.ellen.musicplayer.R;
import com.ellen.musicplayer.bean.PiFu;
import com.ellen.musicplayer.helper.MMKVHelper;
import com.google.gson.Gson;

public class PiFuManager {

    private static volatile PiFuManager piFuManager;
    private PiFu piFu;
    private MMKVHelper piFuMmkv;

    private PiFuManager(){
        piFuMmkv = new MMKVHelper(KeyValueTag.PI_FU_NAME);
    }


    public static PiFuManager getInstance(){
        if(piFuManager == null){
            synchronized (PiFuManager.class){
                if(piFuManager == null){
                    piFuManager = new PiFuManager();
                }
            }
        }
        return piFuManager;
    }

    public static PiFuManager getPiFuManager() {
        return piFuManager;
    }

    public static void setPiFuManager(PiFuManager piFuManager) {
        PiFuManager.piFuManager = piFuManager;
    }

    public PiFu getPiFu() {
        if(piFu == null) {
            String json = piFuMmkv.getValue(KeyValueTag.PI_FU_KEY, null);
            if (json == null) {
                piFu = new PiFu(true, null, false, R.mipmap.pi_fu_0, 0);
                piFuMmkv.save(KeyValueTag.PI_FU_KEY, new Gson().toJson(piFu));
            } else {
                piFu = new Gson().fromJson(json, PiFu.class);
            }
        }
        return piFu;
    }

    public void setPiFu(PiFu piFu) {
        this.piFu = piFu;
        //存储数据
        piFuMmkv.save(KeyValueTag.PI_FU_KEY,new Gson().toJson(piFu));
    }
}
