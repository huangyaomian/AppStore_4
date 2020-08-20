package com.hym.appstore.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.LayoutInflaterCompat;

import com.hym.appstore.R;
import com.hym.appstore.app.MyApplication;
import com.hym.appstore.common.exception.BaseException;
import com.hym.appstore.dagger2.component.AppComponent;
import com.hym.appstore.presenter.BasePresenter;
import com.hym.appstore.ui.BaseView;
import com.mikepenz.iconics.context.IconicsLayoutInflater2;
import com.xuexiang.xui.utils.StatusBarUtils;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class ProgressActivity<T extends BasePresenter> extends AppCompatActivity implements BaseView {

    private Unbinder mUnbinder;
    protected MyApplication mMyApplication;

    private FrameLayout mRootView;
    private View mViewProgress;
    private View mViewEmpty;
    private FrameLayout mViewContent;
    private TextView mTextError;
    private Button mLoginButton;


    @Inject
    public T mPresenter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        LayoutInflaterCompat.setFactory2(getLayoutInflater(),new IconicsLayoutInflater2(getDelegate()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_progress);
        StatusBarUtils.initStatusBarStyle(this,false, ActivityCompat.getColor(this, R.color.theme_blue));
        mMyApplication = (MyApplication) getApplication();
        setupActivityComponent(mMyApplication.getAppComponent());
//        mRootView = (FrameLayout) getLayoutInflater().from(this).inflate(R.layout.fragment_progress,null);
        mRootView = findViewById(R.id.root_view);

        mViewProgress = findViewById(R.id.view_progress);
        mViewEmpty = findViewById(R.id.view_empty);
        mViewContent = findViewById(R.id.view_content);
        mTextError = findViewById(R.id.text_tip);
        mLoginButton = findViewById(R.id.login_btn);
        setRealContentView();
        mTextError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEmptyViewClick();
            }
        });



        init();
        initView();
        initEvent();

    }



    private void initDefaultView(int layoutResId) {
        View childView = LayoutInflater.from(this).inflate(setLayoutResourceID(), null);
        mRootView.addView(childView, 0);
    }



    protected abstract int setLayoutResourceID();

    protected abstract void setupActivityComponent(AppComponent appComponent);



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnbinder != Unbinder.EMPTY) {
            mUnbinder.unbind();
        }
    }





    protected void startActivity(Class c) {
        this.startActivity(new Intent(this, c));
    }

    public abstract void init();
    public abstract void initView();
    public abstract void initEvent();


    //子类实现此方法使其点击重新刷新页面
    public void onEmptyViewClick(){

    }


    protected  void setRealContentView(){
        View realContentView = LayoutInflater.from(this).inflate(setLayoutResourceID(), mViewContent, true);
        mUnbinder = ButterKnife.bind(this, realContentView);
    }

    public void showProgressView(){
        Log.d("ProgressFragment","showProgressView");
        showView(R.id.view_progress);
    }

    public void showContentView(){
        Log.d("ProgressFragment","showContentView");
        showView(R.id.view_content);
    }

    public void showEmptyView(){
        showView(R.id.view_empty);
    }

    public void showEmptyView(int resId){
        showView(R.id.view_empty);
        mTextError.setText(resId);
    }

    public void showEmptyView(String msg){
        showView(R.id.view_empty);
        mTextError.setText(msg);
    }

    public void showEmptyView(String msg, int errorCode){
        showEmptyView(msg);
        if (errorCode == BaseException.ERROR_TOKEN || errorCode == BaseException.INVALID_TOKEN) {
            mLoginButton.setVisibility(View.VISIBLE);
            mLoginButton.setText("登录");
            mLoginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplication(),LoginActivity.class));
                }
            });
        }
    }

    public void showView(int viewId){
        for (int i = 0; i < mRootView.getChildCount(); i++) {
            if (mRootView.getChildAt(i).getId() == viewId) {
                mRootView.getChildAt(i).setVisibility(View.VISIBLE);
            }else {
                mRootView.getChildAt(i).setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void showLoading() {
        showProgressView();
    }

    @Override
    public void dismissLoading() {
        showContentView();
    }

    @Override
    public void showError(String msg,int errorCode) {
        Log.d("ProgressFragment","showError");
        showEmptyView(msg,errorCode);
    }
}
