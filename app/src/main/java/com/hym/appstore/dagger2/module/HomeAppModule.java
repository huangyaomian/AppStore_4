package com.hym.appstore.dagger2.module;

import com.hym.appstore.data.HomeAppModel;
import com.hym.appstore.data.okhttp.ApiService;
import com.hym.appstore.presenter.contract.HomeAppContract;

import dagger.Module;
import dagger.Provides;

@Module
public class HomeAppModule {

    private HomeAppContract.HomeAppView mView;


    public HomeAppModule(HomeAppContract.HomeAppView view){
        this.mView = view;
    }



//    @FragmentScope
    @Provides
    public HomeAppContract.HomeAppView provideView(){
        return  mView;
    }


//    @FragmentScope
    @Provides
    public HomeAppContract.IHomeAppModel provideModel(ApiService apiService){
        return  new HomeAppModel(apiService);
    }





}
