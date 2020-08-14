package com.hym.appstore.common.rx;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonParseException;
import com.hym.appstore.common.exception.ApiException;
import com.hym.appstore.common.exception.BaseException;
import com.hym.appstore.common.exception.ErrorMessageFactory;

import java.net.SocketException;
import java.net.SocketTimeoutException;

import retrofit2.HttpException;

public class RxErrorHandler {
    private Context mContext;

    public RxErrorHandler(Context mContext) {
        this.mContext = mContext;
    }

    public BaseException handleError(Throwable t){
        BaseException exception = new BaseException();
        if (t instanceof ApiException){
            exception.setCode(((ApiException)t).getCode());
        }else if (t instanceof JsonParseException){
            exception.setCode(BaseException.JSON_ERROR);
        }else if (t instanceof HttpException){
            exception.setCode(((HttpException)t).code());
        }else if (t instanceof SocketTimeoutException){
            exception.setCode(BaseException.SOCKET_TIME_ERROR);
        }else if (t instanceof SocketException){
            exception.setCode(BaseException.SOCKET_ERROR);
        }else {
            exception.setCode(BaseException.UNKNOWN_ERROR);
        }

        exception.setDisplayMessage(ErrorMessageFactory.create(mContext,exception.getCode()));

        return exception;
    }

    public void showErrorMsg(BaseException e){
        Log.d("hymmm","縂的錯誤捕捉");
        Toast.makeText(mContext,e.getDisplayMessage(),Toast.LENGTH_SHORT).show();
    }
}
