package com.hym.appstore.dagger2.module;

import com.hym.appstore.data.AppInfoModel;
import com.hym.appstore.data.okhttp.ApiService;
import com.hym.appstore.presenter.contract.AppInfoContract;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModelModule {


    @Provides
    public AppInfoModel provideModel(ApiService apiService){
        return new AppInfoModel(apiService);
    }


}
