package com.hym.appstore.dagger2.component;


import com.hym.appstore.dagger2.module.SubjectModule;
import com.hym.appstore.dagger2.scope.FragmentScope;
import com.hym.appstore.ui.fragment.BaseSubjectFragment;

import dagger.Component;

@FragmentScope
@Component(modules = SubjectModule.class,dependencies = AppComponent.class)
public interface SubjectComponent {
    void inject(BaseSubjectFragment fragment);
}
