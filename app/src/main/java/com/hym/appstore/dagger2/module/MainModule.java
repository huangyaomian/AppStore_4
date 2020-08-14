package com.hym.appstore.dagger2.module;

import com.hym.appstore.dagger2.module.AppModelModule;
import com.hym.appstore.dagger2.scope.FragmentScope;
import com.hym.appstore.data.MainModel;
import com.hym.appstore.data.okhttp.ApiService;
import com.hym.appstore.presenter.contract.AppInfoContract;
import com.hym.appstore.presenter.contract.MainContract;

import dagger.Module;
import dagger.Provides;

@Module
public class MainModule {

    private MainContract.MainView mView;


    public MainModule(MainContract.MainView view){
        this.mView = view;
    }



//    @FragmentScope
    @Provides
    public MainContract.MainView provideView(){
        return  mView;
    }


//    @FragmentScope
    @Provides
    public MainContract.IMainModel provideModel(ApiService apiService){
        return  new MainModel(apiService);
    }





}
