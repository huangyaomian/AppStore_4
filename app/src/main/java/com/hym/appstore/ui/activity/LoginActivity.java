package com.hym.appstore.ui.activity;


import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.hym.appstore.R;
import com.hym.appstore.bean.LoginBean;
import com.hym.appstore.dagger2.component.AppComponent;
import com.hym.appstore.dagger2.component.DaggerLoginComponent;
import com.hym.appstore.dagger2.module.LoginModule;
import com.hym.appstore.presenter.LoginPresenter;
import com.hym.appstore.presenter.contract.LoginContract;
import com.hym.appstore.ui.widget.LoadingButton;
import com.jakewharton.rxbinding2.InitialValueObservable;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.ionicons_typeface_library.Ionicons;

import butterknife.BindView;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;

public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.LoginView {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @BindView(R.id.txt_phone_edit)
    EditText txtPhoneEdit;
    @BindView(R.id.view_phone_wrapper)
    TextInputLayout viewPhoneWrapper;
    @BindView(R.id.txt_password_edit)
    EditText txtPasswordEdit;
    @BindView(R.id.view_password_wrapper)
    TextInputLayout viewPasswordWrapper;
    @BindView(R.id.btn_login)
    LoadingButton mBtnLogin;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    @BindView(R.id.txt_register)
    TextView txtRegister;

    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_login;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerLoginComponent.builder().appComponent(appComponent).loginModule(new LoginModule(this)).build().inject(this);

    }

    @Override
    public void init() {

    }

    @Override
    public void initView() {

        toolbar.setNavigationIcon(
                new IconicsDrawable(this)
                        .icon(Ionicons.Icon.ion_ios_arrow_back)
                        .sizeDp(16)
                        .color(getResources().getColor(R.color.theme_black)
                        )
        );
        InitialValueObservable<CharSequence> obPhone = RxTextView.textChanges(txtPhoneEdit);
        InitialValueObservable<CharSequence> obPassword = RxTextView.textChanges(txtPasswordEdit);

        InitialValueObservable.combineLatest(obPhone, obPassword, new BiFunction<CharSequence, CharSequence, Boolean>() {
            @Override
            public Boolean apply(CharSequence charSequence, CharSequence charSequence2) {
                return isPhoneValid(txtPhoneEdit.getText().toString().trim()) && isPsdValid(txtPasswordEdit.getText().toString().trim());
            }
        }).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) {
                mBtnLogin.setEnabled(aBoolean);
            }
        });

        RxView.clicks(mBtnLogin).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                mPresenter.login(txtPhoneEdit.getText().toString().trim(),txtPasswordEdit.getText().toString().trim());
            }
        });

        /*RxView.clicks(mBtnLogin).subscribe(new Consumer<Unit>() {
            @Override
            public void accept(Unit unit) {
                mPresenter.login(txtPhoneEdit.getText().toString().trim(),txtPasswordEdit.getText().toString().trim());
            }
        });*/
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        txtPhoneEdit.setText("13020169902");
        txtPasswordEdit.setText("cq13020169902");
    }

    @Override
    public void initEvent() {

    }

    private boolean isPhoneValid(String phone){
        return phone.length() == 11;// && phone.startsWith("1");
    }

    private boolean isPsdValid(String password){
        return password.length() >= 6;
    }

    @Override
    public void checkPhoneError() {
        viewPhoneWrapper.setError("手机号码输入错误");
    }

    @Override
    public void checkPhoneSuccess() {
//        viewPhoneWrapper.setError("手机号码输入错误");
    }

    @Override
    public void loginSuccess(LoginBean bean) {
        finish();
        Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading() {
        mBtnLogin.showLoading();

    }

    @Override
    public void showError(String msg,int errorCode) {
        mBtnLogin.showButtonText();
    }

    @Override
    public void dismissLoading() {
        mBtnLogin.showButtonText();
    }
}
