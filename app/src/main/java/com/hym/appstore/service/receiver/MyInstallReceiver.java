package com.hym.appstore.service.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyInstallReceiver extends BroadcastReceiver {

    private MyInstallListener listener;

    /**
     11      * 注册监听器
     12      * @param listener
     13      */
    public void registerListener(MyInstallListener listener){
        this.listener  = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        //安装广播
        if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {
            String packageName = intent.getDataString().split(":")[1];
//            Log.d("hymmm", "onReceive: " + "安装了应用："+packageName);
            if (listener != null) {
                listener.PackageAdded(packageName);
            }

        }


        //卸载广播
        if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {
            String packageName = intent.getDataString().split(":")[1];
//            Log.d("hymmm", "onReceive: " + "卸载了应用："+packageName);
            if (listener != null) {
                listener.PackageRemoved(packageName);
            }
        }


        //覆盖安装广播
        if (intent.getAction().equals("android.intent.action.PACKAGE_REPLACED")) {
            String packageName = intent.getDataString().split(":")[1];
//            Log.d("hymmm", "onReceive: " + "覆盖安装了应用："+packageName);
            if (listener != null) {
                listener.PackageReplaced(packageName);
            }
        }
    }
}
