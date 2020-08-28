package com.hym.appstore.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.hym.appstore.R;
import com.hym.appstore.bean.AppInfoBean;
import com.hym.appstore.bean.BannerBean;
import com.hym.appstore.bean.HomeBean;
import com.hym.appstore.common.Constant;
import com.hym.appstore.common.imageloader.ImageLoadConfig;
import com.hym.appstore.common.imageloader.ImageLoader;
import com.hym.appstore.ui.activity.AppDetailsActivity;
import com.hym.appstore.ui.activity.AppDetailsActivity2;
import com.hym.appstore.ui.activity.AppDetailsActivity3;
import com.hym.appstore.ui.activity.HomeAppActivity;
import com.hym.appstore.ui.activity.SubjectActivity;
import com.hym.appstore.ui.widget.BannerLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import zlc.season.rxdownload2.RxDownload;

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private static final int TYPE_BANNER = 1;
    private static final int TYPE_ICON = 2;
    private static final int TYPE_APP = 3;
    private static final int TYPE_GAME = 4;



    private LayoutInflater mLayoutInflater;
    private HomeBean mHomeBean;

    private Context mContext;

    RxDownload mRxDownload;

    private AppInfoAdapter mAppInfoAdapter;



    public HomeAdapter(Context context, HomeBean homeBean,RxDownload rxDownload) {
        mLayoutInflater = LayoutInflater.from(context);
        this.mHomeBean = homeBean;
        this.mContext = context;
        this.mRxDownload = rxDownload;
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_BANNER;
        } else if (position == 1) {
            return TYPE_ICON;
        } else if (position == 2) {
            return TYPE_APP;
        } else if (position == 3) {
            return TYPE_GAME;
        }
        return 0;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_BANNER) {
            return new BannerViewHolder(mLayoutInflater.inflate(R.layout.home_banner, parent, false));
        } else if (viewType == TYPE_ICON) {
            return new IconViewHolder(mLayoutInflater.inflate(R.layout.home_nav_icon, parent, false));
        } else if (viewType == TYPE_APP) {
            return new AppViewHolder(mLayoutInflater.inflate(R.layout.home_recyclerview_with_title, null, false), TYPE_APP);
        } else if (viewType == TYPE_GAME) {
            return new AppViewHolder(mLayoutInflater.inflate(R.layout.home_recyclerview_with_title, null, false), TYPE_GAME);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//        Log.d("onBindViewHolder","第幾個item：" + position);
        if (position == 0) {
            BannerViewHolder bannerViewHolder = (BannerViewHolder) holder;
            List<BannerBean> banners = mHomeBean.getBanners();
            List<String> urls = new ArrayList<>(banners.size());
            for (BannerBean banner : banners) {
                urls.add(banner.getThumbnail());
            }
            bannerViewHolder.banner.setViewUrls(urls);
            bannerViewHolder.banner.setOnBannerItemClickListener(new BannerLayout.OnBannerItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    BannerBean bannerBean = banners.get(position);
                    switch (bannerBean.getAction()){

                        case "app":
                            Intent intent = new Intent();
                            intent.setClass(mContext, AppDetailsActivity.class);
                            intent.putExtra("isAnim",false);
                            AppInfoBean appInfoBean = new AppInfoBean();
                            appInfoBean.setPackageName(bannerBean.getId());
                            intent.putExtra("appInfo",appInfoBean);
                            mContext.startActivity(intent);
                            break;
                        case "subject":
                            Intent intent1 = new Intent();
                            intent1.setClass(mContext,SubjectActivity.class);
                            intent1.putExtra("subjectId",bannerBean.getId());
                            mContext.startActivity(intent1);
                            break;
                    }

                    Toast.makeText(mContext,"banner--onItemClick " + position, Toast.LENGTH_SHORT).show();

                }
            });
        } else if (position == 1) {
            IconViewHolder iconViewHolder = (IconViewHolder) holder;
            iconViewHolder.mIconHotApp.setOnClickListener(this);
            iconViewHolder.mIconHotGame.setOnClickListener(this);
            iconViewHolder.mIconHotRecommend.setOnClickListener(this);
        } else {
            AppViewHolder viewHolder = (AppViewHolder) holder;
            mAppInfoAdapter = AppInfoAdapter.builder().showPosition(false).showCategoryName(false).showBrief(true).showScore(true).rxDownload(mRxDownload).build();

            if (viewHolder.type == TYPE_APP){
                viewHolder.homeRecyclerviewTitle.setText("热门应用");

                List<AppInfoBean> homeApps = mHomeBean.getHomeApps();
                List<AppInfoBean> homeApps2 = new ArrayList<>();
                homeApps2=homeApps.subList(0,10);
                mAppInfoAdapter.addData(homeApps2);
            }else {
                viewHolder.homeRecyclerviewTitle.setText("热门游戏");
                mAppInfoAdapter.addData(mHomeBean.getHomeGames());
            }

            viewHolder.homeRecyclerview.setAdapter(mAppInfoAdapter);
            viewHolder.homeRecyclerview.setNestedScrollingEnabled(false);



            // 设置点击事件
            mAppInfoAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                    AppInfoBean appInfoBean = (AppInfoBean)adapter.getItem(position);
                    Intent intent = new Intent(mContext, AppDetailsActivity3.class);
                    intent.putExtra("appInfo",appInfoBean);
                    mContext.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.icon_hot_recommend:
                mContext.startActivity(new Intent(mContext, SubjectActivity.class));
                break;
            case R.id.icon_hot_app:
                Intent intent = new Intent(mContext, HomeAppActivity.class);
                intent.putExtra(Constant.HOME_LIST,Constant.APP_HOME_LIST);
                mContext.startActivity(intent);
                break;
            case R.id.icon_hot_game:
                Intent intent2 = new Intent(mContext, HomeAppActivity.class);
                intent2.putExtra(Constant.HOME_LIST,Constant.GAME_HOME_LIST);
                mContext.startActivity(intent2);
                break;
        }
    }


    class BannerViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.banner)
        BannerLayout banner;

        BannerViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            banner.setImageLoader(new ImgLoader());
        }
    }

    class IconViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.icon_hot_app)
        LinearLayout mIconHotApp;
        @BindView(R.id.icon_hot_game)
        LinearLayout mIconHotGame;
        @BindView(R.id.icon_hot_recommend)
        LinearLayout mIconHotRecommend;

        IconViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class AppViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.home_recyclerview_title)
        TextView homeRecyclerviewTitle;
        @BindView(R.id.home_recyclerview)
        RecyclerView homeRecyclerview;

        int type;

        AppViewHolder(@NonNull View itemView, int type) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.type = type;
            initRecyclerView();
            ButterKnife.bind(itemView);
        }

        private void initRecyclerView(){
            homeRecyclerview.setLayoutManager(new LinearLayoutManager(mContext){
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            });
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
//            dividerItemDecoration.setDrawable(ContextCompat.getDrawable(mContext,R.drawable.shape_question_diveder));
            homeRecyclerview.addItemDecoration(dividerItemDecoration);
        }
    }




    class ImgLoader implements BannerLayout.ImageLoader {

        @Override
        public void displayImage(Context context, String path, ImageView imageView) {
            ImageLoadConfig defConfig = new ImageLoadConfig.Builder()
                    .setCropType(ImageLoadConfig.CENTER_CROP)
                    .setAsBitmap(true)
                    .setPlaceHolderResId(R.drawable.vector_drawable_init_pic)
                    .setDiskCacheStrategy(ImageLoadConfig.DiskCache.ALL)
                    .setPrioriy(ImageLoadConfig.LoadPriority.HIGH)
                    .setCrossFade(true)
                    .build();
            ImageLoader.load(path, imageView,defConfig);
        }
    }

}
