package com.hym.appstore.dagger2.module;

import android.app.Application;
import android.content.Context;

import com.hym.appstore.data.AppManagerModel;
import com.hym.appstore.presenter.contract.AppManagerContract;

import dagger.Module;
import dagger.Provides;
import zlc.season.rxdownload2.RxDownload;

@Module
public class AppManagerModule {

    private AppManagerContract.AppManagerView mView;

    public AppManagerModule(AppManagerContract.AppManagerView mView) {
        this.mView = mView;
    }


    @Provides
    public AppManagerContract.AppManagerView provideView(){
        return mView;
    }

    @Provides
    public AppManagerContract.IAppManagerModel provideModel(Application application, RxDownload rxDownload){
        return new AppManagerModel(application,rxDownload);
    }





}
