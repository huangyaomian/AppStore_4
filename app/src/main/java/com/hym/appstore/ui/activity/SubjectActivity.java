package com.hym.appstore.ui.activity;

import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.hym.appstore.R;
import com.hym.appstore.bean.Subject;
import com.hym.appstore.common.rx.RxBus;
import com.hym.appstore.dagger2.component.AppComponent;
import com.hym.appstore.ui.fragment.SubjectDetailFragment;
import com.hym.appstore.ui.fragment.SubjectFragment;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.ionicons_typeface_library.Ionicons;

import butterknife.BindView;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class SubjectActivity extends BaseActivity {

    @BindView(R.id.tool_bar)
    Toolbar mToolBar;

    private FragmentManager mFragmentManager;
    SubjectFragment mSubjectFragment;
    SubjectDetailFragment mSubjectDetailFragment;

    public static final int FRAGMENT_SUBJECT = 0;
    public static final int FRAGMENT_DETAIL = 1;

    private int fragmentIndex = FRAGMENT_SUBJECT;

    private Subject mSubject = new Subject();


    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_subject;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public void init() {

        if (getIntent().getSerializableExtra("subjectId") != null) {
            mSubject.setRelatedId(Integer.parseInt((String) getIntent().getSerializableExtra("subjectId")));
        }else {
            mSubject.setRelatedId(0);
        }

        mToolBar.setNavigationIcon(
                new IconicsDrawable(this)
                        .icon(Ionicons.Icon.ion_ios_arrow_back)
                        .sizeDp(16)
                        .color(getResources().getColor(R.color.theme_black)
                        )
        );

        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                handNavigation();
            }
        });
        mFragmentManager = getSupportFragmentManager();


        showSubjectDetailFragmentRxBus();

        if (mSubject.getRelatedId() > 0){
            showSubjectDetailFragment(mSubject);
        }else {
            showSubjectFragment();
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        handNavigation();
    }

    @Override
    public void initView() {
//        showSubjectFragment();
    }

    @Override
    public void initEvent() {

    }

    private void showSubjectFragment(){
        fragmentIndex = FRAGMENT_SUBJECT;
        mToolBar.setTitle("主题");
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        hideFragment(fragmentTransaction);
        if (mSubjectFragment == null) {
            mSubjectFragment = new SubjectFragment();
            fragmentTransaction.add(R.id.content,mSubjectFragment);
        }else {
            fragmentTransaction.show(mSubjectFragment);
        }
        fragmentTransaction.commit();
    }

    private void handNavigation() {
        if (fragmentIndex == FRAGMENT_SUBJECT || mSubject.getRelatedId()> 0) {
            finish();
        } else {
            showSubjectFragment();
        }
    }

    private void showSubjectDetailFragmentRxBus() {

        RxBus.getDefault().toObservable(Subject.class).subscribe(new Consumer<Subject>() {
            @Override
            public void accept(@NonNull Subject subject) throws Exception {
                showSubjectDetailFragment(subject);
            }
        });
    }

    private void  showSubjectDetailFragment(Subject subject){

        fragmentIndex = FRAGMENT_DETAIL;
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        if(mSubjectDetailFragment != null){
            ft.remove(mSubjectDetailFragment);
        }
        mSubjectDetailFragment = new SubjectDetailFragment();
        mSubjectDetailFragment.setArgs(subject);
        ft.add(R.id.content,mSubjectDetailFragment);

        ft.commit();

        mToolBar.setTitle(subject.getTitle());
    }

    private void  hideFragment(FragmentTransaction ft){

        if(mSubjectFragment != null){
            ft.hide(mSubjectFragment);
        }
        if(mSubjectDetailFragment != null){
            ft.hide(mSubjectDetailFragment);
        }


    }


}
