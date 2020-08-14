package com.hym.appstore.ui.fragment;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.hym.appstore.R;
import com.hym.appstore.bean.PageBean;
import com.hym.appstore.bean.Subject;
import com.hym.appstore.common.rx.RxBus;
import com.hym.appstore.ui.adapter.SubjectAdapter;
import com.hym.appstore.ui.widget.SpaceItemDecoration2;

import butterknife.BindView;

public class SubjectFragment extends BaseSubjectFragment {


    @BindView(R.id.home_rv)
    RecyclerView mRecyclerView;

    private SubjectAdapter subjectAdapter;

    int page = 0;

    @Override
    protected int setLayoutResourceID() {
        return R.layout.template_recycler_view;
    }


    @Override
    protected void init() {
        initRecyclerView();
        mPresenter.getSubject(page);
    }


    protected void initRecyclerView(){
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));

        SpaceItemDecoration2 dividerDecoration = new SpaceItemDecoration2(5);
        mRecyclerView.addItemDecoration(dividerDecoration);

        subjectAdapter = new SubjectAdapter();
        subjectAdapter.setAnimationEnable(true);

        subjectAdapter.setAnimationWithDefault(BaseQuickAdapter.AnimationType.AlphaIn);
        mRecyclerView.setAdapter(subjectAdapter);

    }

    @Override
    public void showSubject(PageBean<Subject> pageBean) {
        subjectAdapter.addData(pageBean.getDatas());

        if(pageBean.isHasMore()){
            //如果还有下一页的话就再加一页
            page++;
        }
        //设置一下loadMore的开关
        subjectAdapter.getLoadMoreModule().setEnableLoadMore(pageBean.isHasMore());
    }


    @Override
    protected void initEvent() {
        subjectAdapter.getLoadMoreModule().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                mPresenter.getSubject(page);
            }
        });

        subjectAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                Subject subject = subjectAdapter.getItem(position);

                RxBus.getDefault().post(subject);
            }
        });


    }



}
