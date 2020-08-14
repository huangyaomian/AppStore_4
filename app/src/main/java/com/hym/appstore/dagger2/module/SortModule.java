package com.hym.appstore.dagger2.module;

import com.hym.appstore.data.LoginModel;
import com.hym.appstore.data.SortModel;
import com.hym.appstore.data.okhttp.ApiService;
import com.hym.appstore.presenter.contract.LoginContract;
import com.hym.appstore.presenter.contract.SortContract;

import dagger.Module;
import dagger.Provides;

@Module
public class SortModule {

    private SortContract.SortView mView;

    public SortModule(SortContract.SortView mView) {
        this.mView = mView;
    }


    @Provides
    public SortContract.SortView provideView(){
        return mView;
    }


    @Provides
    public SortContract.ISortModel provideSortModel(ApiService apiService){
        return new SortModel(apiService);
    }


}
