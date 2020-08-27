package com.hym.appstore.ui.adapter;



import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.hym.appstore.R;
import com.hym.appstore.common.imageloader.ImageLoader;

import org.jetbrains.annotations.NotNull;

public class screenShortAdapter extends BaseQuickAdapter<String, BaseViewHolder> implements LoadMoreModule {

    public screenShortAdapter() {
        super(R.layout.template_imageview_vertical);
    }


    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, String url) {

        ImageLoader.load(url,baseViewHolder.getView(R.id.image_view));
    }

    @Override
    public int getItemCount() {
        return getData().size();
    }




}
