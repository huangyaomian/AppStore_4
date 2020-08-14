package com.hym.appstore.dagger2.module;

import android.app.Application;
import android.content.Context;

import com.google.gson.Gson;
import com.hym.appstore.ui.widget.WaitDialog;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private Application mApplication;

    public AppModule(Application mApplication) {
        this.mApplication = mApplication;
    }


    @Provides
    @Singleton
    public Application provideApplication(){
        return mApplication;
    }

    @Provides
    @Singleton
    public Gson provideGson(){
        return new Gson();
    }

    @Provides
    @Singleton
    public WaitDialog provideWaitDialog(Context context){
        return new WaitDialog(context);
    }



}
