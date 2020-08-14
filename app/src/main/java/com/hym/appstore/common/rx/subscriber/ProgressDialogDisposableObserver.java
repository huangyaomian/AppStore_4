package com.hym.appstore.common.rx.subscriber;

import android.content.Context;

import com.hym.appstore.common.utils.ProgressDialogHandler;

import io.reactivex.disposables.Disposable;


public abstract class ProgressDialogDisposableObserver<T> extends ErrorHandlerDisposableObserver<T> implements ProgressDialogHandler.OnProgressCancelListener {


    private ProgressDialogHandler mProgressDialogHandler;
    private Disposable mDisposable;

    public ProgressDialogDisposableObserver(Context context) {
        super(context);
        mProgressDialogHandler = new ProgressDialogHandler(mContext,true,this);
    }

    protected boolean isShowProgressDialog(){
        return true;
    }

    @Override
    public void onCancelProgress() {
        mDisposable.dispose();//這個是取消訂閲的但是暫時不知道怎麽弄
    }

//    protected abstract void unsubscribe();

    @Override
    public void onSubscribe(Disposable d) {
        mDisposable = d;
        if (isShowProgressDialog()) {
            this.mProgressDialogHandler.showProgressDialog();
        }

    }

    @Override
    public void onComplete() {
        if (isShowProgressDialog()) {
            this.mProgressDialogHandler.dismissProgressDialog();
        }

    }

    @Override
    public void onError(Throwable t) {
        super.onError(t);
        if (isShowProgressDialog()) {
            this.mProgressDialogHandler.dismissProgressDialog();
        }
    }





}
