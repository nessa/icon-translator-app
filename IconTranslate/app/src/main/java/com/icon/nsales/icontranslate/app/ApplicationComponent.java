package com.icon.nsales.icontranslate.app;

import android.content.Context;

import com.icon.nsales.icontranslate.views.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Dagger component class.
 * Author: Noelia Sales Montes, noelia.salesmontes <at> gmail.com
 *
 * Needed to use Dagger dependency injection.
 */
@Singleton // Constraints this component to one-per-application or unscoped bindings.
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    //Exposed to sub-graphs.
    Context context();

    void inject(MainActivity mainActivity);
}