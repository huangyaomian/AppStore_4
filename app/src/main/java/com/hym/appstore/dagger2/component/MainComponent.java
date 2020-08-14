package com.hym.appstore.dagger2.component;


import com.hym.appstore.dagger2.module.LoginModule;
import com.hym.appstore.dagger2.module.MainModule;
import com.hym.appstore.dagger2.scope.ActivityScope;
import com.hym.appstore.ui.activity.LoginActivity;
import com.hym.appstore.ui.activity.MainActivity;

import dagger.Component;

@ActivityScope
@Component(modules = MainModule.class,dependencies= AppComponent.class)
public interface MainComponent {
    void inject(MainActivity activity);
}
