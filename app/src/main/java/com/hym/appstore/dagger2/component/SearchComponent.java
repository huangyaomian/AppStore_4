package com.hym.appstore.dagger2.component;


import com.hym.appstore.dagger2.module.SearchModule;
import com.hym.appstore.dagger2.scope.ActivityScope;
import com.hym.appstore.ui.activity.SearchActivity;

import dagger.Component;

@ActivityScope
@Component(modules = SearchModule.class,dependencies= AppComponent.class)
public interface SearchComponent {
    void inject(SearchActivity activity);
}
