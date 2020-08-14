package com.hym.appstore.ui.activity;

import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.hym.appstore.R;
import com.hym.appstore.bean.FragmentInfo;
import com.hym.appstore.common.Constant;
import com.hym.appstore.dagger2.component.AppComponent;
import com.hym.appstore.ui.adapter.MyViewPagerAdapter;
import com.hym.appstore.ui.fragment.DownloadedFragment;
import com.hym.appstore.ui.fragment.DownloadingFragment;
import com.hym.appstore.ui.fragment.InstalledAppAppFragment;
import com.hym.appstore.ui.fragment.UpgradeAppFragment;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.ionicons_typeface_library.Ionicons;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class AppManagerActivity extends BaseActivity {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.main_tab_layout)
    TabLayout mainTabLayout;
    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @BindView(R.id.main_viewpager)
    ViewPager mainViewpager;


    private List<FragmentInfo> fragmentInfos;

    private int position;




    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_app_manager;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public void init() {
        toolbar.setNavigationIcon(
                new IconicsDrawable(this)
                        .icon(Ionicons.Icon.ion_ios_arrow_back)
                        .sizeDp(16)
                        .color(getResources().getColor(R.color.theme_black)
                        )
        );

    }


    @Override
    public void initView() {

        position = getIntent().getIntExtra(Constant.POSITION,0);

        //fragmentinfo 数据集合
        fragmentInfos =  new ArrayList<>(4);
        fragmentInfos.add(new FragmentInfo("下载", DownloadingFragment.class));
        fragmentInfos.add(new FragmentInfo("已完成", DownloadedFragment.class));
        fragmentInfos.add(new FragmentInfo("升级", UpgradeAppFragment.class));
        fragmentInfos.add(new FragmentInfo("已安装", InstalledAppAppFragment.class));

        MyViewPagerAdapter myViewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager(),fragmentInfos);
        mainViewpager.setAdapter(myViewPagerAdapter);
        mainTabLayout.setupWithViewPager(mainViewpager);

        mainViewpager.setCurrentItem(position);
        mainTabLayout.getTabAt(position).select();
    }

    @Override
    public void initEvent() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }



}
