package com.hym.appstore.dagger2.module;

import com.hym.appstore.dagger2.scope.FragmentScope;
import com.hym.appstore.data.SubjectModel;
import com.hym.appstore.data.okhttp.ApiService;
import com.hym.appstore.presenter.contract.SubjectContract;

import dagger.Module;
import dagger.Provides;

@Module
public class SubjectModule {

    private SubjectContract.SubjectView mView;

    public SubjectModule(SubjectContract.SubjectView view) {
        this.mView = view;
    }


    @FragmentScope
    @Provides
    public SubjectContract.ISubjectModel provideModel(ApiService apiService) {

        return new SubjectModel(apiService);
    }

    @FragmentScope
    @Provides
    public SubjectContract.SubjectView provideView() {
        return mView;
    }


}
