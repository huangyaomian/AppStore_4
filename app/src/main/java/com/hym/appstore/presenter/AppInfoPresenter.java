package com.hym.appstore.presenter;

import com.hym.appstore.bean.AppInfoBean;
import com.hym.appstore.bean.BaseBean;
import com.hym.appstore.bean.PageBean;
import com.hym.appstore.common.rx.RxHttpResponseCompat;
import com.hym.appstore.common.rx.subscriber.ErrorHandlerDisposableObserver;
import com.hym.appstore.common.rx.subscriber.ProgressDisposableObserver;
import com.hym.appstore.data.AppInfoModel;
import com.hym.appstore.presenter.contract.AppInfoContract;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;


public class AppInfoPresenter extends BasePresenter<AppInfoModel, AppInfoContract.AppInfoView> {

    public final static int RANKING_LIST = 1;
    public final static int GAMES_LIST = 2;
    public static final int SORT = 3;

    public static final int FEATURED = 0;
    public static final int TOPLIST = 1;
    public static final int NEWLIST = 2;





    @Inject
    public AppInfoPresenter(AppInfoContract.AppInfoView mView, AppInfoModel model) {
        super(model, mView);
    }

    public void requestData(int type, int page) {
        request(type, page, 0, 0);
    }

    public void requestSortApps(int sortId, int page, int flagType) {
        request(SORT, page, sortId, flagType);
    }




    /*public void requestData(int type,int page) {
        DisposableObserver disposableObserver = null;
        if (page == 0) {
            disposableObserver = new ProgressDisposableObserver<Optional<PageBean<AppInfoBean>>>(mContext, mView) {
                @Override
                public void onNext(@NonNull Optional<PageBean<AppInfoBean>> pageBeanOptional) {
                    mView.showResult(pageBeanOptional.getIncludeNull());
                }
            };
        }else {
            disposableObserver = new ErrorHandlerDisposableObserver<Optional<PageBean<AppInfoBean>>>(mContext) {

                @Override
                public void onNext(@NonNull Optional<PageBean<AppInfoBean>> pageBeanOptional) {
                    mView.showResult(pageBeanOptional.getIncludeNull());
                }

                @Override
                public void onComplete() {
                    mView.onLoadMoreComplete();
                }
            };
        }

        Observable observable = getObservable(type,page);

        observable
                .compose(RxHttpResponseCompat.handle_result())
                .subscribe(disposableObserver);

    }*/

    public void request(int type, int page, int sortId, int flagType) {
        Observer disposableObserver;
        if (page == 0) {
            //加载第一页
            disposableObserver = new ProgressDisposableObserver<PageBean<AppInfoBean>>(mContext, mView) {

                @Override
                public void onNext(@NonNull PageBean<AppInfoBean> appInfoBeanPageBean) {
                    mView.showResult(appInfoBeanPageBean);
                }
            };
        } else {
            //加载第下页
            disposableObserver = new ErrorHandlerDisposableObserver<PageBean<AppInfoBean>>(mContext) {

                @Override
                public void onSubscribe(@NonNull Disposable d) {

                }

                @Override
                public void onNext(@NonNull PageBean<AppInfoBean> appInfoBeanPageBean) {
                    mView.showResult(appInfoBeanPageBean);
                }

                @Override
                public void onComplete() {
                    mView.onLoadMoreComplete();
                }
            };
        }
        Observable observable = getObservable(type, page, sortId, flagType);
        observable
                .compose(RxHttpResponseCompat.<PageBean<AppInfoBean>>compatResult())
                .subscribe(disposableObserver);
    }


    private Observable<BaseBean<PageBean<AppInfoBean>>> getObservable(int type, int page, int sortId, int flagType){
        switch (type){
            case RANKING_LIST:
                return mModel.getRankingRequest(page);
            case GAMES_LIST:
                return mModel.getGameRequest(page);
            case SORT:
                if (flagType == FEATURED) {
                    return mModel.getFeaturedAppsBySort(sortId, page);
                } else if (flagType == TOPLIST) {
                    return mModel.getTopListAppsBySort(sortId, page);
                } else if (flagType == NEWLIST) {
                    return mModel.getNewListAppsBySort(sortId, page);
                }
        }
        return Observable.empty();
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
