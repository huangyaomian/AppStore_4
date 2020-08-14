package com.hym.appstore.data;

import android.content.Context;

import com.hym.appstore.common.Constant;
import com.hym.appstore.common.apkparset.AndroidApk;
import com.hym.appstore.common.utils.ACache;
import com.hym.appstore.common.utils.AppUtils;
import com.hym.appstore.common.utils.FileUtils;
import com.hym.appstore.presenter.contract.AppManagerContract;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import zlc.season.rxdownload2.RxDownload;
import zlc.season.rxdownload2.entity.DownloadRecord;

public class AppManagerModel implements AppManagerContract.IAppManagerModel {

    RxDownload mRxDownload;
    Context mContext;

    public AppManagerModel(Context context, RxDownload mRxDownload) {
        this.mRxDownload = mRxDownload;
        this.mContext = context;
    }

    @Override
    public Observable<List<DownloadRecord>> getDownloadRecord() {
        return mRxDownload.getTotalDownloadRecords();
    }



    @Override
    public RxDownload getRxDownload() {
        return mRxDownload;
    }

    @Override
    public Observable<List<AndroidApk>> getLocalApks() {

        String dir = ACache.get(mContext).getAsString(Constant.APK_DOWNLOAD_DIR);

        return Observable.create(new ObservableOnSubscribe<List<AndroidApk>>() {
            @Override
            public void subscribe(ObservableEmitter<List<AndroidApk>> emitter) throws Exception {
                emitter.onNext(scanApks(dir));
                emitter.onComplete();
            }
        });
    }

    @Override
    public Observable<List<AndroidApk>> getInstalledApps() {
        return Observable.create(new ObservableOnSubscribe<List<AndroidApk>>() {
            @Override
            public void subscribe(ObservableEmitter<List<AndroidApk>> AndroidApks) throws Exception {
                AndroidApks.onNext(AppUtils.getInstalledApps(mContext));
                AndroidApks.onComplete();
            }
        });
    }

    @Override
    public Observable<Boolean> DelDownloadRecord(String url, boolean deleteFile) {
        return (Observable<Boolean>) mRxDownload.deleteServiceDownload(url,deleteFile);
    }

    private List<AndroidApk> scanApks(String dir){
        File file = new File(dir);
        if (!file.isDirectory()) {
            throw new RuntimeException("is not dir");
        }

        File[] apks = file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if (pathname.isDirectory()) {
                    return false;
                }
                return pathname.getName().endsWith("apk");
            }
        });

        List<AndroidApk> androidApks = new ArrayList<>();

        for (File apk: apks){
            AndroidApk androidApk = AndroidApk.read(mContext,apk.getPath());
            if (androidApk != null){
                androidApks.add(androidApk);
            }

        }

        return androidApks;

    }
}
