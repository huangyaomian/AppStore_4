package com.hym.appstore.ui.adapter;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.hym.appstore.R;
import com.hym.appstore.bean.AppInfoBean;
import com.hym.appstore.common.imageloader.ImageLoader;
import com.hym.appstore.ui.widget.DownloadButtonController;
import com.hym.appstore.ui.widget.DownloadProgressButton;

import org.jetbrains.annotations.NotNull;

import zlc.season.rxdownload2.RxDownload;

public class AppInfo2HomeAdapter extends BaseQuickAdapter<AppInfoBean, BaseViewHolder> implements LoadMoreModule {

    String baseImgUrl = "http://file.market.xiaomi.com/mfc/thumbnail/png/w150q80/";
    private DownloadButtonController mDownloadButtonController;
    private RxDownload mRxDownload;

    public AppInfo2HomeAdapter(RxDownload rxDownload,int layoutId) {
        super(layoutId);
        mDownloadButtonController = new DownloadButtonController(mRxDownload);
    }



    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, AppInfoBean appInfoBean) {

        ImageLoader.load(baseImgUrl+appInfoBean.getIcon(),baseViewHolder.getView(R.id.img_app_icon));
        View viewBtn  = baseViewHolder.getView(R.id.btn_download);

        baseViewHolder.setText(R.id.home_recyclerview_name,appInfoBean.getDisplayName());

        TextView txtViewBrief = baseViewHolder.getView(R.id.home_recyclerview_brief);
        txtViewBrief.setVisibility(View.GONE);
        txtViewBrief.setText(appInfoBean.getBriefShow());


        if (viewBtn instanceof  DownloadProgressButton){
            DownloadProgressButton btn = (DownloadProgressButton) viewBtn;
            mDownloadButtonController.handClick(btn,appInfoBean);
        }

    }





}
