package com.hym.appstore.ui.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.hym.appstore.R;
import com.hym.appstore.bean.AppInfoBean;
import com.hym.appstore.bean.PageBean;
import com.hym.appstore.bean.User;
import com.hym.appstore.common.Constant;
import com.hym.appstore.common.rx.RxBus;
import com.hym.appstore.common.utils.ACache;
import com.hym.appstore.common.utils.FileUtils;
import com.hym.appstore.presenter.AppInfoPresenter;
import com.hym.appstore.presenter.contract.AppInfoContract;
import com.hym.appstore.ui.activity.AppDetailsActivity;
import com.hym.appstore.ui.adapter.AppInfoAdapter;

import java.io.File;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.functions.Consumer;
import zlc.season.rxdownload2.RxDownload;


public abstract class AppInfoFragment extends ProgressFragment<AppInfoPresenter> implements AppInfoContract.AppInfoView {

    @BindView(R.id.home_rv)
    RecyclerView mHomeRv;

    @Inject
    RxDownload mRxDownload;

    int page = 0;

    protected AppInfoAdapter mAppInfoAdapter;


    @Override
    protected int setLayoutResourceID() {
        return R.layout.template_recycler_view;
    }

    @Override
    protected void initView() {

    }


    @Override
    protected void init() {
        RxBus.getDefault().toObservable(User.class).subscribe(new Consumer<User>() {
            @Override
            public void accept(User user) {
                mPresenter.requestData(setType(),page);
            }
        });
        mPresenter.requestData(setType(),page);
        initRecyclerView();
    }

    protected void initRecyclerView(){
        mHomeRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.shape_question_diveder));
        mHomeRv.addItemDecoration(dividerItemDecoration);
        mAppInfoAdapter = buildAdapter();
        mAppInfoAdapter.setAnimationEnable(true);
        mAppInfoAdapter.setAnimationWithDefault(BaseQuickAdapter.AnimationType.AlphaIn);
        mHomeRv.setAdapter(mAppInfoAdapter);
    }

    abstract AppInfoAdapter buildAdapter();

    abstract int setType();

    @Override
    protected void initEvent() {
        mAppInfoAdapter.getLoadMoreModule().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                mPresenter.requestData(setType(),page);
            }
        });


        // 设置点击事件
        mAppInfoAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                Toast.makeText(getContext(),"AppInfoFragment--onItemClick " + position, Toast.LENGTH_SHORT).show();
                mMyApplication.setView(view);
                AppInfoBean appInfoBean = mAppInfoAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), AppDetailsActivity.class);
                intent.putExtra("appInfo",appInfoBean);
                intent.putExtra("isAnim",true);
                startActivity(intent);
            }
        });
    }


    @Override
    public void showResult(PageBean<AppInfoBean> data) {
        mAppInfoAdapter.addData(data.getDatas());
        if (data.isHasMore()){
            page++;
        }
        mAppInfoAdapter.getLoadMoreModule().setEnableLoadMore(data.isHasMore());
    }

    @Override
    public void onLoadMoreComplete() {
// 当前这次数据加载完毕，调用此方法
        mAppInfoAdapter.getLoadMoreModule().loadMoreComplete();
    }

}
