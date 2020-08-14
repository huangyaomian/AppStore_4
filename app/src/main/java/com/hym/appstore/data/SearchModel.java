package com.hym.appstore.data;

import com.hym.appstore.bean.BaseBean;
import com.hym.appstore.bean.HomeBean;
import com.hym.appstore.bean.SearchResult;
import com.hym.appstore.data.okhttp.ApiService;
import com.hym.appstore.presenter.contract.HomeAppContract;
import com.hym.appstore.presenter.contract.SearchContract;

import java.util.List;

import io.reactivex.Observable;


public class SearchModel implements SearchContract.ISearchModel {


    private ApiService mApiService;


    public SearchModel(ApiService apiService){

        this.mApiService = apiService;
    }

    public  Observable<BaseBean<List<String>>> getSuggestion(String keyword){


        return  mApiService.searchSuggest(keyword);

    }

    @Override
    public Observable<BaseBean<SearchResult>> search(String keyword) {
        return  mApiService.search(keyword);
    }
}
