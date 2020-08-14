package com.hym.appstore.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.hym.appstore.app.MyApplication;
import com.hym.appstore.dagger2.component.AppComponent;
import com.hym.appstore.presenter.BasePresenter;
import com.hym.appstore.ui.BaseView;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;

//public abstract class BaseFragment extends Fragment implements ContantsPool, HttpListener<String> {
public abstract class BaseFragment<T extends BasePresenter> extends Fragment implements BaseView {
    private View mContentView;
    private Context mContext;
    private Unbinder mUnbinder;

    private MyApplication mMyApplication;

    @Inject
    public T mPresenter;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(setLayoutResourceID(), container, false);
        mUnbinder = ButterKnife.bind(this, mContentView);

        initView();
        initEvent();
        return mContentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMyApplication = (MyApplication) getActivity().getApplication();
        setupActivityComponent(mMyApplication.getAppComponent());
        mContext = getContext();
        init();

    }

    protected abstract void setupActivityComponent(AppComponent appComponent);

    /**
     * 此方法用于返回Fragment设置ContentView的布局文件资源ID * * @return 布局文件资源ID
     */
    protected abstract int setLayoutResourceID();

    /**
     * 一些View的相关操作
     */
    protected abstract void initView();


    /**
     * 做一些初始化的操作
     */
    protected abstract void init();

    /**
     * 一些事件相關的監聽
     */
    protected abstract void initEvent();

    /**
     * 此方法用于初始化成员变量及获取Intent传递过来的数据 * 注意：这个方法中不能调用所有的View，因为View还没有被初始化，要使用View在initView方法中调用
     */
    protected void initIntent() {
    }

    public View getContentView() {
        return mContentView;
    }

    public Context getMContext() {
        return mContext;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnbinder != Unbinder.EMPTY) {
            mUnbinder.unbind();
        }
    }

    @Override
    public void showLoading() {
    }

    @Override
    public void dismissLoading() {
    }

    @Override
    public void showError(String msg,int errorCode) {
    }

}
