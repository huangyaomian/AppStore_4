package com.hym.appstore.dagger2.component;

import com.hym.appstore.dagger2.module.RecommendModule;
import com.hym.appstore.dagger2.scope.FragmentScope;
import com.hym.appstore.ui.fragment.RecommendFragment;

import dagger.Component;

@FragmentScope
@Component(modules = RecommendModule.class,dependencies = AppComponent.class)
public interface RecommendComponent {

    void inject(RecommendFragment fragment);

}
