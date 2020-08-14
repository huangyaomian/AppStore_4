package com.hym.appstore.dagger2.component;


import com.hym.appstore.dagger2.module.LoginModule;
import com.hym.appstore.dagger2.scope.ActivityScope;
import com.hym.appstore.ui.activity.LoginActivity;

import dagger.Component;

@ActivityScope
@Component(modules = LoginModule.class,dependencies = AppComponent.class)
public interface LoginComponent {
    void inject(LoginActivity activity);
}
