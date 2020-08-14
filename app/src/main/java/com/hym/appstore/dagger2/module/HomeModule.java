package com.hym.appstore.dagger2.module;

import com.hym.appstore.data.AppInfoModel;
import com.hym.appstore.data.okhttp.ApiService;
import com.hym.appstore.presenter.contract.AppInfoContract;

import dagger.Module;
import dagger.Provides;

@Module
public class HomeModule {
    private AppInfoContract.View mView;

    public HomeModule(AppInfoContract.View mView) {
        this.mView = mView;
    }


    @Provides
    public AppInfoContract.View provideView(){
        return mView;
    }


    @Provides
    public AppInfoModel provideHomeModel(ApiService apiService){
        return new AppInfoModel(apiService);
    }
}
