package com.hym.appstore.ui.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hym.appstore.R;
import com.hym.appstore.bean.AppInfoBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecommendRVAdapter extends RecyclerView.Adapter<RecommendRVAdapter.MyViewHolder> {

    List<AppInfoBean> gameInfoList;
//    List<RecommendBean.DataBean.ItemsBean> gameInfoList;

    private Context mContext;

/*    public RecommendRVAdapter(List<RecommendBean.DataBean.ItemsBean> gameInfoList, Context context) {
        this.gameInfoList = gameInfoList;
        this.mContext = context;
    }*/
    public RecommendRVAdapter(List<AppInfoBean> gameInfoList, Context context) {
        this.gameInfoList = gameInfoList;
        this.mContext = context;
    }


    // 利用接口 -> 给RecyclerView设置点击事件
    private ItemClickListener mItemClickListener;

    public interface ItemClickListener {
        public void onItemClick(int position);
    }

    public void setOnItemClickListener(ItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recommend_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        /*RecommendBean.DataBean.ItemsBean itemsBean = gameInfoList.get(position);
        List<String> game_type = itemsBean.getGame_type();
        String gameType = "";
        if (game_type != null && !game_type.isEmpty()){
            if (game_type.size() > 1) {
                for (int i = 0; i < game_type.size(); i++) {
                        if (i == game_type.size()-1){
                            gameType = gameType + game_type.get(i);
                        }else{
                            gameType = gameType + game_type.get(i) + " | ";
                        }
                }
            }else {
                gameType = game_type.get(0);
            }
        }
        holder.mRecommendItemGameName.setText(itemsBean.getApp_name());
        holder.mRecommendItemGameTag.setText(gameType);
        DecimalFormat decimalFormat=new DecimalFormat(".0");//构造方法的字符格式这里如果小数不足1位,会以0补足.
        holder.mRecommendItemGameReview.setText(decimalFormat.format(itemsBean.getReviews().getScore()));
        holder.mRecommendItemIv.setImageURI(Uri.parse(itemsBean.getIcon_url()));*/

        AppInfoBean datasBean = gameInfoList.get(position);
        List<AppInfoBean.Tag> gameTags = datasBean.getAppTags();
        String gameTagsString = "";
        if (gameTags != null && !gameTags.isEmpty()){
            if (gameTags.size() > 1) {
                for (int i = 0; i < gameTags.size(); i++) {
                    if (i == gameTags.size()-1){
                        gameTagsString = gameTagsString + gameTags.get(i).getTagName();
                    }else{
                        gameTagsString = gameTagsString + gameTags.get(i).getTagName() + " | ";
                    }
                }
            }else {
                gameTagsString = gameTags.get(0).getTagName();
            }
        }
        holder.mRecommendItemGameName.setText(datasBean.getDisplayName());
        holder.mRecommendItemGameReview.setText(String.valueOf(datasBean.getRatingScore()));
        holder.mRecommendItemGameTag.setText(gameTagsString);
        String baseImgUrl = "http://file.market.xiaomi.com/mfc/thumbnail/png/w150q80/";
        holder.mRecommendItemIv.setImageURI(Uri.parse(baseImgUrl + datasBean.getIcon()));

        // 点击事件一般都写在绑定数据这里，当然写到上边的创建布局时候也是可以的
        if (mItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 这里利用回调来给RecyclerView设置点击事件
                    mItemClickListener.onItemClick(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return gameInfoList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.recommend_item_iv)
        SimpleDraweeView mRecommendItemIv;
        @BindView(R.id.recommend_item_game_name)
        TextView mRecommendItemGameName;
        @BindView(R.id.recommend_item_game_tag)
        TextView mRecommendItemGameTag;
        @BindView(R.id.recommend_item_game_review)
        TextView mRecommendItemGameReview;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
