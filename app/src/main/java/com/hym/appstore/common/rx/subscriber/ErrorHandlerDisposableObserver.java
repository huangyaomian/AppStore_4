package com.hym.appstore.common.rx.subscriber;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.hym.appstore.common.exception.BaseException;
import com.hym.appstore.common.rx.RxErrorHandler;
import com.hym.appstore.ui.activity.LoginActivity;

public abstract class ErrorHandlerDisposableObserver<T> extends DefaultDisposableObserver<T> {


    protected RxErrorHandler mRxErrorHandler = null;
    protected Context mContext;

    public ErrorHandlerDisposableObserver(Context context) {
        this.mContext = context;
        mRxErrorHandler = new RxErrorHandler(mContext);
    }

    @Override
    public void onError(Throwable t) {
        BaseException baseException = mRxErrorHandler.handleError(t);

        if (baseException == null) {
            t.printStackTrace();
            Log.d("ErrorHandlerDO",t.getMessage());
        }else {
            mRxErrorHandler.showErrorMsg(baseException);
            if ( baseException.getCode() == BaseException.ERROR_TOKEN) {
                toLogin();
            }

        }

    }

    private void toLogin() {
        Intent intent = new Intent(mContext, LoginActivity.class);
        mContext.startActivity(intent);
    }

}
