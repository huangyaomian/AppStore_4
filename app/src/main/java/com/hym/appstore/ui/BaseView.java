package com.hym.appstore.ui;

public interface BaseView {
    void showLoading();
//    void showError(String msg);
    void showError(String msg,int errorCode);
    void dismissLoading();
}
