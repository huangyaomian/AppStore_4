package com.hym.appstore.data;

import com.hym.appstore.bean.AppInfoBean;
import com.hym.appstore.bean.BaseBean;
import com.hym.appstore.data.okhttp.ApiService;

import java.util.List;

import io.reactivex.Observable;


public class RecommendModel {

    private ApiService mApiService;

    public RecommendModel(ApiService mApiService) {
        this.mApiService = mApiService;
    }




    public Observable<BaseBean<List<AppInfoBean>>> getRecommendRequest(){
        return  mApiService.getApps("{'page':0}");
    }
}
