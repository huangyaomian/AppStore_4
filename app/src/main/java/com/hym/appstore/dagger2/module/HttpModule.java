package com.hym.appstore.dagger2.module;

import android.app.Application;

import com.google.gson.Gson;
import com.hym.appstore.BuildConfig;
import com.hym.appstore.common.CommonParamsInterceptor;
import com.hym.appstore.common.rx.RxErrorHandler;
import com.hym.appstore.data.okhttp.ApiService;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class HttpModule {


    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient(Application application, Gson gson){

        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();

//        //这个日志需要去掉不然会有oom
//        if (BuildConfig.DEBUG) {
//            //log用拦截器
//            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
//            //开发模式记录整个boby，否则只记录基本信息如返回200，http协议版本等
//            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//
//            builder.addInterceptor(httpLoggingInterceptor);
//        }

        return builder
                //headinterceptor实现了interceptor，用来往request header 添加一些业务相关的数据，如app版本等，token信息
                .addInterceptor(new CommonParamsInterceptor(application,gson))
                .connectTimeout(10, TimeUnit.SECONDS)//连接超时时间
                .readTimeout(10,TimeUnit.SECONDS)//读取超时时间
                .build();

    }

    @Provides
    @Singleton
    public Retrofit provideRetrofit(OkHttpClient okHttpClient){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(ApiService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient);

        return builder.build();
    }

    @Provides
    @Singleton
    public ApiService provideApiService(Retrofit retrofit){
        return retrofit.create(ApiService.class);
    }

    @Provides
    @Singleton
    public RxErrorHandler provideErrorHandler(Application application){
        return new RxErrorHandler(application);
    }
}
