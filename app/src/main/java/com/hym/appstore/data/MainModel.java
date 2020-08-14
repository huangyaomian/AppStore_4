package com.hym.appstore.data;

import com.hym.appstore.bean.AppInfoBean;
import com.hym.appstore.bean.BaseBean;
import com.hym.appstore.bean.LoginBean;
import com.hym.appstore.bean.requestbean.AppsUpdateBean;
import com.hym.appstore.bean.requestbean.LoginRequestBean;
import com.hym.appstore.data.okhttp.ApiService;
import com.hym.appstore.presenter.contract.LoginContract;
import com.hym.appstore.presenter.contract.MainContract;

import java.util.List;

import io.reactivex.Observable;


public class MainModel implements MainContract.IMainModel {


    private ApiService mApiService;

    public MainModel(ApiService apiService){
        this.mApiService = apiService;
    }


    @Override
    public Observable<BaseBean<List<AppInfoBean>>> getUpdateApps(AppsUpdateBean param) {
        return mApiService.getAppsUpdateinfo(param.getPackageName(),param.getVersionCode());
    }
}
