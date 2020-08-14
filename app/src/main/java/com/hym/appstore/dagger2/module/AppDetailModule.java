package com.hym.appstore.dagger2.module;

import com.hym.appstore.presenter.contract.AppInfoContract;

import dagger.Module;
import dagger.Provides;

@Module(includes = {AppModelModule.class})
public class AppDetailModule {

    private AppInfoContract.AppDetailView mView;

    public AppDetailModule(AppInfoContract.AppDetailView mView) {
        this.mView = mView;
    }


    @Provides
    public AppInfoContract.AppDetailView provideView(){
        return mView;
    }





}
