package com.hym.appstore.presenter.contract;

import com.hym.appstore.bean.BaseBean;
import com.hym.appstore.bean.LoginBean;
import com.hym.appstore.ui.BaseView;

import io.reactivex.Observable;


public interface LoginContract {



    interface ILoginModel{
        Observable<BaseBean<LoginBean>> login(String phone, String pwd);
    }

    interface LoginView extends BaseView {

        void checkPhoneError();
        void checkPhoneSuccess();

        void loginSuccess(LoginBean bean);

//        void loginError(String msg);  //errorhander里面处理了
    }


}
