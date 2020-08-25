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

import java.util.List;

import zlc.season.rxdownload2.RxDownload;
import zlc.season.rxdownload2.entity.DownloadRecord;

public class DownloadingAdapter extends BaseQuickAdapter<DownloadRecord, BaseViewHolder> implements LoadMoreModule {

    String baseImgUrl = "http://file.market.xiaomi.com/mfc/thumbnail/png/w150q80/";
    private DownloadButtonController mDownloadButtonController;

    public DownloadingAdapter(RxDownload rxDownload) {
        super(R.layout.template_app_downloading);
        mDownloadButtonController = new DownloadButtonController(rxDownload);

    }



    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, DownloadRecord downloadRecord) {

        AppInfoBean appInfoBean = mDownloadButtonController.downloadRecord2AppInfo(downloadRecord);


        ImageLoader.load(baseImgUrl+appInfoBean.getIcon(),baseViewHolder.getView(R.id.img_app_icon));

        baseViewHolder.setText(R.id.txt_app_name,appInfoBean.getDisplayName());

        View btnView = baseViewHolder.getView(R.id.btn_download);

        if (btnView instanceof DownloadProgressButton) {
            DownloadProgressButton btn = (DownloadProgressButton) btnView;
            mDownloadButtonController.handClick(btn,downloadRecord);
        }

    }

    @Override
    public void onBindViewHolder(@NotNull BaseViewHolder holder, int position, @NotNull List<Object> payloads) {
        if (payloads.isEmpty()){
            super.onBindViewHolder(holder,position,payloads);
        }else {
            String type= (String) payloads.get(0);// 刷新哪个部分 标志位
            switch(type){
                case "download":
                    View viewBtn  = holder.getView(R.id.btn_download);
                    if (viewBtn instanceof  DownloadProgressButton){
                        DownloadProgressButton btn = (DownloadProgressButton) viewBtn;
                        mDownloadButtonController.handClick(btn,mDownloadButtonController.downloadRecord2AppInfo(getData().get(position)));
                    }
                    break;

            }

        }

    }





}
