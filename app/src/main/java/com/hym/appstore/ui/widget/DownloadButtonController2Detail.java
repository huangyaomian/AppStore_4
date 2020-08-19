package com.hym.appstore.ui.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hym.appstore.R;
import com.hym.appstore.bean.AppDownloadInfo;
import com.hym.appstore.bean.AppInfoBean;
import com.hym.appstore.bean.BaseBean;
import com.hym.appstore.common.Constant;
import com.hym.appstore.common.rx.Optional;
import com.hym.appstore.common.rx.RxHttpResponseCompat;
import com.hym.appstore.common.rx.RxSchedulers;
import com.hym.appstore.common.rx.subscriber.ErrorHandlerDisposableObserver;
import com.hym.appstore.common.utils.ACache;
import com.hym.appstore.common.utils.AppUtils;
import com.hym.appstore.common.utils.PermissionUtil;
import com.jakewharton.rxbinding2.view.RxView;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import retrofit2.http.GET;
import retrofit2.http.Path;
import zlc.season.rxdownload2.RxDownload;
import zlc.season.rxdownload2.entity.DownloadBean;
import zlc.season.rxdownload2.entity.DownloadEvent;
import zlc.season.rxdownload2.entity.DownloadFlag;
import zlc.season.rxdownload2.entity.DownloadRecord;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class DownloadButtonController2Detail {
    private RxDownload mRxDownload;
    private Api mApi;

    private FlagChangeListener mFlagChangeListener;

    public interface FlagChangeListener {
        void getFlagChange();
    }

    public void setFlagChangeListener(FlagChangeListener listener){
        this.mFlagChangeListener = listener;
    }


    public DownloadButtonController2Detail(RxDownload rxDownload) {
        mRxDownload = rxDownload;
        if (mRxDownload != null) {
            mApi = mRxDownload.getRetrofit().create(Api.class);
        }

    }

    public void handClick(final DownloadProgressButton2Detail btn, DownloadRecord record) {
        AppInfoBean info = downloadRecord2AppInfo(record);
        receiveDownloadStatus(record.getUrl()).subscribe(new DownloadConsumer(btn, info));

    }

    /**
     * 根据AppInfo的信息去做状态的处理
     *
     * @param appInfo
     */
    public void handClick(final DownloadProgressButton2Detail btn, final AppInfoBean appInfo) {

        if (mApi == null) {
            return;
        }

        isAppInstalled(btn.getContext(), appInfo)
                .flatMap(new Function<DownloadEvent, ObservableSource<DownloadEvent>>() {
                    @Override
                    public ObservableSource<DownloadEvent> apply(@NonNull DownloadEvent event) throws Exception {

                        if (DownloadFlag.INSTALLED == event.getFlag()) {
                            return isUpdate(btn.getContext(), appInfo);
                        }

                        return Observable.just(event);
                    }
                })
                .flatMap(new Function<DownloadEvent, ObservableSource<DownloadEvent>>() {
                    @Override
                    public ObservableSource<DownloadEvent> apply(@NonNull DownloadEvent event)
                            throws Exception {

                        if (DownloadFlag.UN_INSTALL == event.getFlag()) {
                            return isApkFileExist(btn.getContext(), appInfo);
                        }else if (DownloadFlag.UPDATE == event.getFlag()){
                            return isApkFileExist2Update(btn.getContext(), appInfo);
                        }

                        return Observable.just(event);

                    }
                })
                .flatMap(new Function<DownloadEvent, ObservableSource<DownloadEvent>>() {
                    @Override
                    public ObservableSource<DownloadEvent> apply(@NonNull DownloadEvent event) throws Exception {

                        if (appInfo.getDisplayName().contains("快手")){

                        }

                        Log.d("hymmm", "apply: " + appInfo.getDisplayName() + "---" + event.getFlag());

                        if (DownloadFlag.FILE_EXIST == event.getFlag()) {

                            return getAppDownloadInfo(appInfo)
                                    .flatMap(new Function<Optional<AppDownloadInfo>, ObservableSource<DownloadEvent>>() {
                                        @Override
                                        public ObservableSource<DownloadEvent> apply(Optional<AppDownloadInfo> appDownloadInfoOptional) throws Exception {

                                            appInfo.setAppDownloadInfo(appDownloadInfoOptional.getIncludeNull());

                                            return receiveDownloadStatus(appDownloadInfoOptional.getIncludeNull().getDownloadUrl());
                                        }
                                    });


                        }

                        return Observable.just(event);
                    }
                })
                .compose(RxSchedulers.<DownloadEvent>io_main())
                .subscribe(new DownloadConsumer(btn, appInfo));



    }

    /**
     * 点击按钮的状态绑定
     *
     * @param btn
     */
    private void bindClick(final DownloadProgressButton2Detail btn, final AppInfoBean appInfo) {

        RxView.clicks(btn).subscribe(new Consumer<Object>() {

            @Override
            public void accept(@NonNull Object o) throws Exception {

                int flag = (int) btn.getTag(R.id.tag_apk_flag);

                Log.d("hymmm","bindClick:flag=" + flag);

                switch (flag) {

                    case DownloadFlag.INSTALLED:
                        runApp(btn.getContext(), appInfo.getPackageName());
                        break;

                    case DownloadFlag.STARTED:
                        pausedDownload(appInfo.getAppDownloadInfo().getDownloadUrl());
                        break;

                    case DownloadFlag.FAILED:
                    case DownloadFlag.NORMAL:
                    case DownloadFlag.WAITING:
                    case DownloadFlag.PAUSED:
                    case DownloadFlag.UPDATE:// 升级 还加上去
                        startDownload(btn, appInfo);
                        break;

                    case DownloadFlag.COMPLETED:
                        installApp(btn.getContext(), appInfo);
                        break;

                }


            }
        });
    }

    //    安装app
    private void installApp(Context context, AppInfoBean appInfoBean) throws FileNotFoundException {
        String path = ACache.get(context).getAsString(Constant.APK_DOWNLOAD_DIR) + File.separator + appInfoBean.getReleaseKeyHash();
        Log.d("installApp",path);
        AppUtils.installApk(context, path);
    }

    //    开启下载
    public void startDownload(final DownloadProgressButton2Detail btn, final AppInfoBean appInfoBean){
        PermissionUtil.requestPermission(btn.getContext(),WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) {
                        if (aBoolean) {
                            final AppDownloadInfo downloadInfo = appInfoBean.getAppDownloadInfo();
                            if (downloadInfo == null) {
                                getAppDownloadInfo(appInfoBean)
                                        .subscribe(new ErrorHandlerDisposableObserver<Optional<AppDownloadInfo>>(btn.getContext()) {
                                            @Override
                                            public void onSubscribe(Disposable d) {

                                            }

                                            @Override
                                            public void onNext(Optional<AppDownloadInfo> appDownloadInfoOptional) {

                                                appInfoBean.setAppDownloadInfo(appDownloadInfoOptional.getIncludeNull());
                                                download(btn,appInfoBean);

                                            }

                                            @Override
                                            public void onComplete() {

                                            }
                                        });

                            }else {
                                download(btn, appInfoBean);
                            }
                        }
                    }
                });
    }

    // 重新下載
    public void ReDownload(final DownloadProgressButton2Detail btn, final AppInfoBean appInfoBean){

        PermissionUtil.requestPermission(btn.getContext(),WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) {
                        if (aBoolean) {
                            mRxDownload.deleteServiceDownload(appInfoBean.getAppDownloadInfo().getDownloadUrl(), true).subscribe(new Consumer<Object>() {
                                @Override
                                public void accept(Object o) throws Exception {
                                    download(btn, appInfoBean);
                                }
                            });
                        }
                    }
                });



    }

    private void download(DownloadProgressButton2Detail btn, AppInfoBean appInfoBean) {
        mRxDownload.serviceDownload(appInfo2DownloadBean(appInfoBean)).subscribe();
        mRxDownload.receiveDownloadStatus(appInfoBean.getAppDownloadInfo().getDownloadUrl())
                .subscribe(new DownloadConsumer(btn, appInfoBean));
        Log.d("hymmm", "download: " +appInfoBean.getDisplayName()+ "--" + appInfoBean.getAppDownloadInfo().getDownloadUrl());
    }

    private DownloadBean appInfo2DownloadBean(AppInfoBean appInfoBean) {

        DownloadBean downloadBean = new DownloadBean();

        downloadBean.setUrl(appInfoBean.getAppDownloadInfo().getDownloadUrl());
        downloadBean.setSaveName(appInfoBean.getReleaseKeyHash() + ".apk");

        downloadBean.setExtra1(appInfoBean.getId() + "");
        downloadBean.setExtra2(appInfoBean.getIcon());
        downloadBean.setExtra3(appInfoBean.getDisplayName());
        downloadBean.setExtra4(appInfoBean.getPackageName());
        downloadBean.setExtra5(appInfoBean.getReleaseKeyHash());

        return downloadBean;
    }

    public AppInfoBean downloadRecord2AppInfo(DownloadRecord bean) {

        AppInfoBean info = new AppInfoBean();
        info.setId(Integer.parseInt(bean.getExtra1()));
        info.setIcon(bean.getExtra2());
        info.setDisplayName(bean.getExtra3());
        info.setPackageName(bean.getExtra4());
        info.setReleaseKeyHash(bean.getExtra5());


        AppDownloadInfo downloadInfo = new AppDownloadInfo();

        downloadInfo.setDowanloadUrl(bean.getUrl());

        info.setAppDownloadInfo(downloadInfo);

        return info;


    }

    //暂停下载
    private void pausedDownload(String url) {
        mRxDownload.pauseServiceDownload(url).subscribe();
    }

    //开启app
    private void runApp(Context context, String packageName) {
        String path = ACache.get(context).getAsString(Constant.APK_DOWNLOAD_DIR) + File.separator;
        Log.d("installApp",path);
        AppUtils.runApp(context, packageName);
    }


    public Observable<DownloadEvent> isAppInstalled(Context context, AppInfoBean appInfo) {


        DownloadEvent event = new DownloadEvent();

        event.setFlag(AppUtils.isInstalled(context, appInfo.getPackageName()) ? DownloadFlag.INSTALLED : DownloadFlag.UN_INSTALL);

        return Observable.just(event);

    }


    public Observable<DownloadEvent> isApkFileExist(Context context, AppInfoBean appInfo) {
        //下载完成的文件是有apk后缀的
        String path = ACache.get(context).getAsString(Constant.APK_DOWNLOAD_DIR) + File.separator + appInfo.getReleaseKeyHash()+".apk";
        File file = new File(path);
        DownloadEvent event = new DownloadEvent();
        event.setFlag(file.exists() ? DownloadFlag.FILE_EXIST : DownloadFlag.NORMAL);
        return Observable.just(event);

    }

    public Observable<DownloadEvent> isApkFileExist2Update(Context context, AppInfoBean appInfo) {
        //下载完成的文件是有apk后缀的
        String path = ACache.get(context).getAsString(Constant.APK_DOWNLOAD_DIR) + File.separator + appInfo.getReleaseKeyHash()+".apk";
        File file = new File(path);
        DownloadEvent event = new DownloadEvent();
        event.setFlag(file.exists() ? DownloadFlag.FILE_EXIST : DownloadFlag.UPDATE);
        return Observable.just(event);

    }


    //是否需要更新
    public Observable<DownloadEvent> isUpdate(Context context, AppInfoBean appInfo) {
        DownloadEvent event = new DownloadEvent();

        String json= ACache.get(context).getAsString(Constant.APP_UPDATE_LIST);

        if(!TextUtils.isEmpty(json)){
            Gson gson = new Gson();
            List<AppInfoBean> apps = gson.fromJson(json,new TypeToken<List<AppInfoBean>>(){}.getType());

            List<String> packageNames = new ArrayList<>();

            for (int i = 0; i < apps.size(); i++) {
                packageNames.add(apps.get(i).getPackageName());
            }

            event.setFlag(packageNames.contains(appInfo.getPackageName()) ? DownloadFlag.UPDATE : DownloadFlag.INSTALLED);

        }

        return Observable.just(event);

    }


    public Observable<DownloadEvent> receiveDownloadStatus(String url) {
        return mRxDownload.receiveDownloadStatus(url);
    }


    public Observable<Optional<AppDownloadInfo>> getAppDownloadInfo(AppInfoBean appInfoBean) {
        return mApi.getAppDownloadInfo(appInfoBean.getId()).compose(RxHttpResponseCompat.handle_result());
//        return mApi.getAppDownloadInfo(appInfoBean.getId()).compose(RxHttpResponseCompat.<AppDownloadInfo>compatResult());
    }





    class DownloadConsumer implements Consumer<DownloadEvent> {
        DownloadProgressButton2Detail btn;
        AppInfoBean mAppInfo;

        public DownloadConsumer(DownloadProgressButton2Detail b, AppInfoBean appInfo) {
            this.btn = b;
            this.mAppInfo = appInfo;
        }

        @Override
        public void accept(@NonNull DownloadEvent event) throws Exception {
            Integer flag = 0;
            flag = event.getFlag();
            btn.setTag(R.id.tag_apk_flag, flag);
            mFlagChangeListener.getFlagChange();

            Log.d("hymmm","DownloadConsumer:accept=" + mAppInfo.getDisplayName() + flag);

            bindClick(btn, mAppInfo);

            switch (flag) {
                case DownloadFlag.INSTALLED:
                    btn.setCurrentText("运行");
                    break;

                case DownloadFlag.NORMAL:
                    btn.setState(DownloadProgressButton2Detail.STATE_NORMAL);
                    btn.setCurrentText("開始下載" + "(" + mAppInfo.getApkSize() / 1024 / 1024 + "MB" + ")");
                    break;

                case DownloadFlag.STARTED:
                case DownloadFlag.WAITING: //等待中
                    btn.setState(DownloadProgressButton2Detail.STATE_DOWNLOADING);
                    btn.setCurrentText("下載中" + (int) event.getDownloadStatus().getPercentNumber() + "%");
                    btn.setProgress((int) event.getDownloadStatus().getPercentNumber());
//                    Log.d("hymmm","getPercentNumber =" + event.getDownloadStatus().getPercentNumber());
                    break;

                case DownloadFlag.PAUSED:
                    btn.setState(DownloadProgressButton2Detail.STATE_PAUSE);
                    btn.setCurrentText("繼續" + (int) event.getDownloadStatus().getPercentNumber() + "%");
                    btn.setProgress((int) event.getDownloadStatus().getPercentNumber());
                    break;

                case DownloadFlag.COMPLETED: //已完成
                    btn.setState(DownloadProgressButton2Detail.STATE_FINISH);
                    btn.setCurrentText("安装");
                    //installApp(btn.getContext(),mAppInfo);
                    break;

                case DownloadFlag.FAILED://下载失败
                    btn.setCurrentText("失败");
                    break;

                case DownloadFlag.DELETED: //已删除
                    break;

                case DownloadFlag.UPDATE: //已删除
                    btn.setCurrentText("更新");
                    break;
            }
        }
    }


    interface Api {
        @GET("download/{id}")
        Observable<BaseBean<AppDownloadInfo>> getAppDownloadInfo(@Path("id") int id);
    }
}
