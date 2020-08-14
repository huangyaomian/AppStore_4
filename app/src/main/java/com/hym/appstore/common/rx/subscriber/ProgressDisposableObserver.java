package com.hym.appstore.common.rx.subscriber;

import android.content.Context;
import android.util.Log;

import com.hym.appstore.common.exception.BaseException;
import com.hym.appstore.common.utils.ProgressDialogHandler;
import com.hym.appstore.ui.BaseView;

import io.reactivex.disposables.Disposable;


public abstract class ProgressDisposableObserver<T> extends ErrorHandlerDisposableObserver<T> implements ProgressDialogHandler.OnProgressCancelListener {


    private BaseView mBaseView;
    private Disposable mDisposable;

    public ProgressDisposableObserver(Context context, BaseView baseView) {
        super(context);
        this.mBaseView = baseView;
    }

    protected boolean isShowProgress(){
        return true;
    }

    @Override
    public void onCancelProgress() {
        mDisposable.dispose();
    }


    @Override
    public void onSubscribe(Disposable d) {
        Log.d("requestRecommendData","onSubscribe");
        mDisposable = d;
        if (isShowProgress()) {
            mBaseView.showLoading();
        }
    }


    @Override
    public void onComplete() {
        Log.d("requestRecommendData","onComplete");
        mBaseView.dismissLoading();
    }

    @Override
    public void onError(Throwable t) {
        t.printStackTrace();
        BaseException baseException = mRxErrorHandler.handleError(t);
//        Log.d("ErrorHandlerDO",t.getMessage());
//        mBaseView.showError(baseException.getDisplayMessage());
        mBaseView.showError(baseException.getDisplayMessage(),baseException.getCode());
    }


}
