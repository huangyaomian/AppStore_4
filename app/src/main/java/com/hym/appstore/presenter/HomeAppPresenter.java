package com.hym.appstore.presenter;

import android.util.Log;

import com.hym.appstore.bean.HomeBean;
import com.hym.appstore.common.rx.Optional;
import com.hym.appstore.common.rx.RxHttpResponseCompat;
import com.hym.appstore.common.rx.subscriber.ProgressDisposableObserver;
import com.hym.appstore.presenter.contract.HomeAppContract;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;


public class HomeAppPresenter extends BasePresenter<HomeAppContract.IHomeAppModel, HomeAppContract.HomeAppView> {


    @Inject
    public HomeAppPresenter(HomeAppContract.IHomeAppModel model, HomeAppContract.HomeAppView mView) {
        super(model, mView);
    }



    public void requestHomeApps(boolean isShowProgress) {
        mModel.getHomeApps()
                .compose(RxHttpResponseCompat.handle_result())
                .subscribe(new ProgressDisposableObserver<Optional<HomeBean>>(mContext, mView) {
                    @Override
                    public void onNext(@NonNull Optional<HomeBean> homeBeanOptional) {
                        Log.d("requestHomeData", String.valueOf(homeBeanOptional.getIncludeNull().getBanners().size()));
                        mView.showApps(homeBeanOptional.getIncludeNull());
                    }

                    @Override
                    protected boolean isShowProgress() {
                        return isShowProgress;
                    }
                });
    }
}
