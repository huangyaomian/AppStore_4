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
import com.hym.appstore.bean.AppInfoBean;
import com.hym.appstore.common.Constant;
import com.hym.appstore.common.apkparset.AndroidApk;
import com.hym.appstore.common.rx.RxSchedulers;
import com.hym.appstore.common.utils.FileUtils;
import com.hym.appstore.ui.activity.AppDetailsActivity3;
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
        //需要看看能不能通过包名请求
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                AppInfoBean appInfoBean = new AppInfoBean();
                AndroidApk item = (AndroidApk) adapter.getItem(position);
                appInfoBean.setId(item.getId());
                appInfoBean.setIcon(item.getIcon());
                Intent intent = new Intent(getActivity(), AppDetailsActivity3.class);
                intent.putExtra("appInfo",appInfoBean);
                startActivity(intent);
            }
        });
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
        if (apps.size() == 0){
            showEmptyView("暂无数据");
        }else {
            mAdapter.addData(apps);
        }

    }

    @Override
    public void PackageAdded(String packageName) {
        for (int i = 0; i < mAdapter.getItemCount(); i++) {
            if (mAdapter.getItem(i).getPackageName().equals(packageName)) {
                mAdapter.notifyItemChanged(i);
                break;
            }
        }
    }
}
