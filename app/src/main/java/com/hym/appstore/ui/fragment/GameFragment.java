package com.hym.appstore.ui.fragment;

import com.hym.appstore.dagger2.component.AppComponent;
import com.hym.appstore.dagger2.component.DaggerAppInfoComponent;
import com.hym.appstore.dagger2.module.AppInfoModule;
import com.hym.appstore.presenter.AppInfoPresenter;
import com.hym.appstore.ui.adapter.AppInfoAdapter;


public class GameFragment extends AppInfoFragment {



    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerAppInfoComponent.builder().appComponent(appComponent).appInfoModule(new AppInfoModule(this)).build().injectGameFragment(this);
    }

    @Override
    AppInfoAdapter buildAdapter() {
        return AppInfoAdapter.builder().showPosition(false).showCategoryName(false).showBrief(false).rxDownload(mRxDownload).build();
    }

    @Override
    int setType() {
        return AppInfoPresenter.GAMES_LIST;
    }


}
