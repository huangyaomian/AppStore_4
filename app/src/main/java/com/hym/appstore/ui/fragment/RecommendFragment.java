package com.hym.appstore.ui.fragment;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hjq.toast.ToastUtils;
import com.hym.appstore.R;
import com.hym.appstore.bean.AppInfoBean;
import com.hym.appstore.bean.RecommendBean2;
import com.hym.appstore.dagger2.component.AppComponent;
import com.hym.appstore.dagger2.component.DaggerRecommendComponent;
import com.hym.appstore.dagger2.module.RecommendModule;
import com.hym.appstore.presenter.RecommendPresenter;
import com.hym.appstore.presenter.contract.RecommendContract;
import com.hym.appstore.ui.adapter.RecommendRVAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;


public class RecommendFragment extends ProgressFragment<RecommendPresenter> implements RecommendContract.View {


    @BindView(R.id.recommend_rv)
    RecyclerView mRecommendRv;
    @BindView(R.id.recommend_refreshLayout)
    RefreshLayout recommendRefreshLayout;

    /**
     * 自定义的容器
     **/
    private List<AppInfoBean> mGameList;

    private RecommendRVAdapter mRecommendRVAdapter;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerRecommendComponent.builder().appComponent(appComponent).recommendModule(new RecommendModule(this)).build().inject(this);
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_recommend;
    }

    @Override
    protected void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecommendRv.setLayoutManager(layoutManager);
    }


    @Override
    protected void init() {
        mGameList = new ArrayList<>();
        mPresenter.requestRecommendData(true);
    }

    @Override
    protected void initEvent() {
        recommendRefreshLayout.setRefreshHeader(new ClassicsHeader(getActivity()));//设置Header
        recommendRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mPresenter.requestRecommendData(false);
            }
        });

        /*先注釋掉這裏暫時不知道怎麽翻頁
        recommendRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {

            }
        });*/

    }




    /**
     * 定义RecyclerView选项单击事件的回调接口
     */
    public interface OnItemClickListener {
        //也可以不在这个activity或者是fragment中来声明接口，可以在项目中单独创建一个interface，就改成static就OK
        //参数（父组件，当前单击的View,单击的View的位置，数据）
        void onItemClick(RecyclerView parent, View view, int position, Map data);
        // void onItemLongClick(View view);类似，我这里没用就不写了
        //
        //这个data是List中放的数据类型，因为我这里是private List<Map> mapList;这样一个
        //然后我的每个item是这样的：
        //        HashMap map =new HashMap();
        //        map.put("img",R.drawable.delete);
        //        map.put("text","x1");
        //所以我的是map类型的，那如果是item中只有text的话比如List<String>，那么data就改成String类型
    }

    @Override
    public void showResult(List<AppInfoBean> recommendBean) {
       recommendRefreshLayout.finishRefresh();//结束刷新
        mGameList.clear();
        Log.d("showResult","開始showResult");
        mGameList.addAll(recommendBean);
        if (mRecommendRVAdapter == null){
            mRecommendRVAdapter = new RecommendRVAdapter(mGameList, getActivity());
            mRecommendRv.setAdapter(mRecommendRVAdapter);
            // 设置数据后就要给RecyclerView设置点击事件
            mRecommendRVAdapter.setOnItemClickListener(new RecommendRVAdapter.ItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    // 这里本来是跳转页面 ，我们就在这里直接让其弹toast来演示
                    ToastUtils.show(mGameList.get(position).getDisplayName());
                }
            });
        }else {
            mRecommendRVAdapter.notifyDataSetChanged();
        }



    }


    @Override
    public void showMoreResult(RecommendBean2 recommendBean) {
      /*  recommendRefreshLayout.finishLoadMore(2000*//*,false*//*);//传入false表示加载失败
        List<RecommendBean.DataBean.ItemsBean> items = recommendBean.getData().getItems();
        recommendNextURL = recommendBean.getData().getPager().getNext();
        mGameList.addAll(items);
        mRecommendRVAdapter.notifyDataSetChanged();*/
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


}
