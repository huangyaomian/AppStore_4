package com.hym.appstore.dagger2.component;


import com.hym.appstore.dagger2.module.AppDetailModule;
import com.hym.appstore.dagger2.module.AppInfoModule;
import com.hym.appstore.dagger2.scope.FragmentScope;
import com.hym.appstore.ui.activity.AppDetailsActivity;
import com.hym.appstore.ui.activity.AppDetailsActivity_ViewBinding;
import com.hym.appstore.ui.fragment.AppDetailFragment;
import com.hym.appstore.ui.fragment.GameFragment;
import com.hym.appstore.ui.fragment.RankingFragment;
import com.hym.appstore.ui.fragment.SortAppFragment;

import dagger.Component;

@FragmentScope
@Component(modules = AppDetailModule.class,dependencies = AppComponent.class)
public interface AppDetailComponent {
    void inject(AppDetailFragment fragment);
}
