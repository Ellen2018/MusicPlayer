package com.ellen.musicplayer.message;

import com.ellen.musicplayer.bean.PiFu;

public class PiFuMessage {

    private PiFu piFu;

    public PiFu getPiFu() {
        if(piFu == null){
            //读取本地皮肤数据
        }
        return piFu;
    }

    public void setPiFu(PiFu piFu) {
        this.piFu = piFu;
    }
}
