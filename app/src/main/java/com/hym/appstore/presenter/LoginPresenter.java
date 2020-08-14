package com.hym.appstore.presenter;

import com.hym.appstore.bean.LoginBean;
import com.hym.appstore.common.Constant;
import com.hym.appstore.common.rx.RxBus;
import com.hym.appstore.common.rx.RxHttpResponseCompat;
import com.hym.appstore.common.rx.subscriber.ErrorHandlerDisposableObserver;
import com.hym.appstore.common.utils.ACache;
import com.hym.appstore.common.utils.VerificationUtils;
import com.hym.appstore.presenter.contract.LoginContract;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

public class  LoginPresenter extends BasePresenter<LoginContract.ILoginModel, LoginContract.LoginView> {


    @Inject
    public LoginPresenter(LoginContract.ILoginModel mModel, LoginContract.LoginView mView) {
        super(mModel, mView);
    }


    public void login(String phone,String pwd){
        if (!VerificationUtils.matcherPhoneNum(phone)){
            mView.checkPhoneError();
            return;
        }else {
            mView.checkPhoneSuccess();
        }

        mModel.login(phone,pwd).compose(RxHttpResponseCompat.compatResult())
                .subscribe(new ErrorHandlerDisposableObserver<LoginBean>(mContext) {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mView.showLoading();
                    }

                    @Override
                    public void onNext(@NonNull LoginBean loginBean) {
                        mView.loginSuccess(loginBean);
                        saveUser(loginBean);
//                        RxBus.get().post(loginBean.getUser());
                        RxBus.getDefault().post(loginBean.getUser()); //發送數據
                    }

                    @Override
                    public void onComplete() {
                        mView.dismissLoading();
                    }
                });
    }

    private void saveUser(LoginBean bean){
        ACache aCache = ACache.get(mContext);
        aCache.put(Constant.TOKEN,bean.getToken());
        aCache.put(Constant.USER,bean.getUser());
    }
}
