package com.hym.appstore.data;

import com.hym.appstore.bean.BaseBean;
import com.hym.appstore.bean.LoginBean;
import com.hym.appstore.bean.requestbean.LoginRequestBean;
import com.hym.appstore.data.okhttp.ApiService;
import com.hym.appstore.presenter.contract.LoginContract;

import io.reactivex.Observable;


public class LoginModel implements LoginContract.ILoginModel {


    private ApiService mApiService;

    public LoginModel(ApiService mApiService) {
        this.mApiService = mApiService;
    }

    @Override
    public Observable<BaseBean<LoginBean>> login(String phone, String pwd) {
        LoginRequestBean param = new LoginRequestBean();
        param.setEmail(phone);
        param.setPassword(pwd);
        return mApiService.login(param);
    }
}
