package com.hym.appstore.data;


import com.hym.appstore.bean.BaseBean;
import com.hym.appstore.bean.SortBean;
import com.hym.appstore.data.okhttp.ApiService;
import com.hym.appstore.presenter.contract.SortContract;

import java.util.List;

import io.reactivex.Observable;


public class SortModel implements SortContract.ISortModel {
    private ApiService mApiService;

    public SortModel(ApiService mApiService) {
        this.mApiService = mApiService;
    }


    @Override
    public Observable<BaseBean<List<SortBean>>> getSort() {
        return mApiService.getCategories();
    }
}
