package com.icon.nsales.icontranslate;

import android.app.Application;

/**
 * Created by nsales on 7/10/16.
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
