package com.hym.appstore.ui.fragment;

import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.hym.appstore.bean.AppInfoBean;
import com.hym.appstore.dagger2.component.AppComponent;
import com.hym.appstore.dagger2.component.DaggerAppInfoComponent;
import com.hym.appstore.dagger2.module.AppInfoModule;
import com.hym.appstore.ui.activity.AppDetailsActivity3;
import com.hym.appstore.ui.adapter.AppInfoAdapter;


public class SortAppFragment extends AppInfoFragment {

    private int sortId;
    private int mFlagType;

    private SortAppFragment(int sortId, int mFlagType) {
        this.sortId = sortId;
        this.mFlagType = mFlagType;
    }

    public static SortAppFragment newInstance(int sortId, int mFlagType){
        return new SortAppFragment(sortId,mFlagType);
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerAppInfoComponent.builder().appComponent(appComponent).appInfoModule(new AppInfoModule(this)).build().injectSortAppFragment(this);
    }



    @Override
    AppInfoAdapter buildAdapter() {
        return AppInfoAdapter.builder().showPosition(false).showCategoryName(false).showBrief(true).build();
    }

    @Override
    int setType() {
        return 0;
    }

    @Override
    public void init() {
        mPresenter.requestSortApps(sortId,page,mFlagType);
        initRecyclerView();
    }

    @Override
    protected void initEvent() {
        mAppInfoAdapter.getLoadMoreModule().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                mPresenter.requestSortApps(sortId,page,mFlagType);
            }
        });

        // 设置点击事件
        mAppInfoAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                mMyApplication.setView(view);
                AppInfoBean appInfoBean = mAppInfoAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), AppDetailsActivity3.class);
                intent.putExtra("appInfo",appInfoBean);
                startActivity(intent);
            }
        });

    }


}
