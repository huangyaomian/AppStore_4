package com.hym.appstore.presenter.contract;

import com.hym.appstore.bean.AppInfoBean;
import com.hym.appstore.common.apkparset.AndroidApk;
import com.hym.appstore.ui.BaseView;

import java.util.List;

import io.reactivex.Observable;
import zlc.season.rxdownload2.RxDownload;
import zlc.season.rxdownload2.entity.DownloadRecord;

public interface AppManagerContract {

    public interface AppManagerView extends BaseView{
        void showDownloading(List<DownloadRecord> downloadRecords);
        void showApps(List<AndroidApk> apps);
        void showUpdateApps(List<AppInfoBean> appInfoBeans);
    }

    public interface IAppManagerModel{

        Observable<List<DownloadRecord>> getDownloadRecord();

        RxDownload getRxDownload();

        Observable<List<AndroidApk>> getLocalApks();

        Observable<List<AndroidApk>> getInstalledApps();

        Observable<Boolean> DelDownloadRecord(String url , boolean deleteFile);
    }
}
