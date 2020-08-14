package com.hym.appstore.ui.fragment;

import com.hym.appstore.bean.PageBean;
import com.hym.appstore.bean.Subject;
import com.hym.appstore.bean.SubjectDetail;
import com.hym.appstore.dagger2.component.AppComponent;
import com.hym.appstore.dagger2.component.DaggerSubjectComponent;
import com.hym.appstore.dagger2.module.SubjectModule;
import com.hym.appstore.presenter.SubjectPresenter;
import com.hym.appstore.presenter.contract.SubjectContract;

public abstract class BaseSubjectFragment extends ProgressFragment<SubjectPresenter> implements SubjectContract.SubjectView {


    @Override
    public void showSubject(PageBean<Subject> pageBean) {

    }

    @Override
    public void onLoadMoreComplete() {

    }

    @Override
    public void showSubjectDetail(SubjectDetail detail) {

    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerSubjectComponent.builder().appComponent(appComponent).subjectModule(new SubjectModule(this)).build().inject(this);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initEvent() {

    }
}
