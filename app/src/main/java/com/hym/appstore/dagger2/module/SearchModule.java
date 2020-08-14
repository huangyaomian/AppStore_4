package com.hym.appstore.dagger2.module;

import com.hym.appstore.data.SearchModel;
import com.hym.appstore.data.okhttp.ApiService;
import com.hym.appstore.presenter.contract.SearchContract;

import dagger.Module;
import dagger.Provides;

@Module
public class SearchModule {

    private SearchContract.SearchView mView;


    public SearchModule(SearchContract.SearchView view){

        this.mView = view;
    }



//    @FragmentScope
    @Provides
    public SearchContract.ISearchModel provideModel(ApiService apiService){

        return  new SearchModel(apiService);
    }

//    @FragmentScope
    @Provides
    public SearchContract.SearchView provideView(){

        return  mView;
    }





}
