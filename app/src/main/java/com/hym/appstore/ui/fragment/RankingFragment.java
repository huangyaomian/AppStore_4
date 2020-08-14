package com.hym.appstore.ui.fragment;

import com.hym.appstore.dagger2.component.AppComponent;
import com.hym.appstore.dagger2.component.DaggerAppInfoComponent;
import com.hym.appstore.dagger2.module.AppInfoModule;
import com.hym.appstore.presenter.AppInfoPresenter;
import com.hym.appstore.ui.adapter.AppInfoAdapter;

import zlc.season.rxdownload2.RxDownload;


public class RankingFragment extends AppInfoFragment {


    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerAppInfoComponent.builder().appComponent(appComponent).appInfoModule(new AppInfoModule(this)).build().injectRankingFragment(this);
    }



    @Override
    AppInfoAdapter buildAdapter() {
        return AppInfoAdapter.builder().showPosition(true).showCategoryName(true).showBrief(false).rxDownload(mRxDownload).build();
    }

    @Override
    int setType() {
        return AppInfoPresenter.RANKING_LIST;
    }


}
