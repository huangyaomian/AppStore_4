package com.hym.appstore.dagger2.module;

import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.rest.RequestQueue;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class QueueModule {

    @Provides
    @Singleton
    public RequestQueue provideRequestQueue(){
        return NoHttp.newRequestQueue();
    }

}
