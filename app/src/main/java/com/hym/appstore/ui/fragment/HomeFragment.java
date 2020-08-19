package com.hym.appstore.ui.fragment;


import android.util.Log;
import android.view.View;
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
    private HomeAdapter adapter;

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
        RxBus.getDefault().toObservable(User.class).subscribe(new Consumer<User>() {
            @Override
            public void accept(User user) {
                mPresenter.requestHomeData(true);
            }
        });


        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

//        mHomeRv.setItemAnimator(new DefaultItemAnimator());
        mPresenter.requestHomeData(true);
    }

    @Override
    protected void initEvent() {

    }





    @Override
    public void showResult(HomeBean homeBean) {
        adapter = new HomeAdapter(getActivity(), homeBean, mRxDownload);

        SlideInBottomAnimationAdapter alphaAdapter = new SlideInBottomAnimationAdapter(adapter);
        mRecyclerView.setAdapter(new ScaleInAnimationAdapter(alphaAdapter));
        Log.d("showResult", String.valueOf(mRecyclerView.getChildCount()));

        adapter.setInstallListener(new HomeAdapter.InstallListener() {
            @Override
            public void installAdded(AppInfoAdapter appInfoAdapter,String packageName) {
                Log.d("hymmm", "安装成功的名称installAdded: " + packageName);
                for (int i = 0; i < appInfoAdapter.getItemCount(); i++) {
                    if (appInfoAdapter.getItem(i).getPackageName().equals(packageName)) {
                        FileUtils.deleteFile(ACache.get(getContext()).getAsString(Constant.APK_DOWNLOAD_DIR) + File.separator + appInfoAdapter.getItem(i).getReleaseKeyHash()+".apk");
                        View childAt = mRecyclerView.getChildAt(i);
                        DownloadProgressButton btn = childAt.findViewById(R.id.btn_download);
                        btn.setStatue(DownloadFlag.NORMAL);
                        appInfoAdapter.notifyItemChanged(i);
                        break;
                    }
                }
            }

            @Override
            public void installRemoved(AppInfoAdapter appInfoAdapter,String packageName) {
                Log.d("hymmm", "卸载成功的名称installAdded: " + packageName);
                for (int i = 0; i < appInfoAdapter.getItemCount(); i++) {
                    if (appInfoAdapter.getItem(i).getPackageName().equals(packageName)) {
                        appInfoAdapter.notifyItemChanged(i);
                        break;
                    }
                }
            }
        });
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

    @Override
    public void PackageAdded(String packageName) {
        adapter.setInstallAdded(packageName);
    }

    @Override
    public void PackageRemoved(String packageName) {
        adapter.setInstallRemoved(packageName);
    }





}
