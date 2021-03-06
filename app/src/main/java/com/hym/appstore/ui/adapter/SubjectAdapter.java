package com.hym.appstore.ui.adapter;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.hym.appstore.R;
import com.hym.appstore.bean.Subject;
import com.hym.appstore.common.Constant;
import com.hym.appstore.common.imageloader.ImageLoader;

import org.jetbrains.annotations.NotNull;

public class SubjectAdapter extends BaseQuickAdapter<Subject, BaseViewHolder> implements LoadMoreModule {


    public SubjectAdapter() {
        super(R.layout.template_imageview);
    }


    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, Subject subject) {
        ImageLoader.load(Constant.BASE_IMG_URL +subject.getMticon(),baseViewHolder.getView(R.id.image_view));
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

//    @Override
//    public void onBindViewHolder(@NotNull BaseViewHolder holder, int position) {
//        holder.setIsRecyclable(false);
//        super.onBindViewHolder(holder, position);
//    }
}
