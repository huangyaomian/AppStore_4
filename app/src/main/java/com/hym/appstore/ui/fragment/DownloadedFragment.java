package com.hym.appstore.ui.fragment;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemLongClickListener;
import com.hym.appstore.common.Constant;
import com.hym.appstore.common.apkparset.AndroidApk;
import com.hym.appstore.common.rx.RxSchedulers;
import com.hym.appstore.common.utils.FileUtils;
import com.hym.appstore.ui.adapter.AndroidApkAdapter;

import java.util.List;


public class DownloadedFragment extends AppManagerFragment {

    private AndroidApkAdapter mAdapter;

    private PopupMenu mPopupMenu;



    @Override
    protected void init() {
//        RxBus.getDefault().toObservable(User.class).subscribe(new Consumer<User>() {
//            @Override
//            public void accept(User user) {
//                mPresenter.getLocalApks();
//            }
//        });
        super.init();
        mPresenter.getLocalApks();
    }

    @Override
    protected RecyclerView.Adapter setupAdapter() {
        mAdapter = new AndroidApkAdapter(getContext(), AndroidApkAdapter.FLAG_APK);
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
                        Log.d("hymmm", "onMenuItemClick: " + Constant.BASE_DOWNLOAD_URL + mAdapter.getItem(position).getDownloadUrl());
                        mPresenter.DelDownloadingApp(mAdapter.getItem(position).getDownloadUrl(),true)
                                .compose(RxSchedulers.io_main()).subscribe();
                        mAdapter.removeAt(position);
                        return true;
                    }

                });

                return true;
            }
        });
    }


    @Override
    public void showApps(List<AndroidApk> apps) {
        if (apps.size() ==0 || apps ==null){
            showEmptyView("暂无数据");
        }else {
            mAdapter.addData(apps);
        }

    }

    @Override
    public void PackageAdded(String packageName) {
//        super.PackageAdded(packageName);
        mAdapter.notify();
        for (int i = 0; i < mAdapter.getItemCount(); i++) {
            if (mAdapter.getItem(i).getPackageName().equals(packageName)) {
                mAdapter.notifyItemChanged(i);
                FileUtils.deleteFile(mAdapter.getItem(i).getApkPath());
                break;
            }
        }
    }
}
