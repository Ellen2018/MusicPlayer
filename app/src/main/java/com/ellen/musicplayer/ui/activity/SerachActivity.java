package com.ellen.musicplayer.ui.activity;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.ellen.musicplayer.R;
import com.ellen.musicplayer.utils.statusutil.StatusUtils;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class SerachActivity extends BaseMediaPlayerActivity implements View.OnClickListener {


    private ImageView ivBack,ivCancel;
    private EditText editText;
    private TextView tvSerachNull;
    private TabLayout tabLayout;

    private ViewPager viewPager;
    private RelativeLayout rl;
    private List<Fragment> fragmentList;
    private String[] titles = {"单曲","歌手","专辑","歌单"};

    @Override
    protected void setStatus() {
        StatusUtils.setNoActionBar(this);
        StatusUtils.setTranslucentStatus(this);
    }

    @Override
    protected void update() {

    }

    @Override
    protected int layoutId() {
        return R.layout.activity_serach;
    }

    @Override
    protected void initView() {
        rl = findViewById(R.id.rl);
        ivBack = findViewById(R.id.iv_back);
        ivBack.setOnClickListener(this);
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);
        editText = findViewById(R.id.edit_text);
        ivCancel = findViewById(R.id.iv_cancel);
        ivCancel.setOnClickListener(this);
        tvSerachNull = findViewById(R.id.tv_serach_null);
        fragmentList = new ArrayList<>();


        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String serachString = s.toString();
                if(TextUtils.isEmpty(serachString)){
                    ivCancel.setVisibility(View.GONE);
                }else {
                    ivCancel.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void destory() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_cancel:
                editText.setText("");
                break;
        }
    }

}
