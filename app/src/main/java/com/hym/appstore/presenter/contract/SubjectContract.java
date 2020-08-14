package com.hym.appstore.presenter.contract;

import com.hym.appstore.bean.AppInfoBean;
import com.hym.appstore.bean.BaseBean;
import com.hym.appstore.bean.HomeBean;
import com.hym.appstore.bean.PageBean;
import com.hym.appstore.bean.Subject;
import com.hym.appstore.bean.SubjectDetail;
import com.hym.appstore.common.apkparset.AndroidApk;
import com.hym.appstore.ui.BaseView;

import java.util.List;

import io.reactivex.Observable;
import zlc.season.rxdownload2.RxDownload;
import zlc.season.rxdownload2.entity.DownloadRecord;

public interface SubjectContract {


    interface SubjectView extends BaseView {
        void showSubject(PageBean<Subject> pageBean);
//        void showError(String msg);
        void onLoadMoreComplete();

        void showSubjectDetail(SubjectDetail detail);
    }

    interface ISubjectModel{
        Observable<BaseBean<PageBean<Subject>>> getSubjects(int page);
        Observable<BaseBean<SubjectDetail>> getSubjectDetail(int id);
    }
}
