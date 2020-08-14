package com.hym.appstore.app;

import android.app.Application;
import android.content.Context;
import android.view.View;

import androidx.multidex.MultiDex;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.hjq.toast.ToastUtils;
import com.hym.appstore.common.db.DBManager;
import com.hym.appstore.dagger2.component.DaggerAppComponent;
import com.hym.appstore.dagger2.module.AppModule;
import com.hym.appstore.presenter.contract.RecommendContract;
import com.xuexiang.xui.XUI;
import com.yanzhenjie.nohttp.InitializationConfig;
import com.yanzhenjie.nohttp.Logger;
import com.yanzhenjie.nohttp.NoHttp;



public class MyApplication extends Application {


    private DaggerAppComponent mAppComponent;
    private View mView;



    public static MyApplication get(Context context){
        return (MyApplication) context.getApplicationContext();
    }

    public DaggerAppComponent getAppComponent(){
        return mAppComponent;
    }



    @Override
    public void onCreate() {
        super.onCreate();
        DBManager.initDB(this);
        mAppComponent = (DaggerAppComponent) DaggerAppComponent.builder().appModule(new AppModule(this)).build();

        XUI.init(this); //初始化UI框架
        //XUI.debug(true);  //开启UI框架调试日志

        MultiDex.install(this);

        // 在 Application 中初始化
        ToastUtils.init(this);


        //初始化fresco
        Fresco.initialize(this);

    }

    public View getView() {
        return mView;
    }

    public void setView(View mView) {
        this.mView = mView;
    }
}
