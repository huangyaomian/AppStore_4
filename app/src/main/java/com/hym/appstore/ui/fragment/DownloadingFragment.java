package com.hym.appstore.ui.fragment;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemLongClickListener;
import com.hym.appstore.R;
import com.hym.appstore.common.rx.RxSchedulers;
import com.hym.appstore.ui.adapter.DownloadingAdapter;

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


    }

    @Override
    public void showDownloading(List<DownloadRecord> downloadRecords) {
        mAdapter.addData(downloadRecords);
    }



}
