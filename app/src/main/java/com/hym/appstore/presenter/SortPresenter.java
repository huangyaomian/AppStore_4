package com.hym.appstore.presenter;

import com.hym.appstore.bean.SortBean;
import com.hym.appstore.common.rx.RxHttpResponseCompat;
import com.hym.appstore.common.rx.subscriber.ProgressDisposableObserver;
import com.hym.appstore.presenter.contract.SortContract;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;


public class SortPresenter extends BasePresenter<SortContract.ISortModel, SortContract.SortView> {


    @Inject
    public SortPresenter(SortContract.ISortModel mModel, SortContract.SortView mView) {
        super(mModel, mView);
    }



    public void getAllSort(){
        mModel.getSort().compose(RxHttpResponseCompat.compatResult())
                .subscribe(new ProgressDisposableObserver<List<SortBean>>(mContext,mView) {
                    @Override
                    public void onNext(@NonNull List<SortBean> sortBeans) {
                        mView.showResult(sortBeans);
                    }
                });
    }


}
