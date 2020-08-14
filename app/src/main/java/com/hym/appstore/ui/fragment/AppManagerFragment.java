package com.hym.appstore.ui.fragment;

import android.view.Gravity;
import android.view.View;

import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hym.appstore.R;
import com.hym.appstore.bean.AppInfoBean;
import com.hym.appstore.common.apkparset.AndroidApk;
import com.hym.appstore.dagger2.component.AppComponent;
import com.hym.appstore.dagger2.component.DaggerAppManagerComponent;
import com.hym.appstore.dagger2.module.AppManagerModule;
import com.hym.appstore.presenter.AppManagerPresent;
import com.hym.appstore.presenter.contract.AppManagerContract;

import java.util.List;

import butterknife.BindView;
import zlc.season.rxdownload2.entity.DownloadRecord;

public abstract class AppManagerFragment extends ProgressFragment<AppManagerPresent> implements AppManagerContract.AppManagerView {

    @BindView(R.id.home_rv)
    RecyclerView mRecyclerView;

    @Override
    protected int setLayoutResourceID() {
        return R.layout.template_recycler_view;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerAppManagerComponent.builder().appComponent(appComponent).appManagerModule(new AppManagerModule(this)).build().inject(this);
    }


    @Override
    protected void init() {
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.shape_question_diveder));
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mRecyclerView.setAdapter(setupAdapter());
    }

    protected abstract RecyclerView.Adapter setupAdapter();

    @Override
    public void showDownloading(List<DownloadRecord> downloadRecords) {

    }

    @Override
    public void showApps(List<AndroidApk> apps) {

    }

    @Override
    public void showUpdateApps(List<AppInfoBean> appInfoBeans) {

    }

    protected PopupMenu myPopupMenu(View v) {
        //定义PopupMenu对象
        PopupMenu popupMenu = new PopupMenu(getContext(), v);
        //设置PopupMenu对象的布局
        popupMenu.getMenuInflater().inflate(R.menu.pop_menu, popupMenu.getMenu());

        popupMenu.setGravity(Gravity.END);


        //显示菜单
        popupMenu.show();

        return popupMenu;
    }
}
