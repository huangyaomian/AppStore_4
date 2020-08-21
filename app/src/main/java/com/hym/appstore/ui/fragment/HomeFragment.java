package com.hym.appstore.ui.fragment;


import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hym.appstore.R;
import com.hym.appstore.bean.HomeBean;
import com.hym.appstore.bean.User;
import com.hym.appstore.common.Constant;
import com.hym.appstore.common.rx.RxBus;
import com.hym.appstore.common.utils.ACache;
import com.hym.appstore.common.utils.FileUtils;
import com.hym.appstore.dagger2.component.AppComponent;
import com.hym.appstore.dagger2.component.DaggerHomeComponent;
import com.hym.appstore.dagger2.module.HomeModule;
import com.hym.appstore.presenter.HomePresenter;
import com.hym.appstore.presenter.contract.AppInfoContract;
import com.hym.appstore.ui.adapter.AppInfoAdapter;
import com.hym.appstore.ui.adapter.HomeAdapter;
import com.hym.appstore.ui.widget.DownloadProgressButton;

import java.io.File;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.functions.Consumer;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;
import zlc.season.rxdownload2.RxDownload;
import zlc.season.rxdownload2.entity.DownloadFlag;

public class HomeFragment extends ProgressFragment<HomePresenter> implements AppInfoContract.View {


    @BindView(R.id.home_rv)
    RecyclerView mRecyclerView;
    private HomeAdapter mAdapter;

    @Inject
    RxDownload mRxDownload;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerHomeComponent.builder().appComponent(appComponent).homeModule(new HomeModule(this)).build().inject(this);
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.template_recycler_view;
    }

    @Override
    protected void initView() {
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
    }


    @Override
    protected void init() {

        //这里为了解决recycleview不能撑满全屏的问题，这里layoutManager不管你布局里是否设置，都不准确，所以需要在代码里
        //重新设置MATCH_PARENT
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext()) {
            @Override
            public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        };
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

//        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

//        mHomeRv.setItemAnimator(new DefaultItemAnimator());
        mPresenter.requestHomeData(true);
    }

    @Override
    protected void initEvent() {

    }





    @Override
    public void showResult(HomeBean homeBean) {

        mAdapter = new HomeAdapter(getActivity(), homeBean, mRxDownload);

        SlideInBottomAnimationAdapter alphaAdapter = new SlideInBottomAnimationAdapter(mAdapter);
        mRecyclerView.setAdapter(new ScaleInAnimationAdapter(alphaAdapter));
        Log.d("showResult", String.valueOf(mRecyclerView.getChildCount()));

    }



    @Override
    public void showNoData() {

    }

    @Override
    public void onRequestPermissionSuccess() {

    }

    @Override
    public void onRequestPermissionError() {
        Toast.makeText(getActivity(),"您已拒絕授權!",Toast.LENGTH_SHORT).show();
    }






}
