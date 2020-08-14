package com.hym.appstore.data;


import com.hym.appstore.bean.BaseBean;
import com.hym.appstore.bean.PageBean;
import com.hym.appstore.bean.Subject;
import com.hym.appstore.bean.SubjectDetail;
import com.hym.appstore.data.okhttp.ApiService;
import com.hym.appstore.presenter.contract.SubjectContract;

import io.reactivex.Observable;

public class SubjectModel implements SubjectContract.ISubjectModel {
    private ApiService mApiService;

    public SubjectModel(ApiService apiService) {
        this.mApiService = apiService;

    }


    @Override
    public Observable<BaseBean<PageBean<Subject>>> getSubjects(int page) {
        return mApiService.subjects(page);
    }

    @Override
    public Observable<BaseBean<SubjectDetail>> getSubjectDetail(int id) {
        return mApiService.subjectDetail(id);
    }

}
