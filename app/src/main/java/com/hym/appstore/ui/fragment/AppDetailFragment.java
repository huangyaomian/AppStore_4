package com.hym.appstore.ui.fragment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.hym.appstore.R;
import com.hym.appstore.bean.AppInfoBean;
import com.hym.appstore.bean.User;
import com.hym.appstore.common.Constant;
import com.hym.appstore.common.imageloader.ImageLoader;
import com.hym.appstore.common.rx.RxBus;
import com.hym.appstore.common.utils.DateUtils;
import com.hym.appstore.dagger2.component.AppComponent;
import com.hym.appstore.dagger2.component.DaggerAppDetailComponent;
import com.hym.appstore.dagger2.module.AppDetailModule;
import com.hym.appstore.presenter.AppDetailPresenter;
import com.hym.appstore.presenter.contract.AppInfoContract;
import com.hym.appstore.ui.activity.AppDetailsActivity;
import com.hym.appstore.ui.adapter.AppInfoAdapter;
import com.xuexiang.xui.widget.textview.ExpandableTextView;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import io.reactivex.functions.Consumer;


public class AppDetailFragment extends ProgressFragment<AppDetailPresenter> implements AppInfoContract.AppDetailView {

    @BindView(R.id.view_gallery)
    LinearLayout viewGallery;

//    @BindView(R.id.expandable_text)
//    TextView expandableText;
    @BindView(R.id.expand_collapse)
    ImageButton expandCollapse;
    @BindView(R.id.view_introduction)
//    ExpandableTextView viewIntroduction;
    ExpandableTextView viewIntroduction;
    @BindView(R.id.txt_update_time)
    TextView txtUpdateTime;
    @BindView(R.id.txt_version)
    TextView txtVersion;
    @BindView(R.id.txt_apk_size)
    TextView txtApkSize;
    @BindView(R.id.txt_publisher)
    TextView txtPublisher;
    @BindView(R.id.txt_publisher2)
    TextView txtPublisher2;
    @BindView(R.id.recycler_view_same_dev)
    RecyclerView recyclerViewSameDev;
    @BindView(R.id.recycler_view_relate)
    RecyclerView recyclerViewRelate;
    @BindView(R.id.layout_view_same_dev)
    LinearLayout layoutViewSameDev;
    @BindView(R.id.layout_view_relate)
    LinearLayout layoutViewRelate;


    private int mAppId;
    private LayoutInflater mLayoutInflater;
    private AppInfoAdapter mAppInfoAdapterSame;
    private AppInfoAdapter mAppInfoAdapterRelate ;

    public AppDetailFragment(int appId) {
        this.mAppId = appId;
    }

    @Override
    protected void init() {
        RxBus.getDefault().toObservable(User.class).subscribe(new Consumer<User>() {
            @Override
            public void accept(User user) {
                mPresenter.getAppDetail(mAppId);
            }
        });
        mLayoutInflater = LayoutInflater.from(getActivity());
        mPresenter.getAppDetail(mAppId);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_app_detail;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerAppDetailComponent.builder().appComponent(appComponent).appDetailModule(new AppDetailModule(this))
                .build().inject(this);
    }

    @Override
    public void showAppDetail(AppInfoBean appInfoBean) {
        showScreenshot(appInfoBean.getScreenshot());

        viewIntroduction.setText(appInfoBean.getIntroduction());

        txtUpdateTime.setText(DateUtils.formatDate(appInfoBean.getUpdateTime()));
        txtApkSize.setText(appInfoBean.getApkSize() / 1024 / 1024 + "MB");
        txtVersion.setText(String.valueOf(appInfoBean.getVersionCode()));
        txtPublisher.setText(appInfoBean.getPublisherName());
        txtPublisher2.setText(appInfoBean.getPublisherName());

        if (appInfoBean.getSameDevAppInfoList().size() > 0) {
            layoutViewSameDev.setVisibility(View.VISIBLE);
            mAppInfoAdapterSame = AppInfoAdapter.builder().layout(R.layout.template_appinfo2_item)
                    .showName(false).showApkSize(true).showBrief(false).showCategoryName(false).showPosition(false).build();
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            recyclerViewSameDev.setLayoutManager(linearLayoutManager);

            mAppInfoAdapterSame.addData(appInfoBean.getSameDevAppInfoList());
            recyclerViewSameDev.setAdapter(mAppInfoAdapterSame);

            mAppInfoAdapterSame.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                    AppInfoBean appInfoBean = mAppInfoAdapterSame.getItem(position);
                    Intent intent = new Intent(getActivity(), AppDetailsActivity.class);
                    intent.putExtra("appInfo",appInfoBean);
                    intent.putExtra("isAnim",false);
                    startActivity(intent);
                }
            });
        } else {
            layoutViewSameDev.setVisibility(View.GONE);
        }

        if (appInfoBean.getRelateAppInfoList().size() > 0) {
            layoutViewRelate.setVisibility(View.VISIBLE);
            mAppInfoAdapterRelate = AppInfoAdapter.builder().layout(R.layout.template_appinfo2_item).showName(false)
                    .showApkSize(true).showBrief(false).showCategoryName(false).showPosition(false).build();
            recyclerViewRelate.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
            mAppInfoAdapterRelate.addData(appInfoBean.getRelateAppInfoList());
            recyclerViewRelate.setAdapter(mAppInfoAdapterRelate);

            mAppInfoAdapterRelate.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                    AppInfoBean appInfoBean = mAppInfoAdapterRelate.getItem(position);
                    Intent intent = new Intent(getActivity(), AppDetailsActivity.class);
                    intent.putExtra("appInfo",appInfoBean);
                    intent.putExtra("isAnim",false);
                    startActivity(intent);
                }
            });
        } else {
            layoutViewRelate.setVisibility(View.GONE);
        }









    }

    private void showScreenshot(String screenshot) {
        List<String> urls = Arrays.asList(screenshot.split(","));
        for (String url : urls) {
            ImageView imageView = (ImageView) mLayoutInflater.inflate(R.layout.template_imageview, viewGallery, false);
            ImageLoader.load(Constant.BASE_IMG_URL + url, imageView);
            viewGallery.addView(imageView);
        }
    }


}
