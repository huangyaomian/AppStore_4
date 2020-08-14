package com.hym.appstore.dagger2.module;


import com.hym.appstore.bean.RecommendBean2;
import com.hym.appstore.data.RecommendModel;
import com.hym.appstore.data.okhttp.ApiService;
import com.hym.appstore.presenter.contract.RecommendContract;
import com.hym.appstore.ui.adapter.RecommendRVAdapter;
import com.yanzhenjie.nohttp.rest.RequestQueue;

import javax.security.auth.callback.Callback;

import dagger.Module;
import dagger.Provides;

@Module
public class RecommendModule {

    private RecommendContract.View mView;

    public RecommendModule(RecommendContract.View mView) {
        this.mView = mView;
    }


    @Provides
    public RecommendContract.View provideView(){
        return mView;
    }

 /*   @Provides
    public RecommendModel provideRecommendModel(RequestQueue mRequestQueue){
        return new RecommendModel(mRequestQueue);
    }*/

    @Provides
    public RecommendModel provideRecommendModel(ApiService apiService){
        return new RecommendModel(apiService);
    }

    public RecommendRVAdapter provideAdapter(){
        return  null;
    }


}
