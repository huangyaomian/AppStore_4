package com.hym.appstore.ui.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.hym.appstore.R;
import com.hym.appstore.bean.AppInfoBean;
import com.hym.appstore.bean.User;
import com.hym.appstore.common.Constant;
import com.hym.appstore.common.imageloader.ImageLoader;
import com.hym.appstore.common.rx.RxBus;
import com.hym.appstore.common.utils.AppUtils;
import com.hym.appstore.common.utils.DateUtils;
import com.hym.appstore.common.utils.UIUtils;
import com.hym.appstore.dagger2.component.AppComponent;
import com.hym.appstore.dagger2.component.DaggerAppDetailComponent;
import com.hym.appstore.dagger2.module.AppDetailModule;
import com.hym.appstore.presenter.AppDetailPresenter;
import com.hym.appstore.presenter.contract.AppInfoContract;
import com.hym.appstore.service.receiver.MyInstallListener;
import com.hym.appstore.service.receiver.MyInstallReceiver;
import com.hym.appstore.ui.adapter.AppInfoAdapter;
import com.hym.appstore.ui.adapter.screenShortAdapter;
import com.hym.appstore.ui.widget.DownloadButtonController2Detail;
import com.hym.appstore.ui.widget.DownloadProgressButton2Detail;
import com.hym.appstore.ui.widget.SpaceItemDecoration2;
import com.hym.appstore.ui.widget.SpaceItemDecoration3;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.ionicons_typeface_library.Ionicons;
import com.xuexiang.xui.widget.textview.ExpandableTextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import cc.shinichi.library.ImagePreview;
import cc.shinichi.library.bean.ImageInfo;
import io.reactivex.functions.Consumer;
import zlc.season.rxdownload2.RxDownload;
import zlc.season.rxdownload2.entity.DownloadFlag;

public class AppDetailsActivity3 extends ProgressActivity<AppDetailPresenter> implements AppInfoContract.AppDetailView, MyInstallListener, DownloadButtonController2Detail.FlagChangeListener {


    @BindView(R.id.img_icon)
    ImageView imgIcon;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout mToolbarLayout;


    @BindView(R.id.view_gallery)
    RecyclerView viewGallery;

    @BindView(R.id.expand_collapse)
    ImageButton expandCollapse;
    @BindView(R.id.view_introduction)
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
    @BindView(R.id.download_detail_btn)
    DownloadProgressButton2Detail mDownloadDetailBtn;
    @BindView(R.id.linearLayout_btn)
    LinearLayout mLinearLayoutBtn;

    @Inject
    RxDownload mRxDownload;

    @BindView(R.id.tool_bar)
    Toolbar mToolbar;

    @BindView(R.id.app_bar)
    AppBarLayout mAppBarLayout;



    private int mAppId;
    private LayoutInflater mLayoutInflater;
    private AppInfoAdapter mAppInfoAdapterSame;
    private AppInfoAdapter mAppInfoAdapterRelate;
    private DownloadButtonController2Detail mDownloadButtonController2Detail;
    private AppInfoBean mAppInfoBean;
    private MyInstallReceiver mMyInstallReceiver;
    private int mFlag = 0;



    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_app_details3;
    }


    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerAppDetailComponent.builder().appComponent(appComponent).appDetailModule(new AppDetailModule(this))
                .build().injectActivity(this);
    }

    @Override
    public void init() {

        initToolbar();

        registerMyInstallReceiver();

        mAppInfoBean = (AppInfoBean) getIntent().getSerializableExtra("appInfo");
        if (mAppInfoBean != null) {
            mToolbar.setTitle(mAppInfoBean.getDisplayName());
            mToolbarLayout.setTitle(mAppInfoBean.getDisplayName());
            ImageLoader.load(Constant.BASE_IMG_URL + mAppInfoBean.getIcon(), imgIcon);
        }

        mAppId = mAppInfoBean.getId();

        mDownloadButtonController2Detail = new DownloadButtonController2Detail(mRxDownload);
        mDownloadButtonController2Detail.setFlagChangeListener(this);
        mLayoutInflater = LayoutInflater.from(this);

    }

    private void initToolbar(){
        setSupportActionBar(mToolbar);

        mToolbar.setNavigationIcon(
                new IconicsDrawable(this)
                        .icon(Ionicons.Icon.ion_ios_arrow_back)
                        .sizeDp(16)
                        .color(getResources().getColor(R.color.theme_black)
                        )
        );

        mToolbar.setOverflowIcon(new IconicsDrawable(this, Ionicons.Icon.ion_android_more_vertical).color(getResources().getColor(R.color.TextColor)).actionBar());

    }


    @Override
    public void initView() {

        mPresenter.getAppDetail(mAppId);
    }

    @Override
    public void initEvent() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        RxBus.getDefault().toObservable(User.class).subscribe(new Consumer<User>() {
            @Override
            public void accept(User user) {
                mPresenter.getAppDetail(mAppId);
            }
        });
    }


    @Override
    public void showAppDetail(AppInfoBean appInfoBean) {
        if (appInfoBean != null) {
            mAppInfoBean = appInfoBean;
            mDownloadButtonController2Detail.handClick(mDownloadDetailBtn,appInfoBean);
        }


        showScreenshot(appInfoBean.getScreenshot());


        viewIntroduction.setText(appInfoBean.getIntroduction());

        txtUpdateTime.setText(DateUtils.formatDate(appInfoBean.getUpdateTime()));
        txtApkSize.setText(appInfoBean.getApkSize() / 1024 / 1024 + "MB");
        txtVersion.setText(String.valueOf(appInfoBean.getVersionCode()));
        txtPublisher.setText(appInfoBean.getPublisherName());
        txtPublisher2.setText(appInfoBean.getPublisherName());

        if (appInfoBean.getSameDevAppInfoList().size() > 0) {
            recyclerViewSameDev.setNestedScrollingEnabled(false);
            layoutViewSameDev.setVisibility(View.VISIBLE);
            mAppInfoAdapterSame = AppInfoAdapter.builder().layout(R.layout.template_appinfo2_item)
                    .showName(false).showApkSize(true).showBrief(false).showCategoryName(false).showPosition(false).build();
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            recyclerViewSameDev.setLayoutManager(linearLayoutManager);
            SpaceItemDecoration3 spaceItemDecoration3 = new SpaceItemDecoration3(UIUtils.dp2px(16));
            recyclerViewSameDev.addItemDecoration(spaceItemDecoration3);

            mAppInfoAdapterSame.addData(appInfoBean.getSameDevAppInfoList());
            recyclerViewSameDev.setAdapter(mAppInfoAdapterSame);

            mAppInfoAdapterSame.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                    AppInfoBean appInfoBean = mAppInfoAdapterSame.getItem(position);
                    Intent intent = new Intent(AppDetailsActivity3.this, AppDetailsActivity3.class);
                    intent.putExtra("appInfo", appInfoBean);
                    intent.putExtra("isAnim", false);
                    startActivity(intent);
                }
            });
        } else {
            layoutViewSameDev.setVisibility(View.GONE);
        }

        if (appInfoBean.getRelateAppInfoList().size() > 0) {
            recyclerViewRelate.setNestedScrollingEnabled(false);
            layoutViewRelate.setVisibility(View.VISIBLE);
            mAppInfoAdapterRelate = AppInfoAdapter.builder().layout(R.layout.template_appinfo2_item).showName(false)
                    .showApkSize(true).showBrief(false).showCategoryName(false).showPosition(false).build();
            recyclerViewRelate.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            SpaceItemDecoration3 spaceItemDecoration3 = new SpaceItemDecoration3(UIUtils.dp2px(16));
            recyclerViewRelate.addItemDecoration(spaceItemDecoration3);
            mAppInfoAdapterRelate.addData(appInfoBean.getRelateAppInfoList());
            recyclerViewRelate.setAdapter(mAppInfoAdapterRelate);

            mAppInfoAdapterRelate.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                    AppInfoBean appInfoBean = mAppInfoAdapterRelate.getItem(position);
                    Intent intent = new Intent(AppDetailsActivity3.this, AppDetailsActivity3.class);
                    intent.putExtra("appInfo", appInfoBean);
                    startActivity(intent);
                }
            });
        } else {
            layoutViewRelate.setVisibility(View.GONE);
        }


    }

    private void showScreenshot(String screenshot) {
        viewGallery.setNestedScrollingEnabled(false);
        List<String> urls = Arrays.asList(screenshot.split(","));
        List<String> completeUrls = new ArrayList<>();

        ImageInfo imageInfo;
        final List<ImageInfo> imageInfoList = new ArrayList<>();

        for (String url : urls) {
            imageInfo = new ImageInfo();
            imageInfo.setOriginUrl(Constant.HD_BASE_IMG_URL + url);// 原图url
            imageInfo.setThumbnailUrl(Constant.BASE_IMG_URL + url);// 缩略图url
            imageInfoList.add(imageInfo);
            completeUrls.add(Constant.BASE_IMG_URL + url);
        }


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);// 设置 recyclerview 布局方式为横向布局

        SpaceItemDecoration3 dividerDecoration = new SpaceItemDecoration3(UIUtils.dp2px(4));
        viewGallery.addItemDecoration(dividerDecoration);
        viewGallery.setLayoutManager(layoutManager);
        screenShortAdapter adapter = new screenShortAdapter();
        adapter.addData(completeUrls);
        viewGallery.setAdapter(adapter);


        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                ImagePreview
                        .getInstance()
                        // 上下文，必须是activity，不需要担心内存泄漏，本框架已经处理好；
                        .setContext(AppDetailsActivity3.this)
                        // 设置从第几张开始看（索引从0开始）
                        .setIndex(position)
                        // 1：第一步生成的imageInfo List
                        .setImageInfoList(imageInfoList)
                        .setShowCloseButton(true)
                        .start();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_details, menu); //加载toolbar.xml 菜单文件
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        Log.d("hymmm", "onPrepareOptionsMenu: mFlag=" + mFlag);
        super.onPrepareOptionsMenu(menu);

        if (mFlag == DownloadFlag.COMPLETED){
            for (int i = 0; i < menu.size(); i++) {
                if (menu.getItem(i).getItemId() == R.id.delete_apk) {
                    menu.getItem(i).setVisible(true);

                }else if (menu.getItem(i).getItemId() == R.id.re_download){
                    menu.getItem(i).setVisible(true);
                }
            }
        }else if (mFlag == DownloadFlag.STARTED || mFlag == DownloadFlag.PAUSED || mFlag == DownloadFlag.WAITING || mFlag == DownloadFlag.FAILED){
            for (int i = 0; i < menu.size(); i++) {
                if (menu.getItem(i).getItemId() == R.id.cancel_download) {
                    menu.getItem(i).setVisible(true);
                }else if (menu.getItem(i).getItemId() == R.id.re_download){
                    menu.getItem(i).setVisible(true);
                }
            }
        }else if (mFlag == DownloadFlag.INSTALLED || mFlag == DownloadFlag.UPDATE){
            for (int i = 0; i < menu.size(); i++) {
                if (menu.getItem(i).getItemId() == R.id.uninstall) {
                    menu.getItem(i).setVisible(true);
                    break;
                }
            }
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_apk:
                //删除下载记录和删除本地apk
                mPresenter.DelDownloadApp(mAppInfoBean.getAppDownloadInfo().getDownloadUrl(),true,mRxDownload).subscribe();
                mDownloadButtonController2Detail.handClick(mDownloadDetailBtn,mAppInfoBean);
                break;
            case R.id.re_download:
                mDownloadButtonController2Detail.ReDownload(mDownloadDetailBtn,mAppInfoBean);
                break;
            case R.id.uninstall:
                AppUtils.uninstallApk(this, mAppInfoBean.getPackageName());
                break;
            case R.id.cancel_download:
                mPresenter.DelDownloadApp(mAppInfoBean.getAppDownloadInfo().getDownloadUrl(),true,mRxDownload).subscribe();
                break;
        }
        return true;
    }


    private void registerMyInstallReceiver(){
        mMyInstallReceiver = new MyInstallReceiver();
        mMyInstallReceiver.registerListener(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.PACKAGE_ADDED");
        filter.addAction("android.intent.action.PACKAGE_REMOVED");
        filter.addAction("android.intent.action.PACKAGE_REPLACED");
        filter.addDataScheme("package");
        this.registerReceiver(mMyInstallReceiver, filter);
    }

    @Override
    public void PackageAdded(String packageName) {
        Log.d("hymmm", "AppDetailsActivity2: " + "安装了应用："+packageName);
        if (packageName.equals(mAppInfoBean.getPackageName())) {
            mPresenter.DelDownloadApp(mAppInfoBean.getAppDownloadInfo().getDownloadUrl(),true,mRxDownload).subscribe();
            mDownloadButtonController2Detail.handClick(mDownloadDetailBtn,mAppInfoBean);
        }

    }

    @Override
    public void PackageRemoved(String packageName) {
        Log.d("hymmm", "AppDetailsActivity2: " + "卸載了应用："+packageName);
        if (packageName.equals(mAppInfoBean.getPackageName())) {
            mDownloadButtonController2Detail.handClick(mDownloadDetailBtn,mAppInfoBean);
        }
    }

    @Override
    public void PackageReplaced(String packageName) {

    }

    @Override
    protected void onDestroy() {
        if(mMyInstallReceiver != null) {
            this.unregisterReceiver(mMyInstallReceiver);
        }
        super.onDestroy();
    }


    @Override
    public void getFlagChange() {
        Log.d("hymmm", "getFlagChange: " + mDownloadDetailBtn.getTag(R.id.tag_apk_flag));
        mFlag = (int) mDownloadDetailBtn.getTag(R.id.tag_apk_flag);
        this.invalidateOptionsMenu();//通知menu更新
    }

}
