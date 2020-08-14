package com.hym.appstore.presenter;

import com.hym.appstore.bean.AppInfoBean;
import com.hym.appstore.bean.PageBean;
import com.hym.appstore.bean.Subject;
import com.hym.appstore.bean.SubjectDetail;
import com.hym.appstore.common.rx.RxHttpResponseCompat;
import com.hym.appstore.common.rx.subscriber.ErrorHandlerDisposableObserver;
import com.hym.appstore.common.rx.subscriber.ProgressDisposableObserver;
import com.hym.appstore.data.AppInfoModel;
import com.hym.appstore.data.SubjectModel;
import com.hym.appstore.presenter.contract.AppInfoContract;
import com.hym.appstore.presenter.contract.SubjectContract;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;


public class SubjectPresenter extends BasePresenter<SubjectContract.ISubjectModel, SubjectContract.SubjectView> {


    @Inject
    public SubjectPresenter(SubjectContract.SubjectView mView, SubjectContract.ISubjectModel model) {
        super(model, mView);
    }


    public void getSubject(int page){

        Observer subscriber = null;
        if (page == 0) {
            subscriber= new ProgressDisposableObserver<PageBean<Subject>>(mContext, mView) {
                @Override
                public void onNext(PageBean<Subject> pageBean) {
                    mView.showSubject(pageBean);
                }
            };

        }else {
            subscriber = new ErrorHandlerDisposableObserver<PageBean<Subject>>(mContext) {

                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(PageBean<Subject> pageBean) {
                    mView.showSubject(pageBean);
                }

                @Override
                public void onComplete() {
                    mView.onLoadMoreComplete();
                }
            };
        }

        mModel.getSubjects(page)
                .compose(RxHttpResponseCompat.<PageBean<Subject>>compatResult())
                .subscribe(subscriber);


    }

    public void getSubjectDetail(int id){


        mModel.getSubjectDetail(id).compose(RxHttpResponseCompat.<SubjectDetail>compatResult())
                .subscribe(new ProgressDisposableObserver<SubjectDetail>(mContext,mView) {
                    @Override
                    public void onNext(SubjectDetail subjectDetail) {
                        mView.showSubjectDetail(subjectDetail);
                    }
                });


    }
}
