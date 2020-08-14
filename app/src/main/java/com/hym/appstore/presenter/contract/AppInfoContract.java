package com.hym.appstore.presenter.contract;

import com.hym.appstore.bean.AppInfoBean;
import com.hym.appstore.bean.HomeBean;
import com.hym.appstore.bean.PageBean;
import com.hym.appstore.ui.BaseView;

public interface AppInfoContract {

    interface View extends BaseView {

        void showResult(HomeBean datas);
        void showNoData();
        void showError(String msg,int errorCode);

        void onRequestPermissionSuccess();
        void onRequestPermissionError();

    }

    interface AppInfoView extends BaseView {

        void showResult(PageBean<AppInfoBean> data);
        void showError(String msg,int errorCode);
        void onLoadMoreComplete();
    }

    interface AppDetailView extends BaseView {
        void showAppDetail(AppInfoBean appInfoBean);
    }
}
