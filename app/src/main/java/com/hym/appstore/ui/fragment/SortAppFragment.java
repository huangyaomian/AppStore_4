package com.hym.appstore.ui.fragment;

import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.hym.appstore.dagger2.component.AppComponent;
import com.hym.appstore.dagger2.component.DaggerAppInfoComponent;
import com.hym.appstore.dagger2.module.AppInfoModule;
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
    }


}
