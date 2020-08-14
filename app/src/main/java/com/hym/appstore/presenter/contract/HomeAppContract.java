package com.hym.appstore.presenter.contract;

import com.hym.appstore.bean.BaseBean;
import com.hym.appstore.bean.HomeBean;
import com.hym.appstore.ui.BaseView;

import io.reactivex.Observable;

public interface HomeAppContract {

    interface HomeAppView  extends BaseView {
        void showApps(HomeBean datas);

    }

    interface IHomeAppModel{
        /*       public Observable<BaseBean<HomeBean>> getHomeRequest(){
                   return  mApiService.getHome();
               }*/
        Observable<BaseBean<HomeBean>> getHomeApps();

    }


}
