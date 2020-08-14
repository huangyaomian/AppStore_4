package com.hym.appstore.presenter;

import com.hym.appstore.bean.AppInfoBean;
import com.hym.appstore.common.rx.RxHttpResponseCompat;
import com.hym.appstore.common.rx.subscriber.ProgressDisposableObserver;
import com.hym.appstore.data.RecommendModel;
import com.hym.appstore.presenter.contract.RecommendContract;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;


public class RecommendPresenter extends BasePresenter<RecommendModel,RecommendContract.View> {



    @Inject
    public RecommendPresenter(RecommendContract.View mView, RecommendModel model) {
        super(model,mView);
    }

    /*public void requestPermission(){

        RxPermissions rxPermissions = new RxPermissions((Fragment) mView);

        rxPermissions.requestSimple(Manifest.permission.READ_PHONE_STATE).subscribe(new Consumer<Boolean[]>() {
            @Override
            public void accept(Boolean[] booleans) throws Throwable {
                for (int i = 0; i < booleans.length; i++) {
                    if (booleans[i] && i == booleans.length -1) {
                        mView.onRequestPermissionSuccess();
                    }else {
                        mView.onRequestPermissionError();
                        return;
                    }
                }
            }
        });
    }*/


    public void requestRecommendData(boolean isShowProgress) {

        /*RxPermissions rxPermissions = new RxPermissions((Fragment) mView);
        rxPermissions.requestSimple(Manifest.permission.READ_PHONE_STATE).flatMap(new Function<Boolean[], ObservableSource<List<AppInfoBean>>>() {
            @Override
            public ObservableSource<List<AppInfoBean>> apply(Boolean[] booleans) throws Throwable {
                for (int i = 0; i < booleans.length; i++) {
                    if (booleans[i] && i == booleans.length -1) {
                        return mModel.getRecommendRequest().compose(RxHttpResponseCompat.<List<AppInfoBean>>compatResult());
                    }else {
//                        mView.onRequestPermissionError();
                        return Observable.empty();
                    }
                }
                return Observable.empty();
            }
        }).subscribe(new ProgressDisposableObserver<List<AppInfoBean>>(mContext,mView) {
            @Override
            public void onNext(@NonNull List<AppInfoBean> appiInfoBeanPageBean) {
                Log.d("requestRecommendData","onNext");
                Log.d("requestRecommendData",appiInfoBeanPageBean.toString());
                if (appiInfoBeanPageBean != null){
                    mView.showResult(appiInfoBeanPageBean);
                }else {
                    mView.showNoData();
                }
            }

            @Override
            protected boolean isShowProgress() {
                return isShowProgress;
            }
        });*/


        mModel.getRecommendRequest()
                .compose(RxHttpResponseCompat.compatResult())
                .subscribe(new ProgressDisposableObserver<List<AppInfoBean>>(mContext, mView) {
                    @Override
                    public void onNext(@NonNull List<AppInfoBean> appInfoBeans) {
//                        Log.d("requestHomeData", String.valueOf(appInfoBeans);
                        mView.showResult(appInfoBeans);
                    }

                    @Override
                    protected boolean isShowProgress() {
                        return isShowProgress;
                    }
                });


        /*mModel.getRecommendRequest()
                .compose(RxHttpResponseCompat.compatResult())
                .subscribe(new ProgressDisposableObserver<List<AppInfoBean>>(mContext, mView) {
                    @Override
                    public void onNext(@NonNull List<AppInfoBean> appInfoBeans) {
//                        Log.d("requestHomeData", String.valueOf(appInfoBeans);
                        mView.showResult(appInfoBeans);
                    }

                    @Override
                    protected boolean isShowProgress() {
                        return isShowProgress;
                    }
                });*/
/*
        mModel.getRecommendRequest()
                .compose(RxHttpResponseCompat.<List<AppInfoBean>>compatResult())
//                .subscribe(new ProgressDialogDisposableObserver<List<AppInfoBean>>(mContext) {
                .subscribe(new ProgressDisposableObserver<List<AppInfoBean>>(mContext,mView) {

                    @Override
                    public void onNext(@NonNull List<AppInfoBean> appiInfoBeanPageBean) {

                        Log.d("requestRecommendData","请求成功");
                        if (appiInfoBeanPageBean != null){
                            mView.showResult(appiInfoBeanPageBean);
                        }else {
                            mView.showNoData();
                        }
                    }

                    @Override
                    protected boolean isShowProgress() {
                        return isShowProgress;
                    }
                });*/

    }









}
