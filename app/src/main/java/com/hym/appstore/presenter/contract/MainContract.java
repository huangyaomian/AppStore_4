package com.hym.appstore.presenter.contract;

import com.hym.appstore.bean.AppInfoBean;
import com.hym.appstore.bean.BaseBean;
import com.hym.appstore.bean.HomeBean;
import com.hym.appstore.bean.PageBean;
import com.hym.appstore.bean.requestbean.AppsUpdateBean;
import com.hym.appstore.ui.BaseView;

import java.util.List;

import io.reactivex.Observable;

public interface MainContract {

    interface MainView  extends BaseView {

        void requestPermissionSuccess();
        void requestPermissionFail();

        void changeAppNeedUpdateCount(int count);


    }

    interface IMainModel{

        Observable<BaseBean<List<AppInfoBean>>> getUpdateApps(AppsUpdateBean param);

    }


}
