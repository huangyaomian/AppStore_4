package com.hym.appstore.dagger2.component;

import android.app.Application;

import com.hym.appstore.common.rx.RxErrorHandler;
import com.hym.appstore.dagger2.module.AppModule;
import com.hym.appstore.dagger2.module.DownloadModule;
import com.hym.appstore.dagger2.module.HttpModule;
import com.hym.appstore.dagger2.module.QueueModule;
import com.hym.appstore.data.okhttp.ApiService;
import com.yanzhenjie.nohttp.rest.RequestQueue;

import javax.inject.Singleton;

import dagger.Component;
import zlc.season.rxdownload2.RxDownload;


@Singleton
@Component(modules = {AppModule.class, QueueModule.class, HttpModule.class, DownloadModule.class})
public interface AppComponent {
    public RequestQueue getQueue();

    public ApiService getApiService();

    public Application getApplication();

    public RxErrorHandler getRxErrorHandler();

    public RxDownload getRxDownload();
}
