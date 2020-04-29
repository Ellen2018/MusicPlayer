package com.ellen.musicplayer.manager.pifu;

import com.ellen.musicplayer.bean.PiFu;

public class PiFuManager {

    private static volatile PiFuManager piFuManager;
    private PiFu piFu;

    private PiFuManager(){}


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
        return piFu;
    }

    public void setPiFu(PiFu piFu) {
        this.piFu = piFu;
    }
}
