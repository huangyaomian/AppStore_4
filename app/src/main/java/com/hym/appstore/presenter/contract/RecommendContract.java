package com.hym.appstore.presenter.contract;

import com.hym.appstore.bean.AppInfoBean;
import com.hym.appstore.bean.RecommendBean2;
import com.hym.appstore.ui.BaseView;

import java.util.List;

public interface RecommendContract {

    interface View extends BaseView{

        void showResult(List<AppInfoBean> datas);
        void showMoreResult(RecommendBean2 datas);
        void showNoData();
//        void showError(String msg);

        void onRequestPermissionSuccess();
        void onRequestPermissionError();

    }

/*    interface  Presenter extends BasePresenter{

        public void requestRecommendData(String URL);

    }*/
}
