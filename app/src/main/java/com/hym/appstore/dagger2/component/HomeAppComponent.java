package com.hym.appstore.dagger2.component;


import com.hym.appstore.dagger2.module.HomeAppModule;
import com.hym.appstore.dagger2.scope.ActivityScope;
import com.hym.appstore.ui.activity.HomeAppActivity;

import dagger.Component;

@ActivityScope
@Component(modules = HomeAppModule.class,dependencies= AppComponent.class)
public interface HomeAppComponent {
    void inject(HomeAppActivity activity);
}
