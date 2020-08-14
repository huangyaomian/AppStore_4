package com.hym.appstore.presenter.contract;

import com.hym.appstore.bean.BaseBean;
import com.hym.appstore.bean.SortBean;
import com.hym.appstore.ui.BaseView;

import java.util.List;

import io.reactivex.Observable;

public interface SortContract {

    interface SortView extends BaseView {

        void showResult(List<SortBean> datas);
//        void showError(String msg);

    }

    interface ISortModel{
        Observable<BaseBean<List<SortBean>>> getSort();
    }


}
