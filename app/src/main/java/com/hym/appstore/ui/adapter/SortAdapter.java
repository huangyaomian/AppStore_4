package com.hym.appstore.ui.adapter;



import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.hym.appstore.R;
import com.hym.appstore.bean.SortBean;
import com.hym.appstore.common.imageloader.ImageLoader;

import org.jetbrains.annotations.NotNull;

public class SortAdapter extends BaseQuickAdapter<SortBean, BaseViewHolder> implements LoadMoreModule {

    String baseImgUrl = "http://file.market.xiaomi.com/mfc/thumbnail/png/w150q80/";

    public SortAdapter() {
        super(R.layout.sort_recyclerview_item);
    }


    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, SortBean sortBean) {
        ImageLoader.load(baseImgUrl+sortBean.getIcon(),baseViewHolder.getView(R.id.sort_item_iv));
        baseViewHolder.setText(R.id.sort_recyclerview_name,sortBean.getName());
    }




}
