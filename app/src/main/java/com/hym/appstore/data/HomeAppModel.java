package com.hym.appstore.data;

import com.hym.appstore.bean.BaseBean;
import com.hym.appstore.bean.HomeBean;
import com.hym.appstore.data.okhttp.ApiService;
import com.hym.appstore.presenter.contract.HomeAppContract;

import io.reactivex.Observable;


public class HomeAppModel implements HomeAppContract.IHomeAppModel {


    private ApiService mApiService;

    public HomeAppModel(ApiService apiService){
        this.mApiService = apiService;
    }



    @Override
    public Observable<BaseBean<HomeBean>> getHomeApps() {
        return  mApiService.getHome();
    }
}
