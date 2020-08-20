package com.hym.appstore.ui.fragment;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hym.appstore.R;
import com.hym.appstore.app.MyApplication;
import com.hym.appstore.common.exception.BaseException;
import com.hym.appstore.dagger2.component.AppComponent;
import com.hym.appstore.presenter.BasePresenter;
import com.hym.appstore.service.receiver.MyInstallListener;
import com.hym.appstore.service.receiver.MyInstallReceiver;
import com.hym.appstore.ui.BaseView;
import com.hym.appstore.ui.activity.LoginActivity;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public abstract class ProgressFragment<T extends BasePresenter> extends Fragment implements BaseView, MyInstallListener {

    private FrameLayout mRootView;
    private View mViewProgress;
    private View mViewEmpty;
    private FrameLayout mViewContent;
    private TextView mTextError;
    private Button mLoginButton;

    protected MyApplication mMyApplication;

    @Inject
    public T mPresenter;

    private Unbinder mUnbinder;

    private MyInstallReceiver mMyInstallReceiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = (FrameLayout) inflater.inflate(R.layout.fragment_progress,container,false);
        mViewProgress = mRootView.findViewById(R.id.view_progress);
        mViewEmpty = mRootView.findViewById(R.id.view_empty);
        mViewContent = mRootView.findViewById(R.id.view_content);
        mTextError = mRootView.findViewById(R.id.text_tip);
        mLoginButton = mRootView.findViewById(R.id.login_btn);
        mTextError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEmptyViewClick();
            }
        });

        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        registerMyInstallReceiver();
        mMyApplication = (MyApplication) getActivity().getApplication();
        setupActivityComponent(mMyApplication.getAppComponent());
        setRealContentView();
        init();
        initView();
        initEvent();
    }

    //子类实现此方法使其点击重新刷新页面
    public void onEmptyViewClick(){

    }

    protected  void setRealContentView(){
        View realContentView = LayoutInflater.from(getActivity()).inflate(setLayoutResourceID(), mViewContent, true);
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
                    startActivity(new Intent(getActivity(),LoginActivity.class));
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


    /**
     * 此方法用于返回Fragment设置ContentView的布局文件资源ID * * @return 布局文件资源ID
     */
    protected abstract int setLayoutResourceID();

    protected abstract void setupActivityComponent(AppComponent appComponent);

    /**
     * 做一些初始化的操作
     */
    protected abstract void init();

    /**
     * 一些View的相关操作
     */
    protected abstract void initView();

    /**
     * 一些事件相關的監聽
     */
    protected abstract void initEvent();




    @Override
    public void onDestroyView() {
        if(mMyInstallReceiver != null) {
            getContext().unregisterReceiver(mMyInstallReceiver);
        }
        super.onDestroyView();
        if (mUnbinder != Unbinder.EMPTY) {
            mUnbinder.unbind();
        }
    }

    private void registerMyInstallReceiver(){
        mMyInstallReceiver = new MyInstallReceiver();
        mMyInstallReceiver.registerListener(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.PACKAGE_ADDED");
        filter.addAction("android.intent.action.PACKAGE_REMOVED");
        filter.addAction("android.intent.action.PACKAGE_REPLACED");
        filter.addDataScheme("package");
        getContext().registerReceiver(mMyInstallReceiver, filter);
    }

    @Override
    public void PackageAdded(String packageName) {
        Log.d("hymmm", "ProgressFragment: " + "安装了应用："+packageName);
    }

    @Override
    public void PackageRemoved(String packageName) {
        Log.d("hymmm", "ProgressFragment: " + "卸载了应用："+packageName);
    }

    @Override
    public void PackageReplaced(String packageName) {
        Log.d("hymmm", "ProgressFragment: " + "覆盖安装了应用："+packageName);
    }
}
