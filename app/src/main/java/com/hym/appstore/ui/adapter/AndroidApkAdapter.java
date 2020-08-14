package com.hym.appstore.ui.adapter;

import android.content.Context;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.hym.appstore.R;
import com.hym.appstore.common.apkparset.AndroidApk;
import com.hym.appstore.common.rx.RxSchedulers;
import com.hym.appstore.common.utils.AppUtils;
import com.hym.appstore.common.utils.FileUtils;
import com.hym.appstore.ui.widget.DownloadProgressButton;
import com.jakewharton.rxbinding2.view.RxView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class AndroidApkAdapter extends BaseQuickAdapter<AndroidApk, BaseViewHolder> {

    public static final int FLAG_APK = 0;
    public static final int FLAG_APP = 1;

    private int flag;
    private Context mContext;

    public AndroidApkAdapter(Context context, int flag ) {
        super(R.layout.template_app_downloading);
        this.flag = flag;
        this.mContext = context;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, AndroidApk item) {
        helper.setText(R.id.txt_app_name, item.getAppName());

        helper.setImageDrawable(R.id.img_app_icon, item.getDrawable());

        final DownloadProgressButton btn = helper.getView(R.id.btn_download);
        final TextView txtStatus = helper.getView(R.id.txt_status);


        if (flag == FLAG_APK) {

            btn.setTag(R.id.tag_package_name, item.getPackageName());
            btn.setText("删除");

            RxView.clicks(btn).subscribe(new Consumer<Object>() {

                @Override
                public void accept(@NonNull Object o) throws Exception {

                    if (btn.getTag(R.id.tag_package_name).toString().equals(item.getPackageName())) {

                        Object obj = btn.getTag();

                        if (obj == null) {

                            AppUtils.installApk(mContext, item.getApkPath());

                        } else {

                            boolean isInstall = (boolean) obj;
                            if (isInstall) {
                                deleteApk(item);
                            } else {
                                AppUtils.installApk(mContext, item.getApkPath());
                            }
                        }
                    }

                }
            });

            isInstalled(mContext, item.getPackageName()).subscribe(new Consumer<Boolean>() {

                @Override
                public void accept(@NonNull Boolean aBoolean) throws Exception {
                    btn.setTag(aBoolean);

                    if (aBoolean) {
                        txtStatus.setText("已安装");
                        btn.setText("删除");
                    } else {
                        txtStatus.setText("等待安装");
                        btn.setText("安装");
                    }
                }


            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    throwable.printStackTrace();
                }
            });

        } else if (flag == FLAG_APP) {

            btn.setText("卸载");
            RxView.clicks(btn).subscribe(new Consumer<Object>() {

                @Override
                public void accept(@NonNull Object o) throws Exception {
                    //点击后卸载,卸载完成刷新列表
                    /*if (AppUtils.uninstallApk(mContext, item.getPackageName())){
                        getData().remove(helper.getPosition());
                        notifyItemRemoved(helper.getPosition());
                    }*/
                    AppUtils.uninstallApk(mContext, item.getPackageName());
                }
            });


            txtStatus.setText("v" + item.getAppVersionName() + " " + (item.isSystem() ? "内置" : "第三方")); // size 加进来
        }
    }




    private void deleteApk(AndroidApk item){

        // 1. 删除下载记录


        // 2. 删除文件
        List<AndroidApk> list = this.getData();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            AndroidApk androidApk = list.get(i);
            if (item.getPackageName().equals(androidApk.getPackageName())) {
                this.removeAt(i);
                break;
            }
        }

        FileUtils.deleteFile(item.getApkPath());


    }


    public Observable<Boolean> isInstalled(final Context context, final String packageName){


        return   Observable.create(new ObservableOnSubscribe<Boolean>() {


            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {

                e.onNext( AppUtils.isInstalled(context,packageName));

            }


        }).compose(RxSchedulers.<Boolean>io_main());


    }
}
