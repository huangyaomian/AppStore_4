package com.hym.appstore.presenter;

import android.util.Log;

import com.hym.appstore.bean.HomeBean;
import com.hym.appstore.common.Constant;
import com.hym.appstore.common.rx.Optional;
import com.hym.appstore.common.rx.RxHttpResponseCompat;
import com.hym.appstore.common.rx.subscriber.ProgressDisposableObserver;
import com.hym.appstore.common.utils.ACache;
import com.hym.appstore.common.utils.JsonUtils;
import com.hym.appstore.data.AppInfoModel;
import com.hym.appstore.presenter.contract.AppInfoContract;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;


public class HomePresenter extends BasePresenter<AppInfoModel, AppInfoContract.View> {

    @Inject
    public HomePresenter(AppInfoContract.View mView, AppInfoModel model) {
        super(model, mView);
    }




    public void requestHomeData(boolean isShowProgress) {
        mModel.getHomeRequest()
                .compose(RxHttpResponseCompat.handle_result())
                .subscribe(new ProgressDisposableObserver<Optional<HomeBean>>(mContext, mView) {
                    @Override
                    public void onNext(@NonNull Optional<HomeBean> homeBeanOptional) {
                        Log.d("requestHomeData", String.valueOf(homeBeanOptional.getIncludeNull().getBanners().size()));
                        mView.showResult(homeBeanOptional.getIncludeNull());

                    }
                    @Override
                    protected boolean isShowProgress() {
                        return isShowProgress;
                    }
                });

      /*  mModel.getHomeRequest()
               .subscribeOn(Schedulers.io())//把請求放到子綫程中去做(被观察者设置为子线程(发消息))
               .observeOn(AndroidSchedulers.mainThread())//观观察者设置为主线程(接收消息）
                .subscribeWith(new ProgressDisposableObserver<BaseBean<HomeBean>>(mContext, mView) {
                    @Override
                    public void onNext(@NonNull BaseBean baseBean) {
//                        Log.d("requestHomeData", String.valueOf(appInfoBeans);
                        mView.showResult((HomeBean) baseBean.getData());
                    }

                    @Override
                    protected boolean isShowProgress() {
                        return isShowProgress;
                    }
                });*/


    }
}


    /*RxPermissions rxPermissions = new RxPermissions((Fragment) mView);
        rxPermissions.requestSimple(Manifest.permission.READ_PHONE_STATE).flatMap(new Function<Boolean[], ObservableSource<HomeBean>>() {
            @Override
            public ObservableSource<HomeBean> apply(Boolean[] booleans) throws Throwable {
                for (int i = 0; i < booleans.length; i++) {
                    if (booleans[i] && i == booleans.length - 1) {
                        return mModel.getHomeRequest().compose(RxHttpResponseCompat.<HomeBean>compatResult());
                    } else {
                        return Observable.empty();
                    }
                }
                return Observable.empty();
            }
        }).subscribe(new ProgressDisposableObserver<HomeBean>(mContext, mView) {
            @Override
            public void onNext(@NonNull HomeBean homeBean) {
                Log.d("requestRecommendData", "onNext");
                if (homeBean != null) {
                    mView.showResult(homeBean);
                } else {
                    mView.showNoData();
                }
            }

            @Override
            protected boolean isShowProgress() {
                return isShowProgress;
            }
        });
    }*/
