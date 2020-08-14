package com.hym.appstore.presenter;

import com.hym.appstore.bean.SearchResult;
import com.hym.appstore.common.db.DBManager;
import com.hym.appstore.common.rx.Optional;
import com.hym.appstore.common.rx.RxHttpResponseCompat;
import com.hym.appstore.common.rx.subscriber.ProgressDisposableObserver;
import com.hym.appstore.presenter.contract.SearchContract;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;


public class SearchPresenter extends BasePresenter<SearchContract.ISearchModel, SearchContract.SearchView> {


    @Inject
    public SearchPresenter(SearchContract.ISearchModel iSearchModel, SearchContract.SearchView searchView) {
        super(iSearchModel, searchView);
    }



    public void getSuggestions(String keyword){

        mModel.getSuggestion(keyword)
                .compose(RxHttpResponseCompat.<List<String>>compatResult())
                .subscribe(new ProgressDisposableObserver<List<String>>(mContext,mView) {
                    @Override
                    public void onNext(List<String> suggestions) {

                        mView.showSuggestions(suggestions);
                    }
                });

    }



    public void search(String keyword){

        saveSearchHistory(keyword);

        mModel.search(keyword)
                .compose(RxHttpResponseCompat.handle_result())
                .subscribe(new ProgressDisposableObserver<Optional<SearchResult>>(mContext,mView) {
                    @Override
                    public void onNext(@NonNull Optional<SearchResult> searchResultOptional) {
                        mView.showSearchResult(searchResultOptional.getIncludeNull());

                    }
                });

    }

//      mModel.getHomeRequest()
//              .compose(RxHttpResponseCompat.handle_result())
//            .subscribe(new ProgressDisposableObserver<Optional<HomeBean>>(mContext, mView) {
//        @Override
//        public void onNext(@NonNull Optional< HomeBean > homeBeanOptional) {
//            Log.d("requestHomeData", String.valueOf(homeBeanOptional.getIncludeNull().getBanners().size()));
//            mView.showResult(homeBeanOptional.getIncludeNull());
//
//        }
//        @Override
//        protected boolean isShowProgress() {
//            return isShowProgress;
//        }
//    });

    private void saveSearchHistory(String keyword) {

        DBManager.insertSearchHistory(keyword);
    }


    public void showHistory(){

        // get search history from  database



        /*List<String> list = new ArrayList<>();
        list = DBManager.getAllSearchHistoryList();
        list.add("地图");
        list.add("KK");
        list.add("爱奇艺");
        list.add("播放器");
        list.add("支付宝");
        list.add("微信");
        list.add("QQ");
        list.add("TV");
        list.add("直播");
        list.add("妹子");
        list.add("美女");*/
        List<String> allSearchHistoryList = DBManager.getAllSearchHistoryList();
        if (allSearchHistoryList.size()> 0) {
            mView.showSearchHistory(allSearchHistoryList);
        }

    }

}
