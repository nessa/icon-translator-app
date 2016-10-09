package com.icon.nsales.icontranslate.app;

import android.app.Application;

/**
 * My application class.
 * Author: Noelia Sales Montes, noelia.salesmontes <at> gmail.com
 *
 * Needed to use Dagger dependency injection.
 */
public class MyApplication extends Application {
    private ApplicationComponent mComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mComponent = DaggerApplicationComponent.builder()
            .applicationModule(new ApplicationModule(this))
            .build();
    }

    public ApplicationComponent getComponent() {
        return mComponent;
    }
}
