package com.hym.appstore.common.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import com.hym.appstore.R;
import com.xuexiang.xui.widget.dialog.LoadingDialog;
import com.xuexiang.xui.widget.dialog.MiniLoadingDialog;
import com.xuexiang.xui.widget.progress.loading.LoadingCancelListener;



public class ProgressDialogHandler extends Handler {

    public static final int SHOW_PROGRESS_DIALOG = 1;
    public static final int DISMISS_PROGRESS_DIALOG = 0;

    private MiniLoadingDialog mProgressDialog;

    private Context mContext;
    private boolean cancelable;
    private OnProgressCancelListener mOnProgressCancelListener;

    public ProgressDialogHandler(Context context){
        this.mContext = context;
    }

    public ProgressDialogHandler(Context context,boolean cancelable, OnProgressCancelListener progressCancelListener){
        super();
        this.mContext = context;
        this.mOnProgressCancelListener = progressCancelListener;
        this.cancelable = cancelable;
        initProgressDialog();
    }

    private void initProgressDialog() {
        if (mProgressDialog == null){
            mProgressDialog = new MiniLoadingDialog(mContext,"loading...");
            if (cancelable){
                mProgressDialog.setLoadingCancelListener(new LoadingCancelListener() {
                    @Override
                    public void onCancelLoading() {
                        mProgressDialog.dismiss();
                        if (mOnProgressCancelListener != null){
                            mOnProgressCancelListener.onCancelProgress();
                        }
                    }
                });
            }
        }
    }


    public void showProgressDialog(){
        if (mProgressDialog != null && !mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    public void dismissProgressDialog(){
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        switch (msg.what){
            case SHOW_PROGRESS_DIALOG:
                showProgressDialog();
                break;
            case DISMISS_PROGRESS_DIALOG:
                dismissProgressDialog();
                break;
        }
    }

    public interface OnProgressCancelListener{
        void onCancelProgress();
    }
}
