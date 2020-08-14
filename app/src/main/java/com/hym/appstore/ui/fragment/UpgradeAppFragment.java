package com.hym.appstore.ui.fragment;

import androidx.recyclerview.widget.RecyclerView;

import com.hym.appstore.bean.AppInfoBean;
import com.hym.appstore.common.apkparset.AndroidApk;
import com.hym.appstore.dagger2.component.AppComponent;
import com.hym.appstore.dagger2.module.AppManagerModule;
import com.hym.appstore.ui.adapter.AndroidApkAdapter;
import com.hym.appstore.ui.adapter.AppInfoAdapter;

import java.util.List;

import zlc.season.rxdownload2.entity.DownloadRecord;


public class UpgradeAppFragment extends AppManagerFragment {

    private AppInfoAdapter mAdapter;


    @Override
    protected void init() {
        super.init();
        mPresenter.getUpdateApps();
    }

    @Override
    protected RecyclerView.Adapter setupAdapter() {
        mAdapter = AppInfoAdapter.builder().updateStatus(true).showBrief(true).rxDownload(mPresenter.getRxDownload()).build();
        return mAdapter;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initEvent() {

    }

    @Override
    public void showUpdateApps(List<AppInfoBean> appInfoBeans) {
        mAdapter.addData(appInfoBeans);
    }
}
