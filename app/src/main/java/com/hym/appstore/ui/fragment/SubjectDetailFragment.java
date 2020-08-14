package com.hym.appstore.ui.fragment;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hym.appstore.R;
import com.hym.appstore.bean.Subject;
import com.hym.appstore.bean.SubjectDetail;
import com.hym.appstore.common.Constant;
import com.hym.appstore.common.imageloader.ImageLoader;
import com.hym.appstore.ui.adapter.AppInfoAdapter;

import javax.inject.Inject;

import butterknife.BindView;
import zlc.season.rxdownload2.RxDownload;

public class SubjectDetailFragment extends BaseSubjectFragment {

    @BindView(R.id.imageview)
    ImageView mImageView;
    @BindView(R.id.txt_desc)
    TextView mtxtDesc;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Inject
    RxDownload mRxDownload;


    private Subject mSubject;

    private AppInfoAdapter mAdapter;

    public SubjectDetailFragment() {

    }

    public SubjectDetailFragment setArgs(Subject subject) {
        this.mSubject = subject;
        return this;
    }


    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_subject_detail;
    }


    @Override
    protected void init() {
        initRecycleView();
        mPresenter.getSubjectDetail(mSubject.getRelatedId());
    }

    private void initRecycleView() {
        mAdapter = AppInfoAdapter.builder().showBrief(false).showCategoryName(true)
                .rxDownload(mRxDownload).build();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);

        mRecyclerView.addItemDecoration(itemDecoration);

        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void showSubjectDetail(SubjectDetail detail) {
        ImageLoader.load(Constant.BASE_IMG_URL + detail.getPhoneBigIcon(), mImageView);

        mtxtDesc.setText(detail.getDescription());

        mAdapter.addData(detail.getListApp());
    }
}
