package com.hym.appstore.ui.fragment;

import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnItemLongClickListener;
import com.hym.appstore.R;
import com.hym.appstore.bean.AppInfoBean;
import com.hym.appstore.common.apkparset.AndroidApk;
import com.hym.appstore.common.rx.RxSchedulers;
import com.hym.appstore.common.utils.FileUtils;
import com.hym.appstore.ui.activity.AppDetailsActivity3;
import com.hym.appstore.ui.adapter.DownloadingAdapter;

import java.io.File;
import java.util.List;

import zlc.season.rxdownload2.entity.DownloadRecord;


public class DownloadingFragment extends AppManagerFragment {

    private DownloadingAdapter mAdapter;

    private PopupMenu mPopupMenu;


    @Override
    protected void init() {
        super.init();
        mPresenter.getDownloadingApps();
    }

    @Override
    protected RecyclerView.Adapter setupAdapter() {

        mAdapter = new DownloadingAdapter(mPresenter.getRxDownload());
        mAdapter.setAnimationEnable(true);
        mAdapter.setAnimationWithDefault(BaseQuickAdapter.AnimationType.AlphaIn);
        mAdapter.setEmptyView(R.layout.loading_view);
        return mAdapter;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initEvent() {
        mAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                mPopupMenu = myPopupMenu(view);
                //设置PopupMenu的点击事件
                mPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
//                        Log.d("hymmm", "onMenuItemClick: " + mAdapter.getItem(position).getUrl());
                        mPresenter.DelDownloadingApp(mAdapter.getItem(position).getUrl(),true)
                                .compose(RxSchedulers.io_main()).subscribe();
                        mAdapter.removeAt(position);
                        return true;
                    }

                });

                return true;
            }
        });

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                AppInfoBean appInfoBean = new AppInfoBean();
                DownloadRecord item = (DownloadRecord) adapter.getItem(position);
                appInfoBean.setId(Integer.parseInt(item.getExtra1()));
                appInfoBean.setIcon(item.getExtra1());
                Intent intent = new Intent(getActivity(), AppDetailsActivity3.class);
                intent.putExtra("appInfo",appInfoBean);
                startActivity(intent);
            }
        });

    }

    @Override
    public void showDownloading(List<DownloadRecord> downloadRecords) {
        if (downloadRecords.size() == 0) {
            setShowContent(false);
            showEmptyView();
        }else {
            mAdapter.addData(downloadRecords);
        }

    }

    @Override
    public void PackageAdded(String packageName) {

        for (int i = 0; i < mAdapter.getItemCount(); i++) {
            if (mAdapter.getItem(i).getExtra4().equals(packageName)) {
                FileUtils.deleteFile(mAdapter.getItem(i).getSavePath()+ File.separator+ mAdapter.getItem(i).getSaveName());
                mAdapter.notifyItemChanged(i,"download");
                Log.d("hymmm", "PackageAdded: mAdapter.getItem(i).getSavePath() =  " + mAdapter.getItem(i).getSavePath()+ File.separator+ mAdapter.getItem(i).getSaveName());
                break;
            }
        }
    }


}
