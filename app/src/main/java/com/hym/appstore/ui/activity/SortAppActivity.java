package com.hym.appstore.ui.activity;

import android.content.Intent;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.hym.appstore.R;
import com.hym.appstore.bean.SortBean;
import com.hym.appstore.common.Constant;
import com.hym.appstore.dagger2.component.AppComponent;
import com.hym.appstore.ui.adapter.SortAppViewPagerAdapter;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.ionicons_typeface_library.Ionicons;

import butterknife.BindView;

public class SortAppActivity extends BaseActivity {


    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.tool_bar)
    Toolbar mToolBar;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    private SortBean mSortBean;

    private int sortId = 0;

    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_sort_app;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public void init() {
        getData();
    }

    @Override
    public void initView() {
        mToolBar.setNavigationIcon(
                new IconicsDrawable(this)
                        .icon(Ionicons.Icon.ion_ios_arrow_back)
                        .sizeDp(16)
                        .color(getResources().getColor(R.color.theme_black)
                        )
        );

        initTabLayout();
    }

    @Override
    public void initEvent() {
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void getData() {
        Intent intent = getIntent();
        mSortBean = (SortBean) intent.getSerializableExtra(Constant.CATEGORY);
    }

    private void initTabLayout(){
        SortAppViewPagerAdapter adapter = new SortAppViewPagerAdapter(getSupportFragmentManager(),mSortBean.getId());
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }



}
