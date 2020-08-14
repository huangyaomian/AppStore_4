package com.hym.appstore.dagger2.component;


import com.hym.appstore.dagger2.module.LoginModule;
import com.hym.appstore.dagger2.module.SortModule;
import com.hym.appstore.dagger2.scope.ActivityScope;
import com.hym.appstore.ui.activity.LoginActivity;
import com.hym.appstore.ui.fragment.SortFragment;

import dagger.Component;

@ActivityScope
@Component(modules = SortModule.class,dependencies = AppComponent.class)
public interface SortComponent {
    void inject(SortFragment fragment);
}
