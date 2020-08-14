package com.hym.appstore.dagger2.component;


import com.hym.appstore.dagger2.module.AppDetailModule;
import com.hym.appstore.dagger2.module.AppManagerModule;
import com.hym.appstore.dagger2.scope.FragmentScope;
import com.hym.appstore.ui.fragment.AppDetailFragment;
import com.hym.appstore.ui.fragment.AppManagerFragment;
import com.hym.appstore.ui.fragment.DownloadedFragment;
import com.hym.appstore.ui.fragment.DownloadingFragment;
import com.hym.appstore.ui.fragment.InstalledAppAppFragment;

import dagger.Component;

@FragmentScope
@Component(modules = AppManagerModule.class,dependencies = AppComponent.class)
public interface AppManagerComponent {
    void inject(AppManagerFragment fragment);
//    void injectDownloaded(DownloadedFragment fragment);
//    void injecInstalled(InstalledAppAppFragment fragment);

}
