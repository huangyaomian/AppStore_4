package com.hym.appstore.ui.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.hym.appstore.R;
import com.hym.appstore.bean.AppInfoBean;
import com.hym.appstore.bean.SearchResult;
import com.hym.appstore.common.db.DBManager;
import com.hym.appstore.common.rx.RxSchedulers;
import com.hym.appstore.dagger2.component.AppComponent;
import com.hym.appstore.dagger2.component.DaggerSearchComponent;
import com.hym.appstore.dagger2.module.SearchModule;
import com.hym.appstore.presenter.SearchPresenter;
import com.hym.appstore.presenter.contract.SearchContract;
import com.hym.appstore.ui.adapter.AppInfoAdapter;
import com.hym.appstore.ui.adapter.SearchHistoryAdapter;
import com.hym.appstore.ui.adapter.SuggestionAdapter;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.ionicons_typeface_library.Ionicons;
import com.xuexiang.xui.widget.progress.loading.MiniLoadingView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;
import zlc.season.rxdownload2.RxDownload;

public class SearchActivity extends BaseActivity<SearchPresenter> implements SearchContract.SearchView {

//    https://github.com/HowieHai/Android_MobileAssistant/tree/ef5be8ea17cf1e9655479e97140e7bfa7bb393d6

    @BindView(R.id.searchTextView)
    EditText mSearchTextView;
    @BindView(R.id.action_clear_btn)
    ImageView mActionClearBtn;

    @BindView(R.id.search_bar)
    RelativeLayout mSearchBar;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.btn_clear)
    ImageView mBtnClear;

    @BindView(R.id.recycler_view_history)
    RecyclerView mRecyclerViewHistory;

    @BindView(R.id.layout_history)
    LinearLayout mLayoutHistory;

    @BindView(R.id.recycler_view_suggestion)
    RecyclerView mRecyclerViewSuggestion;

    @BindView(R.id.recycler_view_result)
    RecyclerView mRecyclerViewResult;

    @BindView(R.id.activity_search)
    LinearLayout mActivitySearch;
    @BindView(R.id.progress)
    MiniLoadingView mProgress;
    @BindView(R.id.loading)
    TextView mLoading;
    @BindView(R.id.view_progress)
    LinearLayout mViewProgress;


    private SuggestionAdapter mSuggestionAdapter;
    private AppInfoAdapter mAppInfoAdapter;
    private SearchHistoryAdapter mHistoryAdapter;

    @Inject
    RxDownload rxDownload;

    private Disposable disposable;


    public static final int STATUS_REQUESTING = 0;
    public static final int STATUS_FINISH = 1;
    public int status = STATUS_FINISH;


    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_search;
    }

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerSearchComponent.builder().appComponent(appComponent)
                .searchModule(new SearchModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void init() {
        mPresenter.showHistory();
        setupSearchView();
        setupSuggestionRecyclerView();
        initSearchResultRecycleView();
        mSearchTextView.setSaveEnabled(false);
    }

    @Override
    public void initView() {
        mToolbar.setNavigationIcon(
                new IconicsDrawable(this)
                        .icon(Ionicons.Icon.ion_ios_arrow_back)
                        .sizeDp(16)
                        .paddingDp(0)
                        .color(getResources().getColor(R.color.TextColor)
                        )
        );

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        mActionClearBtn.setImageDrawable(new IconicsDrawable(this, Ionicons.Icon.ion_ios_close_empty)
                .color(ContextCompat.getColor(this, R.color.theme_grey)).sizeDp(12));


        mBtnClear.setImageDrawable(new IconicsDrawable(this, Ionicons.Icon.ion_ios_trash_outline)
                .color(ContextCompat.getColor(this, R.color.theme_grey)).sizeDp(16));

        RxView.clicks(mBtnClear).subscribe(new Consumer<Object>() {

            @Override
            public void accept(@NonNull Object o) throws Exception {
                //这里需要移除view，并且将历史记录清除
                DBManager.DeleteAllSearchHistory();
                setLayoutHistoryGone();
            }
        });
    }

    @Override
    public void initEvent() {

    }


    private void setupSuggestionRecyclerView() {

        mSuggestionAdapter = new SuggestionAdapter(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerViewSuggestion.setLayoutManager(layoutManager);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);

        mRecyclerViewSuggestion.addItemDecoration(itemDecoration);

        mRecyclerViewSuggestion.setAdapter(mSuggestionAdapter);

        mSuggestionAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@androidx.annotation.NonNull BaseQuickAdapter adapter, @androidx.annotation.NonNull View view, int position) {
                mSearchTextView.setText(mSuggestionAdapter.getItem(position));
                search(mSuggestionAdapter.getItem(position));

            }
        });

    }

    private void initSearchResultRecycleView() {
        mAppInfoAdapter = AppInfoAdapter.builder().showBrief(false).showCategoryName(true).rxDownload(rxDownload).build();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerViewResult.setLayoutManager(layoutManager);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        mRecyclerViewResult.addItemDecoration(itemDecoration);

        SlideInBottomAnimationAdapter alphaAdapter = new SlideInBottomAnimationAdapter(mAppInfoAdapter);
        mRecyclerViewResult.setAdapter(new ScaleInAnimationAdapter(alphaAdapter));
//        mRecyclerViewResult.setAdapter(mAppInfoAdapter);

        mAppInfoAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@androidx.annotation.NonNull BaseQuickAdapter<?, ?> adapter, @androidx.annotation.NonNull View view, int position) {

            }
        });

    }

    private void initHistoryRecycleView(List<String> list) {

        mHistoryAdapter = new SearchHistoryAdapter();
        mHistoryAdapter.addData(list);

        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this);
        //flexDirection 属性决定主轴的方向（即项目的排列方向）。类似 LinearLayout 的 vertical 和 horizontal。
        layoutManager.setFlexDirection(FlexDirection.ROW);//主轴为水平方向，起点在左端。
        //flexWrap 默认情况下 Flex 跟 LinearLayout 一样，都是不带换行排列的，但是flexWrap属性可以支持换行排列。
        layoutManager.setFlexWrap(FlexWrap.WRAP);//按正常方向换行
        //justifyContent 属性定义了项目在主轴上的对齐方式。
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);//交叉轴的起点对齐。
        mRecyclerViewHistory.setLayoutManager(layoutManager);


//        mRecyclerViewHistory.setAdapter(mHistoryAdapter);

        SlideInBottomAnimationAdapter alphaAdapter = new SlideInBottomAnimationAdapter(mHistoryAdapter);
        mRecyclerViewHistory.setAdapter(new ScaleInAnimationAdapter(alphaAdapter));

        mHistoryAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@androidx.annotation.NonNull BaseQuickAdapter<?, ?> adapter, @androidx.annotation.NonNull View view, int position) {
                mSearchTextView.setText(mHistoryAdapter.getItem(position));
                mSearchTextView.setSelection(mSearchTextView.length());
                search(mHistoryAdapter.getItem(position));
            }
        });


    }


    private void setupSearchView() {

        RxView.clicks(mActionClearBtn).subscribe(new Consumer<Object>() {

            @Override
            public void accept(@NonNull Object o) throws Exception {

                mSearchTextView.setText("");
                mPresenter.showHistory();
//                mLayoutHistory.setVisibility(View.VISIBLE);
//                mRecyclerViewSuggestion.setVisibility(View.GONE);
//                mRecyclerViewResult.setVisibility(View.GONE);
            }
        });


        RxTextView.editorActions(mSearchTextView).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(@NonNull Integer integer) throws Exception {

                search(mSearchTextView.getText().toString().trim());

            }
        });


        disposable = RxTextView.textChanges(mSearchTextView)
                .debounce(400, TimeUnit.MILLISECONDS)
                .compose(RxSchedulers.<CharSequence>io_main())
                .filter(new Predicate<CharSequence>() {
                    @Override
                    public boolean test(@NonNull CharSequence charSequence) throws Exception {
                        return charSequence.toString().trim().length() > 0;
                    }
                })

                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(@NonNull CharSequence charSequence) throws Exception {

                        Log.d("SearchActivity22", charSequence.toString() + "，status=" + status);

                        if (charSequence.length() > 0) {
                            mActionClearBtn.setVisibility(View.VISIBLE);
                        } else {
                            mActionClearBtn.setVisibility(View.GONE);
                        }

                        mPresenter.getSuggestions(charSequence.toString().trim());

                    }
                });
    }


    private void search(String keyword) {
        Log.d("hymmm", "search: " + keyword);
        mPresenter.search(keyword);
        mViewProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void showSearchHistory(List<String> list) {
        initHistoryRecycleView(list);
        setRecyclerViewSuggestionGone();
        setLayoutHistoryVisible();
        setRecyclerViewResultGone();
    }

    @Override
    public void showSuggestions(List<String> list) {

        mSuggestionAdapter.setNewData(list);
        setRecyclerViewSuggestionVisible();

        setLayoutHistoryGone();
        setRecyclerViewResultGone();

    }

    @Override
    public void showSearchResult(SearchResult result) {
        setRecyclerViewSuggestionGone();
        setLayoutHistoryGone();
        setRecyclerViewResultVisible();

        mAppInfoAdapter.addData(result.getListApp());
        mViewProgress.setVisibility(View.GONE);

        // 设置点击事件
        mAppInfoAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@androidx.annotation.NonNull BaseQuickAdapter adapter, @androidx.annotation.NonNull View view, int position) {
                AppInfoBean appInfoBean = (AppInfoBean)adapter.getItem(position);
                Intent intent = new Intent(SearchActivity.this, AppDetailsActivity3.class);
                intent.putExtra("appInfo",appInfoBean);
                SearchActivity.this.startActivity(intent);
            }
        });


    }





    private void setRecyclerViewResultGone() {
        Log.d("SearchActivityhym", "setRecyclerViewResultGone: ");
        mRecyclerViewResult.setVisibility(View.GONE);
    }

    private void setRecyclerViewResultVisible() {
        Log.d("SearchActivityhym", "setRecyclerViewResultVisible: ");
        mRecyclerViewResult.setVisibility(View.VISIBLE);
    }

    private void setRecyclerViewSuggestionGone() {
        Log.d("SearchActivityhym", "setRecyclerViewSuggestionGone: ");
        mRecyclerViewSuggestion.setVisibility(View.GONE);
    }

    private void setRecyclerViewSuggestionVisible() {
        Log.d("SearchActivityhym", "setRecyclerViewSuggestionVisible: ");
        mRecyclerViewSuggestion.setVisibility(View.VISIBLE);
    }

    private void setLayoutHistoryGone() {
        Log.d("SearchActivityhym", "setLayoutHistoryGone: ");
        mLayoutHistory.setVisibility(View.GONE);
    }

    private void setLayoutHistoryVisible() {
        Log.d("SearchActivityhym", "setLayoutHistoryVisible: ");
        mLayoutHistory.setVisibility(View.VISIBLE);
    }


}
