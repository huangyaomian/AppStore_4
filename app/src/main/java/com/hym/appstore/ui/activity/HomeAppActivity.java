package com.hym.appstore.ui.activity;

import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hym.appstore.R;
import com.hym.appstore.bean.HomeBean;
import com.hym.appstore.common.Constant;
import com.hym.appstore.dagger2.component.AppComponent;
import com.hym.appstore.dagger2.component.DaggerHomeAppComponent;
import com.hym.appstore.dagger2.module.HomeAppModule;
import com.hym.appstore.presenter.HomeAppPresenter;
import com.hym.appstore.presenter.contract.HomeAppContract;
import com.hym.appstore.ui.adapter.AppInfoAdapter;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.ionicons_typeface_library.Ionicons;

import javax.inject.Inject;

import butterknife.BindView;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;
import zlc.season.rxdownload2.RxDownload;

public class HomeAppActivity extends ProgressActivity<HomeAppPresenter> implements HomeAppContract.HomeAppView {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;


    private AppInfoAdapter mAdapter;

    @Inject
    RxDownload mRxDownload;


    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_app_game_home;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

        DaggerHomeAppComponent.builder().appComponent(appComponent).homeAppModule(new HomeAppModule(this)).build().inject(this);

    }

    @Override
    public void init() {
        mPresenter.requestHomeApps(true);

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
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

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


    @Override
    public void showApps(HomeBean datas) {

        mAdapter = AppInfoAdapter.builder().updateStatus(true).showBrief(true).rxDownload(mRxDownload).build();

        mAdapter.setEmptyView(R.layout.search_empty_view);
        int intExtra = getIntent().getIntExtra(Constant.HOME_LIST, 1);
        if (intExtra == Constant.APP_HOME_LIST) {
            mAdapter.addData(datas.getHomeApps());
            toolbar.setTitle("热门应用");
        }else {
            mAdapter.addData(datas.getHomeGames());
            toolbar.setTitle("热门游戏");
        }
        SlideInBottomAnimationAdapter alphaAdapter = new SlideInBottomAnimationAdapter(mAdapter);
        mRecyclerView.setAdapter(new ScaleInAnimationAdapter(alphaAdapter));
//        mRecyclerView.setAdapter(mAdapter);

    }
}
