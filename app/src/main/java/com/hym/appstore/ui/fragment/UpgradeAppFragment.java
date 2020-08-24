package com.hym.appstore.ui.fragment;

import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.hym.appstore.bean.AppInfoBean;
import com.hym.appstore.ui.activity.AppDetailsActivity3;
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
        mAdapter = AppInfoAdapter.builder().updateStatus(true).showBrief(true).setDownloadBtnVisible(true).rxDownload(mPresenter.getRxDownload()).build();
        return mAdapter;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initEvent() {
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                AppInfoBean appInfoBean = (AppInfoBean) adapter.getItem(position);
                Intent intent = new Intent(getActivity(), AppDetailsActivity3.class);
                intent.putExtra("appInfo",appInfoBean);
                startActivity(intent);
            }
        });
    }

    @Override
    public void showUpdateApps(List<AppInfoBean> appInfoBeans) {
        mAdapter.addData(appInfoBeans);
    }

    @Override
    public void PackageRemoved(String packageName) {
        for (int i = 0; i < mAdapter.getData().size(); i++) {
            if (mAdapter.getData().get(i).getPackageName().equals(packageName)){
                mAdapter.removeAt(i);
                break;
            }
        }
    }

    @Override
    public void PackageAdded(String packageName) {
        for (int i = 0; i < mAdapter.getData().size(); i++) {
            if (mAdapter.getData().get(i).getPackageName().equals(packageName)){
                mAdapter.notifyItemChanged(i);
                break;
            }
        }
    }
}
